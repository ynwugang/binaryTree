package com.wugang.response;

import lombok.Data;

/**
 * 返回类
 */
@Data
public class CommonResponse<T> {
    /**
     * 业务的成功或者失败
     */
    private Boolean success;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据（泛型）
     */
    private T content;

    /**
     * 无数据的构造函数，success和message可自定义
     *
     * @param success
     * @param message
     */
    public CommonResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * 默认成功且带参数的构造函数
     *
     * @param content
     */
    public CommonResponse(T content) {
        this.content = content;
        this.success = true;
        this.message = "成功";
    }

    /**
     * 执行成功，返回值为null
     *
     * @return
     */
    public static CommonResponse<String> success() {
        return new CommonResponse<>(null);
    }

    /**
     * 执行成功，返回String类型的数据
     *
     * @param data 需要返回的数据
     * @return
     */
    public static CommonResponse<String> success(String data) {
        return new CommonResponse<>(data);
    }

    /**
     * 默认执行执行失败方法
     *
     * @return
     */
    public static CommonResponse<String> fail() {
        return new CommonResponse<>(false, "失败");
    }

    /**
     * 执行失败，定制返回消息
     *
     * @param message 返回消息
     * @return
     */
    public static CommonResponse<String> fail(String message) {
        return new CommonResponse<>(false, message);
    }
}
