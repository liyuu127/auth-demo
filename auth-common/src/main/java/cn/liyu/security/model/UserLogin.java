package cn.liyu.security.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserLogin {

    private Long id;


    private Long deptId;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private String avatarPath;

    private String password;

    private Boolean enabled;

    private Boolean isAdmin = false;

    private Date pwdResetTime;


}
