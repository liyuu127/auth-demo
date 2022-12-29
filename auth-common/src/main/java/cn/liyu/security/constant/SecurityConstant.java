package cn.liyu.security.constant;

public class SecurityConstant {
    /**
     * Request Headers ： Authorization
     */
    public final static String HEADER = "Authorization";
    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    public final static String ONLINE_KEY = "online-token-";
    /**
     * 验证码 key
     */
    public final static String CODE_KEY = "code-key-";
    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    public final static String TOKEN_TYPE = "Bearer ";

    public static final String USER_CACHE_KEY = "USER-LOGIN-DATA";

}
