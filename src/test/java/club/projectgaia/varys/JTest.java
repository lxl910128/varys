package club.projectgaia.varys;


import club.projectgaia.varys.domain.po.AVInfo;
import club.projectgaia.varys.domain.po.AVJob;
import club.projectgaia.varys.domain.po.AvatarInfo;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JTest {
    @Test
    public void getHtml() throws Exception {
        //File in = new File("H:\\text.javbus");
        File in = new File("H:\\javbus_test.mp");
        Document doc = Jsoup.parse(in, "UTF-8");
        System.out.println(doc.outerHtml());
    }

    @Test
    public void testSub() throws IOException {
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
            //TODO
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
                //TODO
            });
        }

        //TODO
    }

}
