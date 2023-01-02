package cn.liyu.security.security;

import cn.liyu.security.model.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * 用户登录成功监听器事件
 */
@Slf4j
@Component
public class CustomerAuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        JwtUser xytSecurityUser = (JwtUser) event.getAuthentication().getPrincipal();
        log.info("用户名:{},成功登录", xytSecurityUser.getUsername());
    }

}
