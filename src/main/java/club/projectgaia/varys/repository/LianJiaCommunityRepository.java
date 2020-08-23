package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.ForeignNews;
import club.projectgaia.varys.domain.po.LianJiaCommunity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LianJiaCommunityRepository extends JpaRepository<LianJiaCommunity, Long> {
    Boolean existsByCid(String cid);
}
