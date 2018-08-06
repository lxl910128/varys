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
        Document doc = Jsoup.connect("http://www.xinhuanet.com/whxw.htm").get();

        Elements hideDataById = doc.select("#hideData");
        Elements allLi = hideDataById.select(".clearfix");
        for (Element clearfixli : allLi) {
            Document clearfixliDoc = Jsoup.parse(clearfixli.toString());
            Element url = clearfixliDoc.select("a[href]").first();
            Element time  =clearfixliDoc.select("span").first();
            System.out.println(url.text());
            System.out.println(url.attributes().get("href"));
            System.out.println(time.text());
            Pattern p = Pattern.compile("20\\d{2}-\\d{2}/\\d{2}/c_(\\d+).htm");
            Matcher m = p.matcher(url.attributes().get("href"));
            if (m.find()){
                System.out.println(m.group(1));
            }
            System.out.println("=====================");

        }

    }
}
