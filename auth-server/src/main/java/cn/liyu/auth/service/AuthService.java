package cn.liyu.auth.service;

import cn.hutool.core.util.IdUtil;
import cn.liyu.auth.config.LoginCodeProperties;
import cn.liyu.auth.constant.LoginCodeEnum;
import cn.liyu.auth.mapper.SysUserMapper;
import cn.liyu.auth.model.form.AuthUserForm;
import cn.liyu.auth.model.vo.CaptchaVo;
import cn.liyu.auth.model.vo.LoginVo;
import cn.liyu.auth.utils.CaptchaUtil;
import cn.liyu.auth.utils.NetUtils;
import cn.liyu.security.config.SecurityProperties;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.model.OnlineUser;
import cn.liyu.security.security.TokenProvider;
import com.alibaba.fastjson.JSON;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.liyu.security.constant.SecurityConstant.*;

@Slf4j
@Service
public class AuthService {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private LoginCodeProperties loginCodeProperties;
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 登录
     *
     * @param userForm
     * @return
     * @throws Exception
     */
    public LoginVo login(AuthUserForm userForm) throws Exception {

//        //获取验证码
//        String captcha = redisTemplate.opsForValue().get(USER_LOGIN_CAPTCHA_KEY + userForm.getCaptchaId());
//        //验证码不存在，id错误或者过期
//        if (StringUtils.isBlank(captcha)) {
//            throw new ApplicationException(CAPTCHA_NOT_FOUND_OR_EXPIRE);
//        }
//        //验证码验证不正确
//        if (!StringUtils.equals(captcha, userForm.getCode())) {
//            throw new ApplicationException(CAPTCHA_ERROR);
//        }

        // 密码解密
//        String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, userForm.getPassword());
        String password = userForm.getPassword();
        //开始认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userForm.getUsername(), password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String token = tokenProvider.createToken(authentication);
        String token = tokenProvider.createUUIDToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        //保存到在线用户中
//        redisTemplate.opsForValue().set(ONLINE_TOKEN_KEY + uuid, token, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(ONLINE_USER_KEY + token, JSON.toJSONString(jwtUser),
                securityProperties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);

        //设置在线用户
        setOnlineUser(token, jwtUser);
        //检测是否需要踢人
        freshOnlineCount(jwtUser);

        LoginVo loginVo = new LoginVo();
        loginVo.setJwtUser(jwtUser);
        loginVo.setToken(token);
        return loginVo;
    }

    private void freshOnlineCount(JwtUser jwtUser) {
        if (securityProperties.getOnlineCount() < 1) {
            log.error("securityProperties onlineCount={}config error", securityProperties.getOnlineCount());
        } else {
            Long onlineCount = securityProperties.getOnlineCount();
            Set<String> keys = redisTemplate.keys(ONLINE_USER_TOKEN_KEY + jwtUser.getUser().getUsername() + ":" + "*");
            if (onlineCount >= keys.size()) {
                return;
            }
            List<OnlineUser> onlineUsers = keys.stream()
                    .map(key -> redisTemplate.opsForValue().get(key))
                    .filter(Objects::nonNull)
                    .filter(StringUtils::isNotBlank)
                    .map(s -> JSON.parseObject(s, OnlineUser.class))
                    .sorted(Comparator.comparing(OnlineUser::getLoginTime))
                    .collect(Collectors.toList());

            int kickOutUser = (int) (onlineUsers.size() - onlineCount);
            for (int i = 0; i < kickOutUser; i++) {
                OnlineUser onlineUser = onlineUsers.get(i);
                //删除在线用户
                redisTemplate.delete(ONLINE_USER_TOKEN_KEY + jwtUser.getUser().getUsername() + ":" + onlineUser.getKey());
                //删除token
                redisTemplate.delete(ONLINE_USER_KEY + onlineUser.getKey());
            }
        }
    }

    private void setOnlineUser(String token, JwtUser jwtUser) {
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setUserName(jwtUser.getUser().getUsername());
        onlineUser.setUserId(jwtUser.getUser().getId());
        onlineUser.setLoginTime(LocalDateTime.now());
        onlineUser.setIp(NetUtils.getIp(null));
        onlineUser.setKey(token);
        redisTemplate.opsForValue().set(ONLINE_USER_TOKEN_KEY + onlineUser.getUserName() + ":" + token, JSON.toJSONString(onlineUser), securityProperties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public CaptchaVo getCaptcha() {
        // 获取运算的结果
        Captcha captcha = CaptchaUtil.getCaptcha(loginCodeProperties);
        String uuid = CODE_KEY + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisTemplate.opsForValue().set(USER_LOGIN_CAPTCHA_KEY + uuid, captchaValue, loginCodeProperties.getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setCaptchaId(uuid);
        captchaVo.setImg(captcha.toBase64());
        return captchaVo;
    }

    public void logout(HttpServletRequest request) {
         tokenProvider.removeToken(request);
    }
}
