package cn.liyu.auth.web;

import cn.liyu.auth.model.form.AuthUserForm;
import cn.liyu.auth.model.vo.CaptchaVo;
import cn.liyu.auth.model.vo.LoginVo;
import cn.liyu.auth.service.AuthService;
import cn.liyu.security.annotation.AnonymousDeleteMapping;
import cn.liyu.security.annotation.AnonymousGetMapping;
import cn.liyu.security.annotation.AnonymousPostMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthService authService;


    @AnonymousPostMapping(value = "/login")
    public LoginVo login(@Validated @RequestBody AuthUserForm userForm) throws Exception {
        return authService.login(userForm);
    }

    @AnonymousGetMapping(value = "/captcha")
    public CaptchaVo getCaptcha() {
        return authService.getCaptcha();
    }

    @AnonymousDeleteMapping(value = "/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request);
    }
}
