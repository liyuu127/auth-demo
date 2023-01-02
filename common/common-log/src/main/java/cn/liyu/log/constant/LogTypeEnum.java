package cn.liyu.log.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogTypeEnum {

    NORMAL(1),
    EXCEPTION(2),
    ;
    int type;
}
