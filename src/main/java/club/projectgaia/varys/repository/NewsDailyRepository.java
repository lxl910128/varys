package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.NewsDaily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsDailyRepository extends JpaRepository<NewsDaily, String> {
    NewsDaily findByDocID(String docID);
}
