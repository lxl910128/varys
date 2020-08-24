package club.projectgaia.varys;

import club.projectgaia.varys.domain.dto.NewsJobStatusEnum;
import club.projectgaia.varys.domain.po.NewsContent;
import club.projectgaia.varys.domain.po.NewsDaily;
import club.projectgaia.varys.repository.NewsContentRepository;
import club.projectgaia.varys.repository.NewsDailyRepository;
import club.projectgaia.varys.service.SpriderHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VarysApplicationTests {
    @Resource(name = "httpClientManagerFactoryBean")
    private CloseableHttpClient client;

    @Autowired
    private SpriderHandler spriderHandler;
    @Autowired
    private NewsDailyRepository newsDailyRepository;
    @Autowired
    private NewsContentRepository newsContentRepository;

    @Test
    public void testHandler() throws Exception {
       /* Pageable pageable = PageRequest.of(0, 1000, Sort.by("docID"));
        boolean flag = true;
        while (flag) {
            List<NewsDaily> change = new ArrayList<>();
            List<NewsDaily> search = newsDailyRepository.findAllByStatusIsNull(pageable);
            if (search == null || search.size() == 0) {
                break;
            }
            int init = 0;
            int save = 0;
            for (NewsDaily daily : search) {
                NewsContent content = newsContentRepository.findByDocID(daily.getDocID());
                if (content == null) {
                    daily.setStatus(NewsJobStatusEnum.init);
                    init++;
                } else {
                    daily.setStatus(NewsJobStatusEnum.saved);
                    save++;
                }
                change.add(daily);
            }
            newsDailyRepository.saveAll(change);
            System.out.println("初始化:" + init + " 保存:" + save);
        }*/
        // spriderHandler.getWhxw();
        spriderHandler.saveContent();
    }

}
