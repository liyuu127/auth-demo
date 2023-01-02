package cn.liyu.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenuTreeVo {
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

    private String path;

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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuTreeVo> childrenList;
}