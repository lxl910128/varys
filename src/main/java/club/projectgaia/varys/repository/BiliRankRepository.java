package club.projectgaia.varys.repository;

import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.dto.TypeEnum;
import club.projectgaia.varys.domain.po.BiliRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Phoenix Luo
 * @version 2020/6/1
 **/
public interface BiliRankRepository extends JpaRepository<BiliRank, Long> {
    Optional<BiliRank> findByRankTypeAndRidAndUpdateFeqAndCollectionDateAndBvid(TypeEnum rankType,
                                                                                RidEnum rid,
                                                                                String UpdateFeq,
                                                                                String collectionDate,
                                                                                String bvid);
}
