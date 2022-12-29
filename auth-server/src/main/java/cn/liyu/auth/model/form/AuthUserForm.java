package cn.liyu.auth.model.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthUserForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";
}
