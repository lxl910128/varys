package club.projectgaia.varys.domain.po;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by luoxiaolong on 18-8-8.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lj_community", indexes = {@Index(name = "cid", columnList = "cid", unique = true)})
@ToString
public class LianJiaCommunity {

    @Column
    private String cid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column
    private String name;

    @Column
    private Integer price;

    @Column
    private String priceDesc;

    @Column
    private String buildAge;

    @Column
    private String buildType;

    @Column
    private String propertyCost;

    @Column
    private String propertyCompany;

    @Column
    private String developers;

    @Column
    private String city;
    @Column
    private String district;
    @Column
    private String area;

    @Column
    private Double lon;

    @Column
    private Double lat;

    @Column(columnDefinition = "TEXT")
    private String qa;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;


}
