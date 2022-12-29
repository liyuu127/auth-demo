package cn.liyu.base.global;

public enum SysStubInfo implements RetStub {
    DEFAULT_SUCCESS(200, "success"),
    PARAMETER_IS_NULL(400, "parameter is null "),
    PARAMETER_TYPE_INVALID(401, "parameter type error"),
    REQUEST_BODY_INVALID(402, "request body invalid"),
    METHOD_INVALID(403, "unsupported method"),
    RESOURCE_INVALID(404, "source not exist"),
    CONTENT_TYPE_INVALID(405, "Content-Type invalid"),
    NEED_LOGIN(406, "need login"),
    API_AUTH_FAIL(407, "API auth fail"),
    API_REQUEST_ERROR(408, "API request error"),
    API_APPID_BLANK(409, "appId is blank"),
    API_SIGN_BLANK(410, "sign is blank"),
    API_APPID_ILLEGALITY(411, "appId is illegality"),
    API_REQUEST_CONTENT_TYPE_ERROR(412, "API request content-type error"),
    AUTHORIZATION_DENIED(413, "user un authorization"),
    USER_NOT_FOUND(414, "user not found"),
    SIGN_ERROR(415, "sign error"),
    LOGIN_EXPIRE(416, "login expire"),
    DEFAULT_FAIL(500, "system is busy"),
    ACQUIRE_REDIS_LOCK_ERROR(501, "acquire_redis_lock_error"),
    ACQUIRE_REDIS_UNLOCK_ERROR(502, "acquire_redis_unlock_error"),
    MYBATIS_QUERY_ERROR(600, "MyBatisSystemException"),
    ;
    private int code;
    private String msg;

    SysStubInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
