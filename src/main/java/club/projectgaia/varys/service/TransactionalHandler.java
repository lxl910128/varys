package club.projectgaia.varys.service;

import club.projectgaia.varys.domain.po.LianJiaCommunity;
import club.projectgaia.varys.domain.po.LianJiaDeal;
import club.projectgaia.varys.domain.po.LianJiaJob;
import club.projectgaia.varys.domain.po.NewsContent;
import club.projectgaia.varys.domain.po.NewsDaily;
import club.projectgaia.varys.repository.LianJiaCommunityRepository;
import club.projectgaia.varys.repository.LianJiaDealRepository;
import club.projectgaia.varys.repository.LianJiaJobRepository;
import club.projectgaia.varys.repository.NewsContentRepository;
import club.projectgaia.varys.repository.NewsDailyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Phoenix Luo
 * @version 2020/1/28
 **/
@Service
public class TransactionalHandler {
    @Autowired
    private LianJiaCommunityRepository lianJiaCommunity;

    @Autowired
    private LianJiaJobRepository lianJiaJobRepository;

    @Autowired
    private LianJiaDealRepository lianJiaDealRepository;

    @Autowired
    private NewsDailyRepository newsDailyRepository;
    @Autowired
    private NewsContentRepository newsContentRepository;

    @Transactional(rollbackFor = Exception.class)
    public void saveAllCommunity(List<LianJiaJob> jobList, List<LianJiaCommunity> communityList) {
        lianJiaCommunity.saveAll(communityList);
        lianJiaJobRepository.saveAll(jobList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveAllDeal(List<LianJiaJob> jobList, List<LianJiaDeal> dealList) {
        lianJiaDealRepository.saveAll(dealList);
        lianJiaJobRepository.saveAll(jobList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveNews(List<NewsDaily> allChange, List<NewsContent> allSave) {
        newsDailyRepository.saveAll(allChange);
        newsContentRepository.saveAll(allSave);
    }
}
