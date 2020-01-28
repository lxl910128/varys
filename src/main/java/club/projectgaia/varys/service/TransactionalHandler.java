package club.projectgaia.varys.service;

import club.projectgaia.varys.domain.po.LianJiaCommunity;
import club.projectgaia.varys.domain.po.LianJiaJob;
import club.projectgaia.varys.repository.LianJiaCommunityRepository;
import club.projectgaia.varys.repository.LianJiaJobRepository;
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

    @Transactional(rollbackFor = Exception.class)
    public void saveAllCommunity(List<LianJiaJob> jobList, List<LianJiaCommunity> communityList) {
        lianJiaCommunity.saveAll(communityList);
        lianJiaJobRepository.saveAll(jobList);
    }
}
