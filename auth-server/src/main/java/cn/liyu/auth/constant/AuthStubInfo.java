package cn.liyu.auth.constant;

import cn.liyu.base.global.RetStub;

public enum AuthStubInfo implements RetStub {
    CAPTCHA_NOT_FOUND_OR_EXPIRE(10_001, "captcha not found or expire"),
    CAPTCHA_ERROR(10_002, "captcha error"),
    CAPTCHA_CONFIG_ERROR(10_003, "captcha config error"),

    ;
    private int code;
    private String msg;

    AuthStubInfo(int code, String msg) {
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
