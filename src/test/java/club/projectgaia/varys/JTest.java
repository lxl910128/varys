package club.projectgaia.varys;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import club.projectgaia.varys.domain.po.AVInfo;
import club.projectgaia.varys.domain.po.AVJob;
import club.projectgaia.varys.domain.po.AvatarInfo;


public class JTest {

    @Test
    public void test() throws IOException {
        File in = new File("H:\\index.tmp");

        Document doc = Jsoup.parse(in, "UTF-8");

        Elements waterfalls = doc.select("div#waterfall");
        Element all = waterfalls.get(1);
        if (all != null) {
            Elements newJobElement = all.select("div.item");
            newJobElement.forEach(x -> {
                Element y = x.selectFirst("a.movie-box");
                if (y != null) {
                    AVJob newJob = new AVJob();
                    newJob.setUrl(y.attr("href"));
                    newJob.setTitle(y.selectFirst("div.photo-info").text());
                }
            });
        }

        Element next = doc.selectFirst("ul.pagination");
        if (next != null) {
            Element nextP = next.selectFirst("a#next");
            if (nextP != null) {
                String nextPage = String.format("https://www.javbus.com%s", nextP.attr("href"));
                System.out.println(nextPage);
            }
        }

    }

}
