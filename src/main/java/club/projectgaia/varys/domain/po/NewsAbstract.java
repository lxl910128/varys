package club.projectgaia.varys.domain.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class NewsAbstract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //摘要
    @Column(columnDefinition = "TEXT")
    private String Abstract;
    //作者
    private String author;
    //编辑者
    private String editor;
    //id
    private Integer docID;
    //url
    @Column(length = 1024)
    private String linkUrl;
    //发布时间
    private String pubTime;
    //来源
    private String sourceName;
    //标题
    @Column(length = 1024)
    private String title;
    //关键词
    @Column(length = 2048)
    private String keyword;
    //图片
    private String pics;
    //分类
    private String newsType;
    //是否有link
    private Integer isLink;
    //nid
    private String nid;

}
