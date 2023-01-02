package cn.liyu.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /* 全部的数据权限 */
    ALL(1, "全部的数据权限"),

    /* 自己部门的数据权限 */
    THIS_LEVEL(2, "自己部门的数据权限"),
    ;


    private final int value;
    private final String description;

    public static DataScopeEnum find(int val) {
        for (DataScopeEnum dataScopeEnum : DataScopeEnum.values()) {
            if (dataScopeEnum.getValue() == val) {
                return dataScopeEnum;
            }
        }
        return null;
    }

}
