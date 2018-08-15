package club.projectgaia.varys.domain.po;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

/**
 * Created by luoxiaolong on 18-8-8.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ForeignNews {

    private String url;
    @Id
    @Column(length = 128)
    private String title;

    private String time;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String type;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;


}
