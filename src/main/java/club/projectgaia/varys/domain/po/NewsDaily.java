package club.projectgaia.varys.domain.po;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class NewsDaily {
    //标题
    @Column(length = 1024)
    private String title;
    //id
    @Id
    @Column(length = 64)
    private String docID;
    //url
    @Column(length = 1024)
    private String linkUrl;
    //time
    private String time;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;
}
