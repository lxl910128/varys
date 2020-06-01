package club.projectgaia.varys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Phoenix Luo
 * @version 2020/6/1
 **/
@AllArgsConstructor
@Getter
public enum RidEnum {
    ALL("全站", "0"),
    SCI("科技", "36"),
    LIFE("生活", "160");

    private String name;
    private String value;
}
