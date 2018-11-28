package club.projectgaia.varys;


import club.projectgaia.varys.domain.po.ForeignNews;
import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.swing.*;

public class JTest {
    @Test
    public void getPic() throws IOException {
        /*String content = new String(Files.readAllBytes(Paths.get("/Users/deepclue/Desktop/pic.txt")));
        BufferedImage new_image_buffer = base64StringToImg(content);
        getSubImage(new_image_buffer,5, 41, 67, 67,"/Users/deepclue/Desktop/pic/t1.jpg");
        getSubImage(new_image_buffer,77, 41, 67, 67,"/Users/deepclue/Desktop/pic/t2.jpg");
        getSubImage(new_image_buffer,149, 41, 67, 67,"/Users/deepclue/Desktop/pic/t3.jpg");
        getSubImage(new_image_buffer,221, 41, 67, 67,"/Users/deepclue/Desktop/pic/t4.jpg");

        getSubImage(new_image_buffer,5, 113, 67, 67,"/Users/deepclue/Desktop/pic/b1.jpg");
        getSubImage(new_image_buffer,77, 113, 67, 67,"/Users/deepclue/Desktop/pic/b2.jpg");
        getSubImage(new_image_buffer,149, 113, 67, 67,"/Users/deepclue/Desktop/pic/b3.jpg");
        getSubImage(new_image_buffer,221, 113, 67, 67,"/Users/deepclue/Desktop/pic/b4.jpg");

        getSubImage(new_image_buffer,120, 0, 173, 25,"/Users/deepclue/Desktop/pic/label.jpg");

        File outputFile = new File("/Users/deepclue/Desktop/pic/pic1.jpg");


        ImageIO.write(new_image_buffer, "jpg", outputFile);
*/

        //draw_image(new_image_buffer);


    }

    public static void getSubImage(BufferedImage image, int x, int y, int w, int h, String path) throws IOException {
        ImageIO.write(image.getSubimage(x, y, w, h), "jpg", new File(path));
    }

    public static void draw_image(BufferedImage image_buffer) throws IOException {
        ImageIcon icon = new ImageIcon(image_buffer);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());


        frame.setSize(image_buffer.getWidth(), image_buffer.getHeight());
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }




    @Test
    public void getImage()throws Exception{
        String url = "https://kyfw.12306.cn/passport/captcha/captcha-image64?login_site=E&module=login&rand=sjrand&1543397049668&callback=jQuery191029187547825656024_1543396921406&_="+new Date().getTime();




    }

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
                        builder.append(text.replace("　", ""));
                    }
                }

                ForeignNews news = new ForeignNews();
                news.setContent(builder.toString());
                news.setTime(time);
                news.setTitle(title);
                news.setUrl(url);

                String context = news.getContent().substring(0, news.getContent().indexOf("问："));
                if (StringUtils.isNotEmpty(context)) {
                    news.setContext(context);
                }
                needSave.add(news);
            }
            System.out.println(i);
        }
    }

}
