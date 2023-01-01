package cn.liyu.auth.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.liyu.auth.constant.LoginCodeEnum;
import cn.liyu.auth.config.LoginCodeProperties;
import cn.liyu.auth.mapper.SysUserMapper;
import cn.liyu.auth.model.form.AuthUserForm;
import cn.liyu.auth.model.vo.CaptchaVo;
import cn.liyu.auth.model.vo.LoginVo;
import cn.liyu.auth.utils.CaptchaUtil;
import cn.liyu.security.config.SecurityProperties;
import cn.liyu.security.model.JwtUser;
import cn.liyu.security.security.TokenProvider;
import com.alibaba.fastjson.JSON;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

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
    private SecurityProperties properties;

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
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        //保存到在线用户中
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(ONLINE_TOKEN_KEY + uuid, token, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(ONLINE_USER_KEY + uuid, JSON.toJSONString(jwtUser), properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);

        LoginVo loginVo = new LoginVo();
        loginVo.setJwtUser(jwtUser);
        loginVo.setToken(token);
        return loginVo;
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
        redisTemplate.delete(ONLINE_TOKEN_KEY + tokenProvider.getToken(request));
    }
}
