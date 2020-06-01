package club.projectgaia.varys;

import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.po.NewsContent;
import club.projectgaia.varys.domain.po.NewsDaily;
import club.projectgaia.varys.repository.NewsContentRepository;
import club.projectgaia.varys.repository.NewsDailyRepository;


import club.projectgaia.varys.schedule.ScheduledTasks;
import club.projectgaia.varys.service.BilibiliHandler;
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
    private ScheduledTasks handler;

    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;


    @Test
    public void contextLoads() throws Exception {
        handler.createNewJob();
    }

}
