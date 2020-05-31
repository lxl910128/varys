package club.projectgaia.varys.domain.dto;

import lombok.Data;

/**
 * @author Phoenix Luo
 * @version 2020/5/31
 **/
@Data
public class BiliRsp {
    private Integer code;
    private String message;
    private Integer ttl;
    private BiliData data;
}
