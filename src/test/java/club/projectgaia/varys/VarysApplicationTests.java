package club.projectgaia.varys;

import club.projectgaia.varys.domain.po.NewsContent;
import club.projectgaia.varys.domain.po.NewsDaily;
import club.projectgaia.varys.repository.NewsContentRepository;
import club.projectgaia.varys.repository.NewsDailyRepository;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VarysApplicationTests {
    @Autowired
    NewsDailyRepository newsDailyRepository;

    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;

    @Test
    public void getImage() throws Exception{
        String url = "https://kyfw.12306.cn/passport/captcha/captcha-image64?login_site=E&module=login&rand=sjrand&1543397049668&callback=jQuery191029187547825656024_1543396921406&_="+new Date().getTime();

        HttpGet get = new HttpGet(url);
        try(CloseableHttpResponse response = client.execute(get)) {
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity(), "utf-8");

                System.out.println(result);
            }
        }
    }


    @Test
    public void contextLoads() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        while (true) {
            List<NewsDaily> r = newsDailyRepository.findAllByDocIDNotNullOrderByCreateTimeDesc(pageable);

        }
    }

}
