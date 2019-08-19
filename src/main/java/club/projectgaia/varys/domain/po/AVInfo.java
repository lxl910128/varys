package club.projectgaia.varys.domain.po;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "av_info",
        indexes = @Index(name = "avId", columnList = "avId"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"avId"}))
public class AVInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256)
    private String title;

    @Column
    private String keyword;

    @Column(length = 64)
    private String avId;

    @Column(length = 64)
    private String issueDate;

    @Column(length = 128)
    private String pic;

    @Column
    private String avatarName;


    @Column(columnDefinition = "TEXT")
    private String samplePics;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createTime;


}
