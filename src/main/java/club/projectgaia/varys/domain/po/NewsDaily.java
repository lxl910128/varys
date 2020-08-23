package club.projectgaia.varys.domain.po;

import club.projectgaia.varys.domain.dto.NewsJobStatusEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author luoxiaolong
 */
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

    @Column
    @Enumerated(EnumType.STRING)
    private NewsJobStatusEnum status;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;
}
