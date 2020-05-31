package club.projectgaia.varys.service;

import club.projectgaia.varys.domain.dto.BiliRsp;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Phoenix Luo
 * @version 2020/5/31
 **/
@Component
@Slf4j
public class BilibiliHandler {
    // 0 全站  36 科技 160 生活
    public static String[] RID = {"0", "36", "160"};

    // 3 3日榜 ；7 7日榜 ；30 月榜
    public static String[] DAY = {"3", "7", "30"};

    // 1 全站榜 ；2 原创榜 ；3 新人榜
    public static String[] TYPE = {"1", "2", "3"};

    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;

    private static String video_stat = "https://api.bilibili.com/x/web-interface/archive/stat?aid=%s";

    private static String url = "https://api.bilibili.com/x/web-interface/ranking?rid=%s&day=%s&type=%s&arc_type=0";

    public void listRank(String rid, String day, String type) {
        log.info("开始爬取任务：");
        String url = String.format(BilibiliHandler.url, rid, day, type);

        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("authority", "api.bilibili.com");
        get.setHeader("Referer", getReferer(rid, day, type));
        get.setHeader("User-Agent", "Mozilla/5.0 (compatible; varysSpider/1.0; +http://www.baidu.com/search/spider.html)");


        try {
            CloseableHttpResponse response = client.execute(get);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info("爬取结果：{}", result);
            BiliRsp ret = JSON.parseObject(result, BiliRsp.class);


        } catch (Exception e) {
            log.warn("爬取B站rid:{},day:{},type:{}错误!", rid, day, type, e);
        }

    }

    private String getReferer(String rid, String day, String type) {
        switch (type) {
            case "1":
                return String.format("https://www.bilibili.com/ranking/all/%s/0/%s", rid, day);
            case "2":
                return String.format("https://www.bilibili.com/ranking/origin/%s/0/%s", rid, day);
            case "3":
                return String.format("https://www.bilibili.com/ranking/rookie/%s/0/%s", rid, day);
            default:
                return "https://www.bilibili.com";
        }

    }

}
