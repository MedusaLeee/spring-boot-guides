package com.jianxun.jwt.user.service.impl;

import com.jianxun.jwt.user.bean.SysRole;
import com.jianxun.jwt.user.bean.UserInfo;
import com.jianxun.jwt.user.mapper.UserInfoMapper;
import com.jianxun.jwt.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;
    @Autowired
    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }
    @Override
    public UserInfo findAdminByUsernamePassword(String username, String password) {
        System.out.println("username:" + username);
        System.out.println("password:" + password);

        HashMap<String, String> hashMap =  new HashMap<>();
        hashMap.put("username", username);
        hashMap.put("password", password);
        UserInfo userInfo = userInfoMapper.findUserByUsernameAndPassword(hashMap);
        return userInfo;
    }

    @Override
    public UserInfo findUserById(Long id) {
        UserInfo userInfo = userInfoMapper.findUserById(id);
        if (userInfo != null) {
            List<SysRole> roleList = userInfoMapper.findRoleByUId(id);
            for(SysRole role: roleList) {
                role.setPermissions(userInfoMapper.findPermissionByRoleId(role.getId()));
            }
            userInfo.setRoles(roleList);
        }
        return userInfo;
    }
}
