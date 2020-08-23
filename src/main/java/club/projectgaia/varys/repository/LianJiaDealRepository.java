package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.LianJiaCommunity;
import club.projectgaia.varys.domain.po.LianJiaDeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LianJiaDealRepository extends JpaRepository<LianJiaDeal, Long> {
    Boolean existsByCid(String cid);
}
