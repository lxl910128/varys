package club.projectgaia.varys.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Phoenix Luo
 * @version 2020/6/1
 **/
@AllArgsConstructor
@Getter
public enum TypeEnum {
    ALL("全站榜", "0"),
    ORIGIN("原创榜", "2"),
    ROOKIE("新人榜", "3");

    private String name;
    private String value;
}
