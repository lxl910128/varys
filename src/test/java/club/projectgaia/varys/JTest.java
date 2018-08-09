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
        Document doc = Jsoup.connect("http://www.xinhuanet.com/2018-08/06/c_1123231404.htm").get();
        Pattern p = Pattern.compile("来源：(.*)</span>");
        Matcher m =p.matcher(doc.outerHtml());
        if (m.find()){
            System.out.println(m.group(1));
        }

    }
}
