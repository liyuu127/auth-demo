package cn.liyu.auth.utils;

import cn.liyu.auth.constant.LoginCodeEnum;
import cn.liyu.auth.config.LoginCodeProperties;
import cn.liyu.base.global.ApplicationException;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Objects;

import static cn.liyu.auth.constant.AuthStubInfo.CAPTCHA_CONFIG_ERROR;

public class CaptchaUtil {

    /**
     * 获取验证码
     *
     */
    public static Captcha getCaptcha(LoginCodeProperties loginCodeProperties) {
        if (Objects.isNull(loginCodeProperties)) {
            loginCodeProperties = new LoginCodeProperties();
            if (Objects.isNull(loginCodeProperties.getCodeType())) {
                loginCodeProperties.setCodeType(LoginCodeEnum.ARITHMETIC);
            }
        }
        return switchCaptcha(loginCodeProperties);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCodeProperties 验证码配置信息
     * @return /
     */
    private static Captcha switchCaptcha(LoginCodeProperties loginCodeProperties) {
        Captcha captcha;
        switch (loginCodeProperties.getCodeType()) {
            case ARITHMETIC:
                // 算术类型 https://gitee.com/whvse/EasyCaptcha
                captcha = new FixedArithmeticCaptcha(loginCodeProperties.getWidth(), loginCodeProperties.getHeight());
                // 几位数运算，默认是两位
                captcha.setLen(loginCodeProperties.getLength());
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(loginCodeProperties.getWidth(), loginCodeProperties.getHeight());
                captcha.setLen(loginCodeProperties.getLength());
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(loginCodeProperties.getWidth(), loginCodeProperties.getHeight());
                captcha.setLen(loginCodeProperties.getLength());
                break;
            case GIF:
                captcha = new GifCaptcha(loginCodeProperties.getWidth(), loginCodeProperties.getHeight());
                captcha.setLen(loginCodeProperties.getLength());
                break;
            case SPEC:
                captcha = new SpecCaptcha(loginCodeProperties.getWidth(), loginCodeProperties.getHeight());
                captcha.setLen(loginCodeProperties.getLength());
                break;
            default:
                throw new ApplicationException(CAPTCHA_CONFIG_ERROR);
        }
        if (StringUtils.isNotBlank(loginCodeProperties.getFontName())) {
            captcha.setFont(new Font(loginCodeProperties.getFontName(), Font.PLAIN, loginCodeProperties.getFontSize()));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}
