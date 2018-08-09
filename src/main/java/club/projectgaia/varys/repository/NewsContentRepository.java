package club.projectgaia.varys.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import club.projectgaia.varys.domain.po.NewsContent;
import club.projectgaia.varys.domain.po.NewsDaily;

/**
 * Created by luoxiaolong on 18-8-7.
 */
public interface NewsContentRepository extends JpaRepository<NewsContent, String> {
    NewsContent findByDocID(String docID);
}
