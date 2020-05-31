package club.projectgaia.varys.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Phoenix Luo
 * @version 2020/5/31
 **/
@Data
public class BiliRow {
    private String aid;
    // 视频编号
    private String bvid;
    // 作者姓名
    private String author;
    // 硬币数
    private long coins;
    // 时长
    private String duration;
    // 作者ID
    private long mid;
    // 视频封面
    private String pic;
    private long cid;
    // 播放次数
    private long play;
    // 评分
    private long pts;
    // 标题
    private String title;
    private String trend;
    // 弹幕数
    private int video_review;
    private BiliRights rights;
    private List<BiliRow> others;
}
