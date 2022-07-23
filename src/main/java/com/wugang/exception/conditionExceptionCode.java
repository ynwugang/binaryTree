package com.wugang.exception;

public enum conditionExceptionCode {
    USER_LOGIN_NAME_EXIST("登录名已存在！"),
    LOGIN_USER_ERROR("用户名或密码错误，请重试！"),
    VOTE_REPEAT("您已点赞过");

    private String desc;

    conditionExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
