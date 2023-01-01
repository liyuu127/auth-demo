package cn.liyu.auth.model.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthUserForm {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    /**
     * 验证码结果
     */
//    @NotBlank
    private String code;
    /**
     * 验证码id
     */
//    @NotBlank
    private String captchaId;
}
