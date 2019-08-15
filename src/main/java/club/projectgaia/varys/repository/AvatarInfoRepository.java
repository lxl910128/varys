package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.AVJob;
import club.projectgaia.varys.domain.po.AvatarInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by magneto on 2018/8/12.
 */
public interface AvatarInfoRepository extends JpaRepository<AvatarInfo, Long> {
}
