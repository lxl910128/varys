package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.po.AVJob;
import club.projectgaia.varys.domain.po.AvatarInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by magneto on 2018/8/12.
 */
public interface AvatarInfoRepository extends JpaRepository<AvatarInfo, Long> {
    Boolean existsAvatarInfoByName(String name);

    List<AvatarInfo> findAllByCrawFlagIsNull(Pageable p);
}
