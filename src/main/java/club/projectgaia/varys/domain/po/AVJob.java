package club.projectgaia.varys.domain.po;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "av_job",
        uniqueConstraints = @UniqueConstraint(columnNames = {"url"}))
public class AVJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128)
    private String title;

    @Column(length = 128)
    private String url;
}
