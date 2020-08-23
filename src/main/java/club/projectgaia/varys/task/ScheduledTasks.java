package club.projectgaia.varys.task;

import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.dto.TypeEnum;
import club.projectgaia.varys.service.BilibiliHandler;
import club.projectgaia.varys.service.LianJiaHandler;
import club.projectgaia.varys.service.SpriderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ScheduledTasks {
    @Autowired
    private LianJiaHandler lianJiaHandler;
    @Autowired
    private BilibiliHandler bilibiliHandler;
    @Autowired
    private SpriderHandler spriderHandler;

    @Scheduled(initialDelay = 3600000L, fixedDelay = 1296000000L)
    public void createNewLianJiaJob() {
        log.info("开始创建链家新任务");
        lianJiaHandler.checkNewJob();
        log.info("创建链家新任务结束");

    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void createDealHouse() {
        log.info("开始爬取成交房");
        lianJiaHandler.createDealHouse();
        log.info("结束爬取成交房");

    }

    // 每间隔半小时执行一次 爬取新华社新闻
    @Scheduled(fixedDelay = 1800000L)
    public void getNews() {
        log.info("检查是否有新的新华社新闻");
        try {
            spriderHandler.getWhxw();
        } catch (Exception e) {
            log.warn("获取新的新闻任务失败！", e);
        }

        log.info("读取新闻内容");
        spriderHandler.saveContent();
        log.info("新华社每小时新闻任务结束");
    }

    // 每天10点爬取外交部新闻
    @Scheduled(cron = "0 0 10 * * ?")
    public void getForeignNews() {
        log.info("开始获取外交部新闻");
        spriderHandler.getForeignNews("jzhsl_673025", 0, 1);
        spriderHandler.getForeignNews("dhdw_673027", 0, 1);
        spriderHandler.getForeignNews1("wjbzhd", 0, 1);
        spriderHandler.getForeignNews1("wjbxw_673019", 0, 1);
        spriderHandler.getForeignNews1("zyxw", 0, 1);
        log.info("获取外交部新闻结束");
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void createNewBiliJob() {
        log.info("开始bili新任务");
        String[] feqs = {"3", "7", "30"};
        for (RidEnum ridEnum : RidEnum.values()) {
            for (TypeEnum typeEnum : TypeEnum.values()) {
                for (String feq : feqs) {
                    bilibiliHandler.listRank(ridEnum, feq, typeEnum);
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
