package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.NewsAbstract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsAbstractRepository extends JpaRepository<NewsAbstract, Long> {
}
