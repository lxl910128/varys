package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.NewsDaily;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsDailyRepository extends JpaRepository<NewsDaily, String> {
    NewsDaily findByDocID(String docID);
    List<NewsDaily> findAllByDocIDNotNullOrderByCreateTimeDesc(Pageable pageable);
}
