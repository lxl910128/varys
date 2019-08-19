package club.projectgaia.varys;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import club.projectgaia.varys.domain.po.AvatarInfo;


public class JTest {

    @Test
    public void test() throws IOException {
        File in = new File("/home/magneto/workspace/varys/tmp");
        Document doc = Jsoup.parse(in, "UTF-8");
        Element avatar = doc.selectFirst("div#avatar-waterfall");
        if (avatar != null) {
            List<String> allAvatar = new ArrayList<>();
            avatar.select("a.avatar-box").forEach(x -> {

                System.out.println(x.attr("href"));
                System.out.println(x.selectFirst("img").attr("src"));
                System.out.println(x.selectFirst("span").text());

            });

        }
    }

}
