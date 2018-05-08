package com.jianxun.jwt.user.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {
    @NotBlank(message = "不能为空")
    private String username;
    @NotBlank(message = "不能为空")
    private String password;
}
