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

    @Scheduled(fixedDelay = 1296000000L)
    public void createNewJob() {
        log.info("开始创建新任务");
        handler.checkNewJob();
        log.info("创建新任务结束");

    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void createDealHouse() {
        log.info("开始爬取成交房");
        handler.createDealHouse();
        log.info("结束爬取成交房");

    }

}
