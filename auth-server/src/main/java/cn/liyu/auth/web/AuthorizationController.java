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
package cn.liyu.auth.web;

import cn.hutool.core.util.IdUtil;
import cn.liyu.auth.config.LoginProperties;
import cn.liyu.auth.config.RsaProperties;
import cn.liyu.auth.constant.LoginCodeEnum;
import cn.liyu.auth.model.form.AuthUserForm;
import cn.liyu.base.global.ApplicationException;
import cn.liyu.security.annotation.AnonymousDeleteMapping;
import cn.liyu.security.annotation.AnonymousGetMapping;
import cn.liyu.security.annotation.AnonymousPostMapping;
import cn.liyu.security.config.SecurityProperties;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.security.TokenProvider;
import cn.liyu.security.security.UserCacheManager;
import cn.liyu.security.utils.RedisUtils;
import cn.liyu.security.utils.RsaUtils;
import cn.liyu.security.utils.SecurityUtils;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.liyu.auth.constant.AuthStubInfo.CAPTCHA_ERROR;
import static cn.liyu.auth.constant.AuthStubInfo.CAPTCHA_NOT_FOUND_OR_EXPIRE;
import static cn.liyu.security.constant.SecurityConstant.CODE_KEY;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserCacheManager userCacheManager;

    @Resource
    private LoginProperties loginProperties;

    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserForm userForm, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userForm.getPassword());
        // 查询验证码
        String code = (String) redisUtils.get(userForm.getUuid());
        // 清除验证码
        redisUtils.del(userForm.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new ApplicationException(CAPTCHA_NOT_FOUND_OR_EXPIRE);
        }
        if (StringUtils.isBlank(userForm.getCode()) || !userForm.getCode().equalsIgnoreCase(code)) {
            throw new ApplicationException(CAPTCHA_ERROR);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userForm.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUserDto = (JwtUser) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(jwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", token);
            put("user", jwtUserDto);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(userForm.getUsername(), token);
        }
        return ResponseEntity.ok(authInfo);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        return ResponseEntity.ok(userCacheManager.getUserCache(SecurityUtils.getCurrentUsername()));
    }

    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = CODE_KEY + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
