package club.projectgaia.varys.schedule;

import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.dto.TypeEnum;
import club.projectgaia.varys.service.BilibiliHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ScheduledTasks {
    @Autowired
    private BilibiliHandler handler;

    @Scheduled(cron = "0 0 18 * * ?")
    public void createNewJob() {
        log.info("开始bili新任务");
        String[] feqs = {"3", "7", "30"};
        for (RidEnum ridEnum : RidEnum.values()) {
            for (TypeEnum typeEnum : TypeEnum.values()) {
                for (String feq : feqs) {
                    handler.listRank(ridEnum, feq, typeEnum);
                    // 爬完一个榜单休息60秒
                    try {
                        Thread.sleep(30000);
                    } catch (Exception e) {
                        log.warn("任务失败", e);
                    }
                }
            }
        }
        log.info("bili新任务结束");
    }

}
