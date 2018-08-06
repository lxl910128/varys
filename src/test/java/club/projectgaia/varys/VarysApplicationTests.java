package club.projectgaia.varys;

import club.projectgaia.varys.repository.NewsAbstractRepository;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VarysApplicationTests {
    @Autowired
    NewsAbstractRepository newsAbstractRepository;

    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;


    @Test
    public void contextLoads() throws Exception{
        String url = "http://qc.wa.news.cn/nodeart/list?nid=11183562&pgnum=1&cnt=10";
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "*/*");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Host", "qc.wa.news.cn");
        get.setHeader("Referer", "http://www.xinhuanet.com");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");

        CloseableHttpResponse response = client.execute(get);

        String result = EntityUtils.toString(response.getEntity());
        System.out.printf(result.substring(1, result.length() - 1));
        JSONObject A = JSON.parseObject(result.substring(1, result.length() - 1));


    }

}
