package club.projectgaia.varys;


import club.projectgaia.varys.domain.po.ForeignNews;
import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JTest {
    @Test
    public void getHtml() throws Exception {
        for (int i = 12; i <= 66; i++) {
            String defUrl = "http://www.fmprc.gov.cn/web/fyrbt_673021/jzhsl_673025/";
            Document doc = Jsoup.connect(defUrl + "default_" + i + ".shtml").get();
            Element urlList = doc.selectFirst("div.rebox_news");
            Elements allLi = urlList.select("li");
            List<ForeignNews> needSave = new ArrayList<>();
            for (Element li : allLi) {
                Element a = li.selectFirst("a");
                String title = a.text();

                String time;
                Matcher m = Pattern.compile("20[0-9]{2}-[0-9]{2}-[0-9]{2}").matcher(li.text());
                if (m.find()) {
                    time = m.group(0);
                } else {
                    time = "";
                }
                String href = a.attr("href");
                String url = defUrl + href.substring(2, href.length());

                StringBuilder builder = new StringBuilder();
                Document foreignNew = Jsoup.connect(url).get();
                Element content = foreignNew.selectFirst("div#News_Body_Txt_A");
                Elements allP = content.select("p");
                for (Element p : allP) {
                    String text = p.text();
                    if (StringUtils.isEmpty(text)) {
                        continue;
                    } else {
                        builder.append(text.replace("　",""));
                    }
                }

                ForeignNews news = new ForeignNews();
                news.setContent(builder.toString());
                news.setTime(time);
                news.setTitle(title);
                news.setUrl(url);

                String context = news.getContent().substring(0,news.getContent().indexOf("问："));
                if (StringUtils.isNotEmpty(context)){
                    news.setContext(context);
                }
                needSave.add(news);
            }
            System.out.println(i);
        }
    }

}
