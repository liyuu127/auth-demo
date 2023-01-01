
package cn.liyu.security.utils;

import cn.liyu.base.global.ApplicationException;
import cn.liyu.base.global.SysStubInfo;
import cn.liyu.security.model.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

/**
 * 获取当前登录的用户
 */
@Slf4j
public class SecurityUtils {

    /**
     * 获取当前登录的用户
     *
     * @return UserDetails
     */
    public static JwtUser getCurrentUser() {
        Object principal = getAuthentication().getPrincipal();
        if (principal == null) {
            return null;
        }
        if (principal instanceof JwtUser) {
            return (JwtUser) principal;
        }
        throw new ApplicationException(SysStubInfo.LOGIN_EXPIRE);
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        JwtUser currentUser = getCurrentUser();
        return currentUser.getUsername();
    }

    /**
     * 获取系统用户ID
     *
     * @return 系统用户ID
     */
    public static Integer getCurrentUserId() {
        JwtUser userDetails = getCurrentUser();
        return userDetails.getUser().getId();
    }

    /**
     * 获取当前用户的数据权限
     *
     * @return /
     */
    public static Set<Integer> getCurrentUserDataScope() {
        JwtUser userDetails = getCurrentUser();
        return userDetails.getDataScopes();
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
