package club.projectgaia.varys;

import org.apache.commons.lang3.StringUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import club.projectgaia.varys.domain.po.AVInfo;
import club.projectgaia.varys.domain.po.AVJob;
import club.projectgaia.varys.domain.po.AvatarInfo;
import club.projectgaia.varys.domain.po.NewsDaily;
import club.projectgaia.varys.repository.AVInfoRepository;
import club.projectgaia.varys.repository.AVJobRepository;
import club.projectgaia.varys.repository.AvatarInfoRepository;
import club.projectgaia.varys.repository.NewsDailyRepository;

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
    public void test() throws Exception {
        Pattern pName = Pattern.compile("([\u4e00-\u9fa5,\u0800-\u4e00]+)");
        File in = new File("/home/magneto/workspace/varys/error.log");

        AVInfo infoPO = new AVInfo();
        Document doc = Jsoup.parse(in, "UTF-8");
        // head
        Element head = doc.selectFirst("head");
        String title = head.selectFirst("title").text().replace(" - JavBus", "");
        infoPO.setTitle(title);
        infoPO.setKeyword(head.select("meta[name=keywords]").attr("content"));
        Elements info = doc.selectFirst("div.col-md-3").select("p");
        info.forEach(x -> {
            if (infoPO.getAvId() != null && infoPO.getIssueDate() != null) {
                return;
            }
            if (x.text().contains("識別碼")) {
                infoPO.setAvId(x.select("span").get(1).text());
            }
            if (x.text().contains("發行日期")) {
                String date = x.text().replace("發行日期", "").replace(":", "").replace(" ", "");
                if (StringUtils.isNotBlank(date)) {
                    infoPO.setIssueDate(date);
                }
            }
        });

        infoPO.setPic(doc.selectFirst("a.bigImage").attr("href"));
        Element avatar = doc.selectFirst("div#avatar-waterfall");
        if (avatar != null) {
            List<String> allAvatar = new ArrayList<>();
            avatar.select("a.avatar-box").forEach(x -> {
                AvatarInfo avatarInfo = new AvatarInfo();

                avatarInfo.setUrl(x.attr("href"));
                avatarInfo.setPic(x.selectFirst("img").attr("src"));
                avatarInfo.setName(x.selectFirst("span").text());
                allAvatar.add(x.selectFirst("span").text());
                if (!avatarInfoDAO.existsAvatarInfoByName(avatarInfo.getName())) {
                    avatarInfoDAO.save(avatarInfo);
                }
            });

            infoPO.setAvatarName(String.join(",", allAvatar));
        } else {
            String[] splitName = title.split(" ");
            if (splitName.length >= 3) {
                String name = splitName[splitName.length - 1];
                Matcher matcher = pName.matcher(name);
                String str = "";
                while (matcher.find()) {
                    str += matcher.group(0);
                }
                if (str.length() >= 2 && str.length() <= 6) {
                    infoPO.setAvatarName(str);
                }
            }
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
                    avJobRepositoryDAO.saveAndFlush(newJob);
                }
            });
        }

        if (StringUtils.isNotBlank(infoPO.getAvId()) && !avInfoRepositoryDAO.existsAVInfoByAvId(infoPO.getAvId())) {
            avInfoRepositoryDAO.save(infoPO);
        }
        //log.info("完成:{}", infoPO.getAvId());
        Thread.sleep(500);
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
