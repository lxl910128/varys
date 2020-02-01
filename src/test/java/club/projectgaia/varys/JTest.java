package club.projectgaia.varys;


import club.projectgaia.varys.domain.po.LianJiaDeal;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;

import static club.projectgaia.varys.service.HttpClientManagerFactoryBean.createIgnoreVerifySSL;

public class JTest {
    @Test
    public void testHttps() throws Exception {
        PoolingHttpClientConnectionManager poolHttpcConnManager = new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
        // 最大连接数
        poolHttpcConnManager.setMaxTotal(10);
        // 路由基数
        poolHttpcConnManager.setDefaultMaxPerRoute(20);
        /*
         * 建议此处使用HttpClients.custom的方式来创建HttpClientBuilder，而不要使用HttpClientBuilder.create()方法来创建HttpClientBuilder
         * 从官方文档可以得出，HttpClientBuilder是非线程安全的，但是HttpClients确实Immutable的，immutable 对象不仅能够保证对象的状态不被改变，
         * 而且还可以不使用锁机制就能被其他线程共享
         */
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = createIgnoreVerifySSL();
        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager)
                //.setRetryHandler(httpRequestRetryHandler)
                .setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

                    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                        // Honor 'keep-alive' header
                        HeaderElementIterator it = new BasicHeaderElementIterator(
                                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                        while (it.hasNext()) {
                            HeaderElement he = it.nextElement();
                            String param = he.getName();
                            String value = he.getValue();
                            if (value != null && param.equalsIgnoreCase("timeout")) {
                                try {
                                    return Long.parseLong(value) * 1000;
                                } catch (NumberFormatException ignore) {
                                }
                            }
                        }
                        return 30 * 1000;
                    }
                })
                .setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost("123.149.136.241", 9999)))
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(30000)
                        .setConnectTimeout(60000)
                        .setSocketTimeout(600000)
                        .build())
                .build();
        CloseableHttpResponse response = client.execute(new HttpGet("https://www.baidu.com/"));
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }

    @Test
    public void testCJ() {
        try {
            LianJiaDeal dealHouse = new LianJiaDeal();
            Document doc = Jsoup.parse(new File("G:\\workSpace\\varys\\lianjia5.html"), "utf-8");
            Element title = doc.selectFirst("div.house-title > div.wrapper > h1");
            dealHouse.setTitle(title.text());

            Elements location = doc.select("div.deal-bread > a[href]");
            Element district = location.get(2);
            dealHouse.setDistrict(district.text().replace("二手房成交", ""));

            Element area = location.get(3);
            dealHouse.setArea(area.text().replace("二手房成交", ""));

            Element community = location.get(4);
            dealHouse.setCommunity(community.text().replace("二手房成交", ""));

            Elements price = doc.select("div.price > span.dealTotalPrice > i");
            dealHouse.setTransactionPrice(price.text());

            String dealDate = doc.selectFirst("div.house-title > div.wrapper > span").text().replace(" 成交", "");
            dealHouse.setDealDate(dealDate);
            System.out.println(dealDate);

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
                            dealHouse.setFloor(value);
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
            System.out.println(dealHouse.toString());
        } catch (Exception e) {

        }
    }

    @Test
    public void testSub() {
        ByteBuffer b = ByteBuffer.allocate(255);
        b = b.put("adadsf".getBytes());
        byte c = b.get();
        System.out.println(c);
    }

}
