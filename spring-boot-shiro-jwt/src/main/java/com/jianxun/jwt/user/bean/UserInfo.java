package com.jianxun.jwt.user.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private long uid;//用户id.
    private String username;//账号.
    private String name;//名称，昵称或者姓名.
    private String password;//密码.
    private String salt;//密码加密的盐. 主要加强密码的安全性.
    private byte state;//用户状态，0：创建用户；1：正常；2：用户被锁定了.
    private List<SysRole> roles;
    public String getUserNameAndSalt() {
        return this.username+this.salt;
    }
}
