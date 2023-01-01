package cn.liyu.auth.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysRoleMenu implements Serializable {
    private Integer id;

    /**
    * 角色id
    */
    private Integer roleId;

    /**
    * 资源id
    */
    private Integer menuId;

    private static final long serialVersionUID = 1L;
}