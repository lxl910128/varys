package club.projectgaia.varys;

import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.dto.TypeEnum;
import club.projectgaia.varys.service.BilibiliHandler;
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

    private static final Logger log = LoggerFactory.getLogger(VarysApplication.class);

    @Autowired
    private BilibiliHandler bilibiliHandler;

    public static void main(String[] args) {
        SpringApplication.run(VarysApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        bilibiliHandler.listRank(RidEnum.ALL, "3", TypeEnum.ALL);
    }
}
