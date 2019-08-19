package club.projectgaia.varys.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import club.projectgaia.varys.domain.po.AVInfo;

/**
 * Created by magneto on 2018/8/12.
 */
public interface AVInfoRepository extends JpaRepository<AVInfo, Long> {
    Boolean existsAVInfoByAvId(String avId);

    Page<AVInfo> findByIssueDateIsNull(Pageable pageable);
}
