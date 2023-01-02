package cn.liyu.auth.model.form;

import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MenuForm {
    /**
     * 菜单表主键
     */
    @NotNull(message = "id can not be null", groups = {Update.class, Delete.class})
    private Integer id;

    /**
     * 菜单标题
     */
    @NotBlank(message = "title can not be null", groups = {Add.class})
    private String title;

    /**
     * 类型：0顶级菜单页，1一级菜单页
     */
    @NotNull(message = "type can not be null", groups = {Add.class})
    private Integer type;

    /**
     * 父级id
     */
    @NotNull(message = "pid can not be null", groups = {Add.class})
    private Integer pid;

    /**
     * 权限code
     */
    @NotBlank(message = "permission can not be null", groups = {Add.class})
    private String permission;

    /**
     * 前端组件地址
     */
    @NotBlank(message = "component can not be null", groups = {Add.class})
    private String component;

    /**
     * 展示顺序排序
     */
    private Integer sort;

    /**
     * 是否被隐藏：0否，1是
     */
    private Byte hidden;

}