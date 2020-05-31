package club.projectgaia.varys.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Phoenix Luo
 * @version 2020/5/31
 **/
@Data
public class BiliData {
    private String note;
    private List<BiliRow> list;
}
