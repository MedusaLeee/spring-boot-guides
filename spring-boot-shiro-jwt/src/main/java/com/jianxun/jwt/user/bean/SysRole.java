package com.jianxun.jwt.user.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;//主键.

    private String role;//角色标识. 如：“admin|vip” 这个是需要唯一的.

    private String description;//角色描述,超级管理员，vip用户.

    private Boolean available = false;//是否可用.

    //用户 -- 角色 关系.
    private List<UserInfo> userInfos;

    //角色 - 权限有关系.
    private List<SysPermission> permissions;//
}
