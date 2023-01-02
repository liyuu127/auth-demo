package cn.liyu.security.constant;

public class SecurityConstant {
    /**
     * Request Headers ： Authorization
     */
    public final static String HEADER = "Authorization";
    /**
     * 在线用户 key，根据 token 查询 jwtToken
     */
    public final static String ONLINE_TOKEN_KEY = "online:token:";
    public final static String ONLINE_USER_KEY = "online:user:";
    /**
     * 在线的用户的token，用于查询在线用户下的生效token
     */
    public final static String ONLINE_USER_TOKEN_KEY = "online:user:token:";
    /**
     * 验证码 key
     */
    public final static String CODE_KEY = "code-key-";
    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    public final static String TOKEN_TYPE = "Bearer ";

    public static final String USER_CACHE_KEY = "user:cache";

    /**
     * 图片验证码
     */
    public static final String USER_LOGIN_CAPTCHA_KEY = "captcha:user:login:";

    /**
     * 是否启用:0禁用,1启用
     */
    public static final byte USER_ENABLE_FALSE = 0;
    public static final byte USER_ENABLE_TRUE = 1;

}
