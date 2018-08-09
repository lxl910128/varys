package club.projectgaia.varys;

import club.projectgaia.varys.domain.po.NewsContent;
import club.projectgaia.varys.domain.po.NewsDaily;
import club.projectgaia.varys.repository.NewsContentRepository;
import club.projectgaia.varys.repository.NewsDailyRepository;


import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void contextLoads() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        while (true) {
            List<NewsDaily> r = newsDailyRepository.findAllByDocIDNotNullOrderByCreateTimeDesc(pageable);
            System.out.println(r.get(0).getCreateTime().getTime());
            System.out.println(r.size());
            pageable = pageable.next();
        }
    }

}
