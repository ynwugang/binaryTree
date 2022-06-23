package com.wugang.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResp<T> {
    /**
     * 业务的成功或者失败
     */
    private Boolean success = true;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据（泛型）
     */
    private T content;
}
