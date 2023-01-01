package cn.liyu.auth.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysMenu implements Serializable {
    /**
     * 菜单表主键
     */
    private Integer id;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 类型：0顶级菜单页，1一级菜单页
     */
    private Integer type;

    /**
     * 父级id
     */
    private Integer pid;

    /**
     * 权限code
     */
    private String permission;

    /**
     * 前端组件地址
     */
    private String component;

    /**
     * 展示顺序排序
     */
    private Integer sort;

    /**
     * 是否被隐藏：0否，1是
     */
    private Byte hidden;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 记录是否被删除：0否，1是
     */
    private Byte deleted;

    private static final long serialVersionUID = 1L;
}