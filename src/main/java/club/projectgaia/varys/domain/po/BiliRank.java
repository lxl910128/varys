package club.projectgaia.varys.domain.po;

import club.projectgaia.varys.domain.dto.RidEnum;
import club.projectgaia.varys.domain.dto.TypeEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Phoenix Luo
 * @version 2020/6/1
 **/
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BiliRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 榜单分类
    @Column
    @Enumerated(EnumType.STRING)
    private TypeEnum rankType;

    // 分区ID
    @Column
    @Enumerated(EnumType.STRING)
    private RidEnum rid;

    // 更新周期
    @Column
    private String updateFeq;

    @Column(length = 64)
    private String aid;
    // 视频编号
    @Column(length = 64)
    private String bvid;

    // 作者姓名
    @Column(length = 64)
    private String author;

    // 硬币数
    @Column
    private Long coins;

    // 时长
    @Column(length = 32)
    private String duration;

    // 作者ID
    @Column
    private Long mid;

    @Column
    private Long cid;
    // 播放次数
    @Column
    private Long play;
    // 评分
    @Column
    private Long pts;
    // 标题
    @Column
    private String title;
    // 弹幕数
    @Column
    private Integer video_review;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;

}
