package club.projectgaia.varys.service;

import club.projectgaia.varys.domain.po.*;
import club.projectgaia.varys.repository.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.hibernate.exception.DataException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SpriderHandler {
    @Autowired
    NewsAbstractRepository newsAbstractRepository;

    @Autowired
    NewsTypeRepository newsTypeRepository;

    @Autowired
    NewsDailyRepository newsDailyRepository;
    @Autowired
    NewsContentRepository newsContentRepository;
    @Autowired
    ForeignNewsRepository foreignNewsRepository;

    @Autowired
    LianJiaJobRepository lianJiaJobRepository;

    private static Pattern p = Pattern.compile("来源：(.*)</span>");
    private static Pattern timeP = Pattern.compile("20[0-9]{2}-[0-9]{2}-[0-9]{2}");


    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;

    public static final Logger log = LoggerFactory.getLogger(SpriderHandler.class);

    public void test() throws Exception {

        String t = "{\"status\":0,\"data\":{\"list\":[{\"DocID\":1122919298,\"Title\":\"精彩视频\",\"NodeId\":1136053,\"PubTime\":\"2018-05-31 16:55:48\",\"LinkUrl\":\"\",\"Abstract\":\\{ showColumnTit: 'yes', columnClass: '', columnTitClass: 'skinTit2', columnTitIsPic: '', columnTitBgPic: '', showColumnTitMore: '', columnPcMarginTop: '', columnMbMarginTop: '', columnPcMarginBottom: '', columnMbMarginBottom: '', columnPcPaddingTop: '', columnMbPaddingTop: '', columnPcPaddingBottom: '', columnMbPaddingBottom: '', columnTitPcH: '', columnTitMbH: '', SetCompose: [{ composeName: 'foucs-1', ComposeClass: '', advSkin: [''], composeCon: { composeConClass: '', topDistance: '20px', bottomDistance: '', advSkin: [''], modules: [{ dataId: '01', moduleTit: '', moduleTitLink: '', moduleSubTit: '', modulePcH: '', moduleMbH: '', MaxNum: 6, moduleClass: 'margin10B ElemlisB', advSkin: [''], animation: [], SetElem: { elemPcH: '', elemMbH: '', picPcH: '569px', picMbH: '223px', titPcH: '', titMbH: '', abstracPcH: '', abstracMbH: '', elemPcDistanceB: '', elemMbDistanceB: '', picPcDistanceB: '', picMbDistanceB: '', titPcDistanceB: '', titMbDistanceB: '', abstracPcDistanceB: '', abstracMbDistanceB: '', } }] } }],}\",\"keyword\":null,\"Editor\":null,\"Author\":\"刘梦姣\",\"IsLink\":1,\"SourceName\":null,\"PicLinks\":\"\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null}]},\"totalnum\":125}";
        System.out.println(t.indexOf("\"Abstract\":\\"));
        System.out.println(t.replaceFirst("\"Abstract\":\\\\.*}\",", ""));
        JSON.toJSON(t.replaceFirst("\"Abstract\":\\\\.*}\"", ""));

    }

    public void getAll() throws Exception {
        List<NewsType> allTypes = newsTypeRepository.findAll();
        String url = "http://qc.wa.news.cn/nodeart/list?nid=%s&pgnum=%d&cnt=%d&orderby=1";
        Integer cnt = 100;
        int total = 0;
        for (NewsType type : allTypes) {
            Integer count = type.getCount();
            Integer pgnum = 1;
            while (count > 0) {
                List<NewsAbstract> save = new ArrayList<>();

                String trueUrl;
                if (count < cnt) {
                    trueUrl = String.format(url, type.getNid(), pgnum, count);
                    count -= count;
                } else {
                    trueUrl = String.format(url, type.getNid(), pgnum, cnt);
                    count -= cnt;
                }
                pgnum++;

                HttpGet get = new HttpGet(trueUrl);
                get.setHeader("Accept", "*/*");
                get.setHeader("Accept-Encoding", "gzip, deflate");
                get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                get.setHeader("Connection", "keep-alive");
                get.setHeader("Host", "qc.wa.news.cn");
                get.setHeader("Referer", "http://www.xinhuanet.com");
                get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
                try {
                    CloseableHttpResponse response = client.execute(get);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(response.getEntity(), "utf-8");

                        result = result.substring(1, result.length() - 1);
                        if (result.indexOf("\"Abstract\":\\") > 0) {
                            result = result.replaceFirst("\"Abstract\":\\\\.*}\",", "");
                        }

                        JSONObject retJson = JSON.parseObject(result.substring(0, result.length() - 1));
                        JSONArray newsList = retJson.getJSONObject("data").getJSONArray("list");
                        for (int s = 0; s < newsList.size(); s++) {
                            NewsAbstract news = newsList.getObject(s, NewsAbstract.class);
                            if (newsList.getJSONObject(s).getJSONArray("allPics").size() > 0) {
                                news.setPics(newsList.getJSONObject(s).getJSONArray("allPics").getString(0));
                            }
                            if (StringUtils.isNotEmpty(type.getNewsType())) {
                                news.setNewsType(type.getNewsType());
                            }
                            news.setNid(type.getNid());
                            save.add(news);
                            total += 1;
                            if (total % 5000 == 0) {
                                log.info(total + "");
                            }
                        }
                        newsAbstractRepository.saveAll(save);

                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    log.error(type.getNid() + ":" + pgnum);
                    break;
                }

            }
        }
    }

    public void getByType(String ntype) throws Exception {
        String url = "http://qc.wa.news.cn/nodeart/list?nid=%s&pgnum=%d&cnt=%d&orderby=1";
        Integer cnt = 100;
        List<NewsType> allTypes = newsTypeRepository.getAllByNewsType(ntype);
        for (NewsType type : allTypes) {
            Integer count = type.getCount();
            Integer pgnum = 1;
            while (count > 0) {
                List<NewsAbstract> save = new ArrayList<>();

                String trueUrl;
                if (count < cnt) {
                    trueUrl = String.format(url, type.getNid(), pgnum, count);
                    count -= count;
                } else {
                    trueUrl = String.format(url, type.getNid(), pgnum, cnt);
                    count -= cnt;
                }
                pgnum++;

                HttpGet get = new HttpGet(trueUrl);
                get.setHeader("Accept", "*/*");
                get.setHeader("Accept-Encoding", "gzip, deflate");
                get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                get.setHeader("Connection", "keep-alive");
                get.setHeader("Host", "qc.wa.news.cn");
                get.setHeader("Referer", "http://www.xinhuanet.com");
                get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
                try {
                    CloseableHttpResponse response = client.execute(get);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        String result = EntityUtils.toString(response.getEntity(), "utf-8");

                        result = result.substring(1, result.length() - 1);
                        if (result.indexOf("\"Abstract\":\\") > 0) {
                            result = result.replaceFirst("\"Abstract\":\\\\.*}\",", "");
                        }

                        JSONObject retJson = JSON.parseObject(result.substring(0, result.length() - 1));
                        JSONArray newsList = retJson.getJSONObject("data").getJSONArray("list");
                        for (int s = 0; s < newsList.size(); s++) {
                            NewsAbstract news = newsList.getObject(s, NewsAbstract.class);
                            if (newsList.getJSONObject(s).getJSONArray("allPics").size() > 0) {
                                news.setPics(newsList.getJSONObject(s).getJSONArray("allPics").getString(0));
                            }
                            if (StringUtils.isNotEmpty(type.getNewsType())) {
                                news.setNewsType(type.getNewsType());
                            }
                            save.add(news);
                        }
                        newsAbstractRepository.saveAll(save);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    break;
                }
            }
        }

    }

    public void getType() throws Exception {
        try (FileReader fr = new FileReader("/Users/deepclue/Desktop/index.txt");
             BufferedReader br = new BufferedReader(fr);
             FileWriter fw = new FileWriter("/Users/deepclue/Desktop/save.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            String url = "http://qc.wa.news.cn/nodeart/list?nid=%s&pgnum=1&cnt=1";
            Pattern p = Pattern.compile(".com/([a-zA-Z]+)/?20");

            String index = br.readLine();
            int i = 0;
            int total = 0;
            while (index != null) {
                i++;
                HttpGet get = new HttpGet(String.format(url, index));
                get.setHeader("Accept", "*/*");
                get.setHeader("Accept-Encoding", "gzip, deflate");
                get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                get.setHeader("Connection", "keep-alive");
                get.setHeader("Host", "qc.wa.news.cn");
                get.setHeader("Referer", "http://www.xinhuanet.com");
                get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
                //get.setHeader("Cookie", "wdcid=6550ff071e1722e0; tma=4434860.51068288.1532867142302.1532867142302.1532867142302.1; tmd=1.4434860.51068288.1532867142302.; fingerprint=78461c690986347c3c3400a83e1f74ff; bfd_g=87205254007bf95200005f7b0160a96a5b573e49; pc=e60e67c3e2e2ba368070a8fbdb573ec7.1532442926.1532867392.17");

                CloseableHttpResponse response = client.execute(get);
                NewsType newsType = new NewsType();

                try {
                    String result = EntityUtils.toString(response.getEntity(), "utf-8");
                    if (result.indexOf("\"Abstract\":\\") > 0) {
                        result = result.replaceFirst("\"Abstract\":\\\\.*}\",", "");
                    }
                    JSONObject retJson = JSON.parseObject(result.substring(1, result.length() - 1));

                    newsType.setCount(retJson.getInteger("totalnum"));
                    total += retJson.getInteger("totalnum");
                    newsType.setNid(index);
                    JSONObject news = retJson.getJSONObject("data").getJSONArray("list").getJSONObject(0);
                    Matcher m = p.matcher(news.getString("LinkUrl"));
                    if (m.find()) {
                        newsType.setNewsType(m.group(1));
                    }
                    newsType.setTitle(news.getString("Title"));
                    newsTypeRepository.save(newsType);
                } catch (JSONException e) {
                    log.warn(index + " beJson error!");
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    log.warn(index + " null pointer");
                } catch (DataIntegrityViolationException e) {
                    log.warn(index + " save db error");
                    bw.write(JSON.toJSONString(newsType));
                    bw.newLine();
                    bw.flush();
                } catch (Exception e) {

                    log.info("");
                } finally {
                    index = br.readLine();
                }

                if (i % 1000 == 0) {
                    log.info(i + "");
                }
            }

            log.info("total news:" + total);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public void getNewsAbstract(String nid) throws Exception {
        Long stamp = 1533369117247L;
        String newsType = "world";
        String url = "http://qc.wa.news.cn/nodeart/list?nid=" + nid + "&pgnum=%d&cnt=100";
        int i = 0;
        int total = 0;
        while (true) {
            i++;
            stamp++;
            List<NewsAbstract> save = new ArrayList<>();
            HttpGet get = new HttpGet(String.format(url, i));
            //get.setHeader("Accept", "*/*");
            get.setHeader("Accept-Encoding", "gzip, deflate");
            get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            get.setHeader("Connection", "keep-alive");
            get.setHeader("Host", "qc.wa.news.cn");
            get.setHeader("Referer", "http://www.xinhuanet.com/auto/syc.htm");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
            //get.setHeader("Cookie", "wdcid=6550ff071e1722e0; tma=4434860.51068288.1532867142302.1532867142302.1532867142302.1; tmd=1.4434860.51068288.1532867142302.; fingerprint=78461c690986347c3c3400a83e1f74ff; bfd_g=87205254007bf95200005f7b0160a96a5b573e49; pc=e60e67c3e2e2ba368070a8fbdb573ec7.1532442926.1532867392.17");

            CloseableHttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                result = result.substring(1, result.length() - 1);

                try {
                    JSONObject retJson = JSON.parseObject(result.substring(0, result.length() - 1));
                    JSONArray newsList = retJson.getJSONObject("data").getJSONArray("list");
                    for (int s = 0; s < newsList.size(); s++) {
                        NewsAbstract news = newsList.getObject(s, NewsAbstract.class);
                        if (newsList.getJSONObject(s).getJSONArray("allPics").size() > 0) {
                            news.setPics(newsList.getJSONObject(s).getJSONArray("allPics").getString(0));
                        }
                        if (StringUtils.isNotEmpty(newsType)) {
                            news.setNewsType(newsType);
                        }
                        total++;
                        System.out.printf(news.getTitle());

                        //save.add(news);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    log.info("total:{}", total);
                    break;
                }

                //newsAbstractRepository.saveAll(save);
            }

        }
    }

    public void getIndex() {
        try (FileWriter fw = new FileWriter("C:\\Users\\Administrator\\Desktop\\index.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            int i = 0;
            while (true) {
                i++;
                if (i % 1000 == 0) {
                    bw.flush();
                    log.info(i + "");
                }
                if (!String.valueOf(i).startsWith("1")) {
                    continue;
                }
                String url = "http://qc.wa.news.cn/nodeart/list?nid=%d&pgnum=1&cnt=1";
                HttpGet get = new HttpGet(String.format(url, i));
                get.setHeader("Accept", "*/*");
                get.setHeader("Accept-Encoding", "gzip, deflate");
                get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
                get.setHeader("Connection", "keep-alive");
                get.setHeader("Host", "qc.wa.news.cn");
                get.setHeader("Referer", "http://www.xinhuanet.com/auto/syc.htm");
                get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
                get.setHeader("Cookie", "wdcid=6550ff071e1722e0; tma=4434860.51068288.1532867142302.1532867142302.1532867142302.1; tmd=1.4434860.51068288.1532867142302.; fingerprint=78461c690986347c3c3400a83e1f74ff; bfd_g=87205254007bf95200005f7b0160a96a5b573e49; pc=e60e67c3e2e2ba368070a8fbdb573ec7.1532442926.1532867392.17");

                CloseableHttpResponse response = client.execute(get);

                String result = EntityUtils.toString(response.getEntity(), "utf-8");
                if (result.contains("\"data\":{\"list\"")) {
                    log.info(i + " can use");
                    bw.write(i + "");
                    bw.newLine();
                }
                if (i == 20000000) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveContent() throws Exception {
        log.info("start save content");
        long count = newsDailyRepository.count();
        Pageable pageable = PageRequest.of(0, 100);
        boolean flag = true;
        while (flag) {
            List<NewsContent> allSave = new ArrayList<>();
            List<NewsDaily> daily = newsDailyRepository.findAllByDocIDNotNullOrderByCreateTimeDesc(pageable);
            count -= 100;
            for (NewsDaily news : daily) {
                try {
                    NewsContent newsContent = newsContentRepository.findByDocID(news.getDocID());
                    if (newsContent != null) {
                        flag = false;
                        continue;
                    }
                    Document doc = Jsoup.connect(news.getLinkUrl()).get();
                    Element keyWordsElement = doc.selectFirst("meta[name=keywords]");
                    String keyWords = "";
                    if (keyWordsElement != null) {
                        keyWords = keyWordsElement.attr("content").replace("\r", "").replace("\n", "");
                    }
                    String description = "";
                    Element desc = doc.selectFirst("meta[name=description]");
                    if (desc != null) {
                        description = desc.attr("content").replace("\r", "").replace("\n", "").replace("　", "");
                        description = description.replace(news.getTitle(), "").replace("-", "");
                    }

                    String time = null;
                    Element timeE = doc.selectFirst("span.h-time");
                    if (timeE != null) {
                        time = timeE.text();
                    }
                    Element sourceE = doc.selectFirst("em#source");
                    String source;
                    if (sourceE != null) {
                        source = sourceE.text();
                    } else {
                        Matcher m = p.matcher(doc.outerHtml());
                        if (m.find()) {
                            source = m.group(1).replace("\r", "").replace("\n", "").replace("　", "").replace(" ", "");
                        } else {
                            source = "新华社";
                        }
                    }
                    StringBuilder content = new StringBuilder();
                    Elements elements = doc.select("p");
                    elements.forEach(x -> {
                        content.append(x.text());
                    });
                    String all = content.toString().replaceAll("\r", "").replaceAll("\n", "").replace("　", "");
                    NewsContent save = new NewsContent();
                    save.setContent(all);
                    save.setTime(time);
                    save.setDescription(description);
                    save.setDocID(news.getDocID());
                    save.setKeyWords(keyWords);
                    save.setSource(source);
                    save.setTitle(news.getTitle());
                    save.setLinkUrl(news.getLinkUrl());
                    allSave.add(save);

                } catch (Exception e) {
                    log.warn("get news error !" + news.getTitle() + ":" + news.getDocID(), e);
                }
            }
            newsContentRepository.saveAll(allSave);
            pageable = pageable.next();

            if (count <= 0) {
                flag = false;
            }

        }

    }

    public void getWhxw() throws Exception {
        log.info("start get whxw");
        Document doc = Jsoup.connect("http://www.xinhuanet.com/whxw.htm").get();
        Elements hideDataById = doc.select("#hideData");
        Elements allLi = hideDataById.select(".clearfix");

        List<NewsDaily> needSave = new ArrayList<>();
        for (Element clearfixli : allLi) {
            Document clearfixliDoc = Jsoup.parse(clearfixli.toString());
            Element url = clearfixliDoc.select("a[href]").first();
            Element time = clearfixliDoc.select("span").first();
            NewsDaily daily = new NewsDaily();
            daily.setLinkUrl(url.attributes().get("href"));
            daily.setTitle(url.text());
            daily.setTime(time.text());
            Pattern p = Pattern.compile("20\\d{2}-\\d{2}/\\d{2}/c_(\\d+).htm");
            Matcher m = p.matcher(url.attributes().get("href"));
            if (m.find()) {
                daily.setDocID(m.group(1));
            } else {
                daily.setDocID(daily.getLinkUrl());
            }

            NewsDaily need = newsDailyRepository.findByDocID(daily.getDocID());
            if (need == null) {
                needSave.add(daily);
            }
        }
        if (needSave.size() > 0) {
            newsDailyRepository.saveAll(needSave);
        }
    }

    private String getContent(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = this.client.execute(get);
        return EntityUtils.toString(response.getEntity(), "utf-8");
    }

    public void getForeignNews(String key, int start, int end) throws Exception {
        //jzhsl_673025 dhdw_673027 wjbzhd
        String defUrl = "https://www.fmprc.gov.cn/web/fyrbt_673021/" + key + "/";
        for (int i = start; i < end; i++) {
            Document doc;
            if (i == 0) {
                doc = Jsoup.parse(getContent(defUrl + "default.shtml"));
            } else {
                doc = Jsoup.parse(getContent(defUrl + "default_" + i + ".shtml"));
            }

            Element urlList = doc.selectFirst("div.rebox_news");
            Elements allLi = urlList.select("li");
            List<ForeignNews> needSave = new ArrayList<>();
            for (Element li : allLi) {
                try {
                    Element a = li.selectFirst("a");
                    String title = a.text();

                    String time;
                    Matcher m = timeP.matcher(li.text());
                    if (m.find()) {
                        time = m.group(0);
                    } else {
                        time = "";
                    }
                    String href = a.attr("href");
                    String url = defUrl + href.substring(2, href.length());

                    StringBuilder builder = new StringBuilder();
                    Document foreignNew = Jsoup.parse(getContent(url));
                    Element content = foreignNew.selectFirst("div#News_Body_Txt_A");
                    Elements allP = content.select("p");
                    for (Element p : allP) {
                        String text = p.text();
                        if (StringUtils.isEmpty(text)) {
                            continue;
                        } else {
                            builder.append(text.replace("　", ""));
                        }
                    }

                    ForeignNews news = new ForeignNews();
                    if (builder.length() != 0) {
                        news.setContent(builder.toString());
                    } else {
                        news.setContent(title);
                    }
                    news.setTime(time);
                    news.setTitle(title);
                    news.setUrl(url);
                    if ("dhdw_673027".equals(key)) {
                        news.setType("发言人表态和电话答问");
                    } else if ("jzhsl_673025".equals(key)) {
                        news.setType("例行记者会");
                    }

                    if (news.getContent().indexOf("问：") > 0) {
                        String context = news.getContent().substring(0, news.getContent().indexOf("问："));
                        if (StringUtils.isNotEmpty(context)) {
                            news.setContext(context);
                        }
                    }
                    Optional<ForeignNews> f = foreignNewsRepository.findById(news.getTitle());
                    if (!f.isPresent()) {
                        needSave.add(news);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            foreignNewsRepository.saveAll(needSave);
        }
        System.out.println(key + " end");
    }

    public void getForeignNews1(String key, int start, int end) throws Exception {
        //wjbzhd wjbxw_673019
        String defUrl = "https://www.fmprc.gov.cn/web/" + key + "/";
        for (int i = start; i < end; i++) {
            Document doc;
            if (i == 0) {
                doc = Jsoup.parse(getContent(defUrl + "default.shtml"));
            } else {
                doc = Jsoup.parse(getContent(defUrl + "default_" + i + ".shtml"));
            }
            Elements imboxs = doc.select("div.imbox_ul");
            for (Element urlList : imboxs) {
                Elements allLi = urlList.select("li");
                List<ForeignNews> needSave = new ArrayList<>();
                for (Element li : allLi) {
                    try {

                        Element a = li.selectFirst("a");
                        String title = a.text();

                        String time;
                        Matcher m = timeP.matcher(li.text());
                        if (m.find()) {
                            time = m.group(0);
                        } else {
                            time = "";
                        }
                        String href = a.attr("href");
                        String url = defUrl + href.substring(2, href.length());

                        StringBuilder builder = new StringBuilder();
                        Document foreignNew = Jsoup.parse(getContent(url));
                        Element content = foreignNew.selectFirst("div#News_Body_Txt_A");
                        Elements allP = content.select("p");
                        for (Element p : allP) {
                            String text = p.text();
                            if (StringUtils.isEmpty(text)) {
                                continue;
                            } else {
                                builder.append(text.replace("　", ""));
                            }
                        }

                        ForeignNews news = new ForeignNews();
                        if (builder.length() != 0) {
                            news.setContent(builder.toString());
                        } else {
                            news.setContent(title);
                        }
                        news.setTime(time);
                        news.setTitle(title);
                        news.setUrl(url);

                        if ("wjbzhd".equals(key)) {
                            news.setType("外交部长活动");
                        } else if ("wjbxw_673019".equals(key)) {
                            news.setType("外交部新闻");
                        } else if ("zyxw".equals(key)) {
                            news.setType("重要新闻");
                        }

                        Optional<ForeignNews> f = foreignNewsRepository.findById(news.getTitle());
                        if (!f.isPresent()) {
                            needSave.add(news);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
                foreignNewsRepository.saveAll(needSave);
            }
        }
        System.out.println(key + " end");
    }

    public void getVegetable(int start, int end) throws Exception {
        String baseUrl = "http://www.bjtzh.gov.cn";
        Document first = Jsoup.connect("http://www.bjtzh.gov.cn/n125/n4349/n4400/n4416/index.html").timeout(10000).get();
        Element e = first.selectFirst("span#comp_2389617");
        Elements index = e.select("a");
        for (Element indexCatalog : index) {
            String secUrl = indexCatalog.attr("href").substring(11);
            String title = indexCatalog.attr("title");

            System.out.println("-----");
        }


        String url = "http://www.bjtzh.gov.cn/n125/n4349/n4400/n4416/index_2389617_%d.html";
        for (; end > start; end--) {
            Document doc = Jsoup.connect(String.format(url, end)).timeout(10000).get();
            Elements all = doc.select("a");
            for (Element catalog : all) {
                System.out.println(catalog.attr("href"));
                System.out.println(catalog.attr("title"));
                System.out.println("-----");
            }
        }
    }

    private void getTodayVegetable(String secUrl, File file) throws Exception {
        try (FileWriter writer = new FileWriter(file)) {
            String baseUrl = "http://www.bjtzh.gov.cn";
            Document doc = Jsoup.connect(baseUrl + secUrl).timeout(10000).get();
            Element table = doc.selectFirst("td#Icontent").selectFirst("table").selectFirst("tbody");
            Elements row = table.select("tr");
            for (Element r : row) {
                r.select("td");
            }
        }


    }

    public void cramLJ(String url, String type) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(new BasicHeader("user-agent", "Mozilla/5.0 (compatible; varysSpider/1.0; +http://www.baidu.com/search/spider.html)"));
        try {
            CloseableHttpResponse response = this.client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response != null) {
                Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                        "HTTP entity too large to be buffered in memory");
                Charset charset = getCharset(entity);
                int i = 0;
                try (final InputStream instream = entity.getContent();
                     final Reader reader = new InputStreamReader(instream, charset);
                     BufferedReader bf = new BufferedReader(reader)) {
                    LianJiaJob job = null;
                    String str;
                    while ((str = bf.readLine()) != null) {
                        if ("<url>".equals(str)) {
                            job = new LianJiaJob();
                            job.setType(type);
                            job.setCramFlag(false);
                        } else if (str.startsWith("<loc>")) {
                            job.setUrl(str.replace("<loc>", "").replace("</loc>", ""));
                        } else if (str.startsWith("<lastmod>")) {
                            job.setLastmod(str.replace("<lastmod>", "").replace("</lastmod>", ""));
                        } else if ("</url>".equals(str)) {
                            if (!lianJiaJobRepository.existsByUrl(job.getUrl())) {
                                lianJiaJobRepository.saveAndFlush(job);
                            }
                        }
                        i++;
                        if (i % 100 == 0) {
                            log.info("爬取job{}个", i);
                            Thread.sleep(300);
                        }
                    }

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

}
