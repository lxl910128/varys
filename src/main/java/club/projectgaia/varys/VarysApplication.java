package club.projectgaia.varys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import club.projectgaia.varys.service.SpriderHandler;


@SpringBootApplication
@EnableJpaAuditing
public class VarysApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(VarysApplication.class);

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

        // -----v3
        handler.getAVIndex("https://www.javbus.com/page/%s", 1, 10, "骑兵");
        handler.getAVIndex("https://www.javbus.com/uncensored/page/%s", 1, 10, "步兵");
        handler.createJobByAvatar();
        handler.getAVDetailInfo();

        // ----fix
        //handler.createJobByAvatar();

    }
}
