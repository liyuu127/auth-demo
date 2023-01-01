package cn.liyu.auth.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysDept implements Serializable {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：0顶级，1公司，2部门，3职位
     */
    private Integer type;

    /**
     * 父级id
     */
    private Integer pid;

    /**
     * 排序优先级
     */
    private Integer sort;

    /**
     * 层级结构路径1|2|5|12|18
     */
    private String path;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;

    private Byte deleted;

    private static final long serialVersionUID = 1L;
}