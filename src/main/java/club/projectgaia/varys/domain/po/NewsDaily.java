package club.projectgaia.varys.domain.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class NewsDaily {
    //标题
    @Column(length = 1024)
    private String title;
    //id
    @Id
    private String docID;
    //url
    @Column(length = 1024)
    private String linkUrl;
    //time
    private String time;


}
