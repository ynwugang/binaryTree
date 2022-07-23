package com.wugang.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLoginRequest {
    /**
     * 登录名
     */
    @NotEmpty(message = "【用户名】不能为空")
    private String loginName;

    /**
     * 密码
     */
    @NotEmpty(message = "【密码】不能为空")
    private String password;
}
