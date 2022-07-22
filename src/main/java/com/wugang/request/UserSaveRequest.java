package com.wugang.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSaveRequest {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 登录名
     */
    @NotNull(message = "【用户名】不能为空")
    private String loginName;

    /**
     * 昵称
     */
    @NotNull(message = "【昵称】不能为空")
    private String name;

    /**
     * 密码
     */
    @NotNull(message = "【密码】不能为空")
    // @Length(min = 6, max = 20, message = "【密码】6~20位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】至少包含 数字和英文，长度6-32")
    private String password;
}
