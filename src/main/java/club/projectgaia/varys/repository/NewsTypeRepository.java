package club.projectgaia.varys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.projectgaia.varys.domain.po.NewsAbstract;
import club.projectgaia.varys.domain.po.NewsType;

import java.util.List;

public interface NewsTypeRepository extends JpaRepository<NewsType, Long> {
    List<NewsType> getAllByNewsType(String newsType);
}
