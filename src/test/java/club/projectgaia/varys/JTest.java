package club.projectgaia.varys;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


public class JTest {

    @Test
    public void test() throws IOException {
        File in = new File("/home/magneto/workspace/varys/tmp");
        Document doc = Jsoup.parse(in, "UTF-8");
        Elements info = doc.selectFirst("div.col-md-3").select("p");
        info.forEach(x -> {
            if (x.text().contains("發行日期")) {
                System.out.println(x.text().replace("發行日期","").replace(":","").replace(" ",""));
            }
        });
    }

}
