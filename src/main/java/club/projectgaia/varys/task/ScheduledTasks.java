package club.projectgaia.varys.task;

import club.projectgaia.varys.service.LianJiaHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ScheduledTasks {
    @Autowired
    private LianJiaHandler handler;
    private static final Long NEW_JOB_TIME = 1000 * 60 * 60 * 24 * 15L;

    @Scheduled(fixedDelay = NEW_JOB_TIME)
    public void createNewJob() {
        log.info("开始创建新任务");
        handler.checkNewJob();
        log.info("创建新任务结束");

    }

    @Scheduled(cron = "0 0 23 * *")
    public void createDealHouse() {
        log.info("开始爬取成交房");
        handler.createDealHouse();
        log.info("结束爬取成交房");

    }

}
