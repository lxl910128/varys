package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.AVInfo;
import club.projectgaia.varys.domain.po.ForeignNews;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by magneto on 2018/8/12.
 */
public interface AVInfoRepository extends JpaRepository<AVInfo, Long> {
}
