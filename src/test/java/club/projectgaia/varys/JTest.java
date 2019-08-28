package club.projectgaia.varys;



import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.junit.Test;

import java.awt.print.Pageable;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
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
    public void getLianjia() throws Exception {
        //创建httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建httpGet实例
        HttpGet httpGet = new HttpGet("https://bj.lianjia.com/sitemap/bj_xq1.xml");
        httpGet.setHeader(new BasicHeader("user-agent","Mozilla/5.0 (compatible; varysSpider/1.0; +http://www.baidu.com/search/spider.html)"));
        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();
        if (response != null) {
            ContentType contentType = null;
            try {
                contentType = ContentType.get(entity);
            } catch (final UnsupportedCharsetException ex) {

            }
            if (contentType != null) {
                if (contentType.getCharset() == null) {
                    contentType = contentType.withCharset("utf-8");
                }
            } else {
                contentType = ContentType.DEFAULT_TEXT.withCharset("utf-8");
            }

            final InputStream instream = entity.getContent();
            try {
                Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                        "HTTP entity too large to be buffered in memory");
                int capacity = (int) entity.getContentLength();
                if (capacity < 0) {
                    capacity = 4096;
                }
                Charset charset = null;
                if (contentType != null) {
                    charset = contentType.getCharset();
                    if (charset == null) {
                        final ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
                        charset = defaultContentType != null ? defaultContentType.getCharset() : null;
                    }
                }
                if (charset == null) {
                    charset = HTTP.DEF_CONTENT_CHARSET;
                }
                final Reader reader = new InputStreamReader(instream, charset);
                BufferedReader bf = new BufferedReader(reader);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    System.out.println(str);
                }

                bf.close();
                reader.close();

            } catch (IOException E) {

            } finally {
                instream.close();
            }

        }
        if (response != null) {
            response.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }


    @Test
    public void testSub() {
        ByteBuffer b = ByteBuffer.allocate(255);
        b = b.put("adadsf".getBytes());
        byte c = b.get();
        System.out.println(c);
    }

}
