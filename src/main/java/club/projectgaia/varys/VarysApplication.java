package club.projectgaia.varys;

import club.projectgaia.varys.service.LianJiaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class VarysApplication {

    @Autowired
    LianJiaHandler handler;

    public static void main(String[] args) {
        SpringApplication.run(VarysApplication.class, args);
    }

}
