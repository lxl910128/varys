package club.projectgaia.varys;

import club.projectgaia.varys.domain.po.*;
import club.projectgaia.varys.repository.*;


import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VarysApplicationTests {
    @Autowired
    NewsDailyRepository newsDailyRepository;

    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;


    @Autowired
    private AVInfoRepository avInfoRepositoryDAO;
    @Autowired
    private AVJobRepository avJobRepositoryDAO;
    @Autowired
    private AvatarInfoRepository avatarInfoDAO;

    @Test
    public void testIndex() throws Exception {
        File in = new File("/home/magneto/workspace/varys/tmp");
        Document doc = Jsoup.parse(in, "UTF-8");
        Elements info = doc.selectFirst("div.col-md-3").select("p");
        info.forEach(x -> {
            if (x.text().contains("發行日期")) {
                System.out.println(x.text());
            }
        });
    }

    @Test
    public void test() throws Exception {
        //File in = new File("H:\\text.javbus");
        File in = new File("H:\\javbus_test.mp");
        AVInfo infoPO = new AVInfo();
        Document doc = Jsoup.parse(in, "UTF-8");
        // head
        Element head = doc.selectFirst("head");
        String title = head.selectFirst("title").text().replace(" - JavBus", "");
        infoPO.setTitle(title);
        infoPO.setKeyword(head.select("meta[name=keywords]").attr("content"));
        Elements info = doc.selectFirst("div.col-md-3").select("p");
        info.forEach(x -> {
            if (x.text().contains("識別碼")) {
                infoPO.setAvId(x.select("span").get(1).text());
                return;
            }
        });
        infoPO.setPic(doc.selectFirst("a.bigImage").attr("href"));
        Element avatar = doc.selectFirst("div#avatar-waterfall");
        if (avatar != null) {
            AvatarInfo avatarInfo = new AvatarInfo();

            avatarInfo.setUrl(avatar.selectFirst("a.avatar-box").attr("href"));
            avatarInfo.setPic(avatar.selectFirst("img").attr("src"));
            avatarInfo.setName(avatar.selectFirst("span").text());

            if (!avatarInfoDAO.existsAvatarInfoByName(avatarInfo.getName())) {
                avatarInfoDAO.save(avatarInfo);
            }

            infoPO.setAvatarName(avatarInfo.getName());
        } else {
            infoPO.setAvatarName(title.split(" ")[title.split(" ").length - 1]);
        }


        Elements samples = doc.select("div#sample-waterfall > a.sample-box");
        if (samples != null) {
            List<String> sample = new ArrayList<>();
            samples.forEach(x -> {
                sample.add(x.attr("href"));
            });
            infoPO.setSamplePics(String.join(",", sample));
        }

        Elements related = doc.select("div#related-waterfall > a.movie-box");
        if (related != null) {
            related.forEach(x -> {
                AVJob newJob = new AVJob();
                newJob.setTitle(x.attr("title"));
                newJob.setUrl(x.attr("href"));

                if (!avJobRepositoryDAO.existsAVJobByUrl(newJob.getUrl())) {
                    avJobRepositoryDAO.save(newJob);
                }
            });
        }

        if (!avInfoRepositoryDAO.existsAVInfoByAvId(infoPO.getAvId())) {
            avInfoRepositoryDAO.save(infoPO);
        }
    }


    @Test
    public void contextLoads() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        while (true) {
            List<NewsDaily> r = newsDailyRepository.findAllByDocIDNotNullOrderByCreateTimeDesc(pageable);
            System.out.println(r.get(0).getCreateTime().getTime());
            System.out.println(r.size());
            pageable = pageable.next();
        }
    }

}
