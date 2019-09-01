package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.LianJiaJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by magneto on 2018/8/12.
 */
public interface LianJiaJobRepository extends JpaRepository<LianJiaJob, Long> {
    Boolean existsByUrl(String url);

    List<LianJiaJob> getAllByCramFlagFalse(Pageable pageable);

}
