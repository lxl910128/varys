package club.projectgaia.varys;

import club.projectgaia.varys.service.SpriderHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class VarysApplication implements CommandLineRunner {

    @Autowired
    SpriderHandler handler;

    public static void main(String[] args) {
        SpringApplication.run(VarysApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //world yuqing world news fortune topic sports health english energy mil science finance
        //legal comments overseas gov gundong reform
        //handler.getByType("reform");
        //handler.getAll();
        //------ v1
        //this.handler.getWhxw();
        //this.handler.saveContent();
        //------ v2
       /* handler.getForeignNews("jzhsl_673025", 0, 1);
        handler.getForeignNews("dhdw_673027", 0, 1);
        handler.getForeignNews1("wjbzhd", 0, 1);
        handler.getForeignNews1("wjbxw_673019", 0, 1);
        handler.getForeignNews1("zyxw", 0, 1);*/

        //handler.cramLJ("https://bj.lianjia.com/sitemap/bj_xq1.xml", "小区");

       /* for (int i = 1; i <= 4; i++) {
            handler.cramLJByFile(String.format("/Users/luoxiaolong/data/lianjia/bj_esf%d.xml", i), "二手房");
        }
        for (int i = 1; i <= 22; i++) {
            handler.cramLJByFile(String.format("/Users/luoxiaolong/data/lianjia/bj_cj%d.xml", i), "成交");
        }*/

        //handler.createCommunity();

        handler.createDealHouse(Long.valueOf(args[0]));

    }
}
