package club.projectgaia.varys;


import com.alibaba.fastjson.JSON;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.awt.print.Pageable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JTest {
    @Test
    public void getHtml() throws Exception {
        String defUrl = "http://www.fmprc.gov.cn/web/fyrbt_673021/jzhsl_673025/";
        Document doc = Jsoup.connect(defUrl + "default.shtml").get();
        Element urlList = doc.selectFirst("div.rebox_news");
        Elements allA = urlList.select("a");

        allA.forEach(x -> {
            System.out.println(x.text());
            String url = defUrl+x.attr("href").substring(2,)
            System.out.println(x.attr("href"));
        });


    }
}
