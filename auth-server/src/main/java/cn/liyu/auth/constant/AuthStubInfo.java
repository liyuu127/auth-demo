package cn.liyu.auth.constant;

import cn.liyu.base.global.RetStub;

public enum AuthStubInfo implements RetStub {
    INSUFFICIENT_PERMISSIONS(10_000, "insufficient permissions"),
    CAPTCHA_NOT_FOUND_OR_EXPIRE(10_001, "captcha not found or expire"),
    CAPTCHA_ERROR(10_002, "captcha error"),
    CAPTCHA_CONFIG_ERROR(10_003, "captcha config error"),
    ACCOUNT_DISABLE(10_004, "account disable"),

    USERNAME_EXIST(10_005, "username has exist"),
    TEL_EXIST(10_006, "tel has exist"),
    ROLE_NAME_EXIST(10_007, "role name has exist"),
    ROLE_DELETE_ERROR(10_008, "role delete error with associate user"),
    DEPT_PID_NOT_FOUND(10_009, "dept parent not found"),
    DEPT_NOT_FOUND(10_010, "dept not found"),
    PARENT_NODE_NOT_FOUND(10_011, "parent node not found"),


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
