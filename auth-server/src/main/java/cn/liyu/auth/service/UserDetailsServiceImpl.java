/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.liyu.auth.service;

import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.model.Authority;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.model.UserLogin;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static cn.liyu.auth.constant.AuthStubInfo.ACCOUNT_DISABLE;
import static cn.liyu.auth.constant.AuthConstant.USER_ENABLE_FALSE;
import static cn.liyu.security.constant.SecurityConstant.USER_CACHE_KEY;


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
