package cn.liyu.auth.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class SysUserRole implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer roleId;

    private static final long serialVersionUID = 1L;
}