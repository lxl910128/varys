package club.projectgaia.varys.domain.po;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lj_deal")
@ToString
public class LianJiaDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 房屋ID
    @Column(length = 64)
    private String cid;

    @Column(length = 128)
    private String title;

    // 小区
    @Column(length = 32)
    private String community;

    // 区
    @Column(length = 32)
    private String district;

    // 社区
    @Column(length = 32)
    private String area;

    // 成交价
    @Column(length = 32)
    private String transactionPrice;

    // 挂牌价
    @Column(length = 32)
    private String listedPrice;

    // 成交周期（天）
    @Column(length = 32)
    private String transactionCycle;

    // 关注（人）
    @Column(length = 32)
    private String follow;

    // 带看(次)
    @Column(length = 32)
    private String takeLook;

    // 户型
    @Column(length = 32)
    private String houseType;

    // 楼层
    @Column(length = 32)
    private String floor;

    // 建筑面积
    @Column(length = 32)
    private String buildArea;

    // 套内面积
    @Column(length = 32)
    private String houseArea;

    // 建筑类型
    @Column(length = 32)
    private String buildType;

    // 建筑年代
    @Column(length = 32)
    private String buildAge;

    // 朝向
    @Column(length = 32)
    private String orientation;

    // 建筑结构
    @Column(length = 32)
    private String buildStructure;

    // 户型结构
    @Column(length = 32)
    private String houseStructure;

    // 供暖方式
    @Column(length = 32)
    private String heatingMode;

    // 梯户比
    @Column(length = 32)
    private String ladderHousehold;

    // 配备电梯
    @Column(length = 32)
    private String ladder;

    // 产权年限
    @Column(length = 32)
    private String propertyYear;

    // 交易权属
    @Column(length = 32)
    private String tradingRight;

    // 房屋用途
    @Column(length = 32)
    private String HouseUse;

    // 房屋年限
    @Column(length = 32)
    private String HouseLife;

    // 房权所属
    @Column(length = 32)
    private String HouseOwnership;

    // 挂牌日期
    @Column(length = 32)
    private String listingDate;

    // 交易日期
    @Column(length = 32)
    private String dealDate;

    // 历史
    @Column(columnDefinition = "text")
    private String history;

    // 标签
    @Column(length = 128)
    private String label;


    // 备注
    @Column(columnDefinition = "text")
    private String remark;
}
