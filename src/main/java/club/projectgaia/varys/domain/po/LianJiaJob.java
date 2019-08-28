package club.projectgaia.varys.domain.po;

import lombok.Data;
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
@Table(name = "lj_job", indexes = {@Index(name = "url", columnList = "url", unique = true)})
public class LianJiaJob {

    @Column
    private String url;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String lastmod;

    @Column
    private String type;

    @Column
    private Boolean cramFlag;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;


}
