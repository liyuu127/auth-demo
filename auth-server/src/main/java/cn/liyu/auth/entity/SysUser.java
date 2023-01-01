package cn.liyu.auth.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SysUser implements Serializable {
    /**
     * 用户表主键
     */
    private Integer id;

    private Integer deptId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别:0女性,1男性
     */
    private Byte gender;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像网址
     */
    private String avatarUrl;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用:0禁用,1启用
     */
    private Byte enabled;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否被删除:0否，1是
     */
    private Byte deleted;

    private static final long serialVersionUID = 1L;
}