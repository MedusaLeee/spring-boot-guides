package com.jianxun.jwt.user.service;

import com.jianxun.jwt.user.bean.UserInfo;

import java.util.HashMap;

public interface UserInfoService {
    public UserInfo findAdminByUsernamePassword(String username, String password);
}
