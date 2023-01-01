package cn.liyu.security.security;

import cn.liyu.base.global.ApplicationException;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

import static cn.liyu.base.global.SysStubInfo.*;

/**
 * 用户登录失败监听器事件
 */
@Component
public class CustomerAuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            //提供的凭据是错误的，用户名或者密码错误
            throw new ApplicationException(USER_NAME_OR_PASSWORD_ERROR);
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            throw new ApplicationException(PASSWORD_EXPIRE);
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            throw new ApplicationException(ACCOUNT_DENY);
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            throw new ApplicationException(ACCOUNT_EXPIRE);
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            throw new ApplicationException(ACCOUNT_LOCK);
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            throw new ApplicationException(AUTH_ERROR);
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            //代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            throw new ApplicationException(AUTH_ERROR);
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            //其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            throw new ApplicationException(AUTH_ERROR);
        }
    }

}