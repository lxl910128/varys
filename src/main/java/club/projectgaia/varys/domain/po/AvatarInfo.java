package club.projectgaia.varys.domain.po;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "avatar",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class AvatarInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 64)
    private String name;

    @Column(length = 128)
    private String pic;

    @Column(length = 128)
    private String url;

}
