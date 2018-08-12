package club.projectgaia.varys.domain.po;

import javax.persistence.*;

import lombok.Data;

/**
 * Created by luoxiaolong on 18-8-8.
 */
@Data
@Entity
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


}
