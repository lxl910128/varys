package club.projectgaia.varys.domain.po;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "av_info",
        uniqueConstraints = @UniqueConstraint(columnNames = {"avId"}))
public class AVInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128)
    private String title;

    @Column
    private String keyword;

    @Column(length = 64)
    private String avId;

    @Column(length = 128)
    private String pic;

    @Column(length = 64)
    private String avatarName;


    @Column(columnDefinition = "TEXT")
    private String samplePics;


}
