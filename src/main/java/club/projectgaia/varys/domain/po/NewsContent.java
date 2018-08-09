package club.projectgaia.varys.domain.po;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Created by luoxiaolong on 18-8-6.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class NewsContent {
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
    //摘要
    @Column(columnDefinition = "TEXT")
    private String description;

    //来源
    @Column
    private String source;

    //正文
    @Column(columnDefinition = "TEXT")
    private String content;

    //keyWord
    @Column
    private String keyWords;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;
}
