package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.ForeignNews;
import club.projectgaia.varys.domain.po.NewsAbstract;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by magneto on 2018/8/12.
 */
public interface ForeignNewsRepository extends JpaRepository<ForeignNews, String> {
}
