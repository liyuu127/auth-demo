package cn.liyu.auth.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

@Data
public class SysRole implements Serializable {
    /**
    * 角色表主键
    */
    private Integer id;

    /**
    * 角色名称
    */
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
    * 是否被删除：0否，1是
    */
    private Byte deleted;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysRole role = (SysRole) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}