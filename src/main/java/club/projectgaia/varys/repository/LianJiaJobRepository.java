package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.ForeignNews;
import club.projectgaia.varys.domain.po.LianJiaJob;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by magneto on 2018/8/12.
 */
public interface LianJiaJobRepository extends JpaRepository<LianJiaJob, String> {
    Boolean existsByUrl(String url);
}
