package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.AVInfo;
import club.projectgaia.varys.domain.po.AVJob;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by magneto on 2018/8/12.
 */
public interface AVJobRepository extends JpaRepository<AVJob, Long> {
    Boolean existsAVJobByUrl(String url);
    List<AVJob> findAllByDeleteFlagIsNull(Pageable p);
}
