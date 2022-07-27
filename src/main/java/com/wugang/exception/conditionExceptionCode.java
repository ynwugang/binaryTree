package com.wugang.exception;

public enum conditionExceptionCode {
    USER_LOGIN_NAME_EXIST("登录名已存在！"),
    LOGIN_USER_ERROR("用户名或密码错误，请重试！"),
    LOGOUT_USER_ERROR("退出异常，请重试！"),
    USER_UN_AUTH("您还未登陆，请先登录！"),
    TOKEN_IS_EXPIRE("token失效，请重新登录"),
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
