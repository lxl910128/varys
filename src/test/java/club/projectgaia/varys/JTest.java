package club.projectgaia.varys;


import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
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
        String defUrl = "http://www.fmprc.gov.cn/web/fyrbt_673021/jzhsl_673025/t1519154.shtml";
        Document doc = Jsoup.connect(defUrl + "default.shtml").get();
        Element content = doc.selectFirst("div#News_Body_Txt_A");
        StringBuilder builder = new StringBuilder();
        Elements allP = content.select("p");
        for (Element p : allP) {
            String text = p.text();
            if (StringUtils.isEmpty(text)) {
                Elements allB = p.select("b");
                if (allB == null) {
                    continue;
                } else {
                    for (Element b : allB) {
                        builder.append(b.text());
                    }
                }
            } else {
                builder.append(text);
            }
        }

        System.out.println(builder.toString());


    }
}
