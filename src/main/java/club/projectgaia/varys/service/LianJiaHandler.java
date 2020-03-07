package club.projectgaia.varys.service;

import club.projectgaia.varys.domain.po.LianJiaDeal;
import club.projectgaia.varys.domain.po.LianJiaJob;
import club.projectgaia.varys.repository.LianJiaDealRepository;
import club.projectgaia.varys.repository.LianJiaJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class LianJiaHandler {
    @Value("${lianjia.sitemap-path}")
    private String siteMapPath;
    @Autowired
    private LianJiaJobRepository lianJiaJobRepository;
    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;
    @Autowired
    private LianJiaDealRepository lianJiaDealRepository;
    @Autowired
    private TransactionalHandler transactionalHandler;

    public void createDealHouse() {
        Random r = new Random();
        //Sort.Direction.DESC
        Long start = 100000L;
        log.info("开始ID：{}", start);
        Pageable pageable = PageRequest.of(0, 100, Sort.by("id"));
        List<LianJiaJob> allJob = lianJiaJobRepository.getAllByTypeEqualsAndIdAfterAndCramFlagIsFalse("成交", start, pageable);
        AtomicInteger i = new AtomicInteger();
        while (true) {
            AtomicReference<Long> lastId = new AtomicReference<>(0L);
            List<LianJiaDeal> dealList = new ArrayList<>();
            List<LianJiaJob> jobList = new ArrayList<>();
            Set<String> cids = new HashSet<>();
            allJob.forEach(x -> {
                try {
                    org.jsoup.nodes.Document doc = Jsoup.parse(getContentStr(x.getUrl()));
                    LianJiaDeal dealHouse = makeDeal(doc);
                    if (!lianJiaDealRepository.existsByCid(dealHouse.getCid()) && !cids.contains(dealHouse.getCid())) {
                        dealList.add(dealHouse);
                        cids.add(dealHouse.getCid());
                    }
                    x.setCramFlag(true);
                    jobList.add(x);
                    Thread.sleep((r.nextInt(3) + 1) * 1000);
                    //Thread.sleep(r.nextInt(4000));
                } catch (Exception e) {
                    log.error("爬取" + x.getUrl() + "失败！", e);
                    x.setCramFlag(null);
                    jobList.add(x);
                }
                i.getAndIncrement();
                lastId.set(x.getId());
            });
            transactionalHandler.saveAllDeal(jobList, dealList);
            allJob = lianJiaJobRepository.getAllByTypeEqualsAndIdAfterAndCramFlagIsFalse("成交", start, pageable);
            if (allJob.size() == 0) {
                break;
            }
            log.info("爬取{}个,最后一个任务id{}", i.get(), lastId.get());
        }
    }

    public void checkNewJob() {
        try {
            //创建一个解析器
            SAXBuilder builder = new SAXBuilder();

            //将流加载到解析器中。
            Document document = builder.build(getContent("https://bj.lianjia.com/sitemap/bj_index.xml"));

            //获取文档的根节点
            Element rootElement = document.getRootElement();

            //循环遍历所有子节点
            List<Element> list = rootElement.getChildren();
            for (Element element : list) {
                String loc = element.getChild("loc").getContent(0).getValue();
                String lastmod = element.getChild("lastmod").getContent(0).getValue();
                File dataDir = new File(siteMapPath + File.separator + lastmod);
                if (!dataDir.exists()) {
                    dataDir.mkdir();
                }

                if (loc.contains("cj")) {
                    String fileName = loc.replace("https://bj.lianjia.com/sitemap/", "");
                    File dataFile = new File(siteMapPath + File.separator + lastmod + File.separator + fileName);
                    if (dataFile.exists()) {
                        write2File(loc, dataFile);
                        cramLJByFile(dataFile, "成交");
                    }
                }
            }
        } catch (IOException e) {
            log.error("连接错误", e);
        } catch (JDOMException e) {
            log.error("xml格式错误", e);
        }
    }

    private void cramLJ(BufferedReader bf, String type) throws Exception {
        int i = 0;
        LianJiaJob job = null;
        String str;
        List<LianJiaJob> jobs = new ArrayList<>();
        Set<String> jobStr = new HashSet<>();
        while ((str = bf.readLine()) != null) {
            if ("<url>".equals(str)) {
                job = new LianJiaJob();
                job.setType(type);
                job.setCramFlag(false);
            } else if (str.startsWith("<loc>")) {
                job.setUrl(str.replace("<loc>", "").replace("</loc>", ""));
                if (!job.getUrl().contains("html")) {
                    job.setCramFlag(null);
                }
            } else if (str.startsWith("<lastmod>")) {
                job.setLastmod(str.replace("<lastmod>", "").replace("</lastmod>", ""));
            } else if ("</url>".equals(str)) {
                if (!lianJiaJobRepository.existsByUrl(job.getUrl()) && !jobStr.contains(job.getUrl())) {
                    jobs.add(job);
                    jobStr.add(job.getUrl());
                }
                if (i % 1000 == 0) {
                    lianJiaJobRepository.saveAll(jobs);
                    i += jobs.size();
                    jobs.clear();
                    jobStr.clear();

                }
            }

        }
        lianJiaJobRepository.saveAll(jobs);
        i += jobs.size();
        log.info("新增job{}个", i);
    }

    private void cramLJByFile(File path, String type) {
        try (final InputStream instream = new FileInputStream(path);
             final Reader reader = new InputStreamReader(instream, "utf-8");
             BufferedReader bf = new BufferedReader(reader)) {
            log.info("处理文件:{}", path.getCanonicalPath());
            cramLJ(bf, type);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void cramLJ(String url, String type) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(new BasicHeader("user-agent", "Mozilla/5.0 (compatible; varysSpider/1.0; +http://www.baidu.com/search/spider.html)"));
        try {
            CloseableHttpResponse response = this.client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response != null) {
                Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                        "HTTP entity too large to be buffered in memory");
                Charset charset = getCharset(entity);
                try (final InputStream instream = entity.getContent();
                     final Reader reader = new InputStreamReader(instream, charset);
                     BufferedReader bf = new BufferedReader(reader)) {
                    cramLJ(bf, type);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private Charset getCharset(HttpEntity entity) {
        ContentType contentType = null;
        try {
            contentType = ContentType.get(entity);
        } catch (final UnsupportedCharsetException ex) {
            log.error(ex.getMessage(), ex);
        }
        if (contentType != null) {
            if (contentType.getCharset() == null) {
                contentType = contentType.withCharset("utf-8");
            }
        } else {
            contentType = ContentType.DEFAULT_TEXT.withCharset("utf-8");
        }

        Charset charset = null;
        if (contentType != null) {
            charset = contentType.getCharset();
            if (charset == null) {
                final ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
                charset = defaultContentType != null ? defaultContentType.getCharset() : null;
            }
        }
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        return charset;
    }

    private void write2File(String url, File file) {
        try (OutputStream outputStream = new FileOutputStream(file);
             InputStream inputStream = getContent(url)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            byte b[] = new byte[1024];
            int len = 0;

            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }

        } catch (IOException e) {
            log.error("写文件错误！", e);
        }
    }

    private String getContentStr(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        //get.setHeader(new BasicHeader("user-agent", "Mozilla/5.0 (compatible; varysSpider/1.0; +http://www.baidu.com/search/spider.html)"));
        CloseableHttpResponse response = this.client.execute(get);
        return EntityUtils.toString(response.getEntity(), "utf-8");
    }

    private InputStream getContent(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        //get.setHeader(new BasicHeader("user-agent", "Mozilla/5.0 (compatible; varysSpider/1.0; +http://www.baidu.com/search/spider.html)"));
        CloseableHttpResponse response = this.client.execute(get);
        return response.getEntity().getContent();
    }


    private LianJiaDeal makeDeal(org.jsoup.nodes.Document doc) throws Exception {
        LianJiaDeal dealHouse = new LianJiaDeal();
        org.jsoup.nodes.Element title = doc.selectFirst("div.house-title > div.wrapper > h1");
        dealHouse.setTitle(title.text());

        Elements location = doc.select("div.deal-bread > a[href]");
        org.jsoup.nodes.Element district = location.get(2);
        dealHouse.setDistrict(district.text().replace("二手房成交", ""));

        org.jsoup.nodes.Element area = location.get(3);
        dealHouse.setArea(area.text().replace("二手房成交", ""));

        org.jsoup.nodes.Element community = location.get(4);
        dealHouse.setCommunity(community.text().replace("二手房成交", ""));

        Elements price = doc.select("div.price > span.dealTotalPrice > i");
        dealHouse.setTransactionPrice(price.text());

        String dealDate = doc.selectFirst("div.house-title > div.wrapper > span").text().replace(" 成交", "");
        dealHouse.setDealDate(dealDate);

        Elements msg = doc.select("div.info > div.msg > span");
        msg.forEach(x -> {
            String value = x.selectFirst("label").text();
            String property = x.text().replace(value, "");
            if (!"暂无数据".equals(value)) {
                switch (property) {
                    case "挂牌价格（万）":
                        dealHouse.setListedPrice(value);
                        break;
                    case "成交周期（天）":
                        dealHouse.setTransactionCycle(value);
                        break;
                    case "带看（次）":
                        dealHouse.setTakeLook(value);
                        break;
                    case "关注（人）":
                        dealHouse.setFollow(value);
                        break;
                    default:
                        break;

                }
            }
        });

        Elements info = doc.select("section.houseContentBox > div.fl > div.newwrap > div.introContent > div.base > div.content > ul > li");
        info.forEach(x -> {
            String property = x.selectFirst("span").text();
            String value = x.text().replace(property, "");
            if (!"暂无数据".equals(value)) {
                switch (property) {
                    case "房屋户型":
                        dealHouse.setHouseType(value);
                        break;
                    case "所在楼层":
                        dealHouse.setFloor(value);
                        break;
                    case "建筑面积":
                        dealHouse.setBuildArea(value);
                        break;
                    case "户型结构":
                        dealHouse.setHouseStructure(value);
                        break;
                    case "套内面积":
                        dealHouse.setHouseArea(value);
                        break;
                    case "建筑类型":
                        dealHouse.setBuildType(value);
                        break;
                    case "房屋朝向":
                        dealHouse.setOrientation(value);
                        break;
                    case "建成年代":
                        dealHouse.setBuildAge(value);
                        break;
                    case "建筑结构":
                        dealHouse.setBuildStructure(value);
                        break;
                    case "供暖方式":
                        dealHouse.setHeatingMode(value);
                        break;
                    case "梯户比例":
                        dealHouse.setLadderHousehold(value);
                        break;
                    case "产权年限":
                        dealHouse.setPropertyYear(value);
                        break;
                    case "配备电梯":
                        dealHouse.setLadder(value);
                        break;
                    default:
                        break;

                }
            }
        });
        Elements deal = doc.select("section.houseContentBox > div.fl > div.newwrap > div.introContent > div.transaction > div.content > ul > li");
        deal.forEach(x -> {
            String property = x.selectFirst("span").text();
            String value = x.text().replace(property, "");
            if (!"暂无数据".equals(value)) {
                switch (property) {
                    case "链家编号":
                        dealHouse.setCid(value);
                        break;
                    case "交易权属":
                        dealHouse.setTradingRight(value);
                        break;
                    case "挂牌时间":
                        dealHouse.setListingDate(value);
                        break;
                    case "房屋用途":
                        dealHouse.setHouseUse(value);
                        break;
                    case "房屋年限":
                        dealHouse.setHouseLife(value);
                        break;
                    case "房权所属":
                        dealHouse.setHouseOwnership(value);
                        break;
                    default:
                        break;

                }
            }
        });
        Elements history = doc.select("div.chengjiao_record > ul.record_list > li");
        if (history != null) {
            List<String> h = new ArrayList<>();
            history.forEach(x -> {
                h.add(x.selectFirst("span").text() + "," + x.selectFirst("p").text());
            });
            dealHouse.setHistory(String.join(";", h));
        }

        Elements labels = doc.select("div.tags > div.content > a");
        if (labels != null) {
            List<String> l = new ArrayList<>();
            labels.forEach(x -> {
                l.add(x.text());
            });
            dealHouse.setLabel(String.join(";", l));
        }

        Elements remarks = doc.select("div.baseattribute");
        if (remarks != null) {
            List<String> r = new ArrayList<>();
            remarks.forEach(x -> {
                r.add(x.selectFirst("div.name").text() + ":" + x.selectFirst("div.content").text());
            });
            dealHouse.setRemark(String.join("\r\n", r));
        }

        return dealHouse;
    }

}
