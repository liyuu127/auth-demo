package cn.liyu.auth.model.form;

import cn.liyu.base.validated.Add;
import cn.liyu.base.validated.Delete;
import cn.liyu.base.validated.Update;
import cn.liyu.base.validated.ValidList;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class RoleForm {
    /**
     * 角色表主键
     */
    @NotNull(message = "id can not bu null", groups = {Update.class, Delete.class})
    private Integer id;

    /**
     * 角色名称
     */
    @NotBlank(message = "name can not bu null", groups = {Add.class})
    private String name;

    /**
     * 角色级别，能否看到下级组织人员数据
     */
    private Integer dataScope;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 菜单
     */
    private ValidList<Integer> menuIdList;


}