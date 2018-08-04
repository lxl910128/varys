package club.projectgaia.varys.domain.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by luoxiaolong on 18-7-31.
 */
@Data
@Entity
public class NewsType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1024)
    private String title;

    private String nid;

    private Integer count;

    private String newsType;
}
