
package cn.liyu.auth.service;

import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.model.Authority;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.model.UserLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static cn.liyu.auth.constant.AuthConstant.USER_ENABLE_FALSE;
import static cn.liyu.auth.constant.AuthStubInfo.ACCOUNT_DISABLE;


@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final SysUserService userService;
    private final SysMenuService menuService;
    private final SysDeptService deptService;

    @Override
    public JwtUser loadUserByUsername(String username) {
        UserLogin user = userService.getLoginUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            if (ObjectUtils.equals(USER_ENABLE_FALSE, user.getEnabled())) {
                throw new ApplicationException(ACCOUNT_DISABLE);
            }
        }
        JwtUser jwtUser = new JwtUser(
                user,
                deptService.getUserDeptId(user.getId()),
                menuService.getUserPermission(user.getId()).stream().map(Authority::new).collect(Collectors.toList())
        );
        return jwtUser;

    }
}
