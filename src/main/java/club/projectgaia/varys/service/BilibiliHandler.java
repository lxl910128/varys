package club.projectgaia.varys.service;

import club.projectgaia.varys.domain.dto.BiliRsp;
import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.dto.TypeEnum;
import club.projectgaia.varys.domain.po.BiliRank;
import club.projectgaia.varys.repository.BiliRankRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Phoenix Luo
 * @version 2020/5/31
 **/
@Component
@Slf4j
public class BilibiliHandler {

    @Autowired
    private BiliRankRepository biliRank;


    // 3 3日榜 ；7 7日榜 ；30 月榜
    public static String[] DAY = {"3", "7", "30"};


    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;

    private static String video_stat = "https://api.bilibili.com/x/web-interface/archive/stat?aid=%s";

    private static String url = "https://api.bilibili.com/x/web-interface/ranking?rid=%s&day=%s&type=%s&arc_type=0";

    public void listRank(RidEnum rid, String day, TypeEnum type) {
        log.info("开始爬取任务：rid:{},day:{},type:{}", rid.getName(), day, type.getName());
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
            List<BiliRank> rets = rep2PO(ret, rid, day, type);
            for (BiliRank r : rets) {
                log.info(r.getTitle());
                biliRank.save(r);
            }
            log.info("爬取榜单结束！");

        } catch (Exception e) {
            log.warn("爬取B站rid:{},day:{},type:{}错误!", rid.getName(), day, type.getName(), e);
        }

    }

    private String getReferer(RidEnum rid, String day, TypeEnum type) {
        switch (type) {
            case ALL:
                return String.format("https://www.bilibili.com/ranking/all/%s/0/%s", rid.getValue(), day);
            case ORIGIN:
                return String.format("https://www.bilibili.com/ranking/origin/%s/0/%s", rid.getValue(), day);
            case ROOKIE:
                return String.format("https://www.bilibili.com/ranking/rookie/%s/0/%s", rid.getValue(), day);
            default:
                return "https://www.bilibili.com";
        }

    }

    private List<BiliRank> rep2PO(BiliRsp ret, RidEnum rid, String day, TypeEnum type) {
        return ret.getData().getList().stream().map(rsp -> {
            BiliRank rank = new BiliRank();
            BeanUtils.copyProperties(rsp, rank);
            rank.setRankType(type);
            rank.setRid(rid);
            rank.setUpdateFeq(day);
            return rank;
        }).collect(Collectors.toList());

    }

}
