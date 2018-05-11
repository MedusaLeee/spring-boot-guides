package com.jianxun.jwt.user.mapper;


import com.jianxun.jwt.user.bean.SysPermission;
import com.jianxun.jwt.user.bean.SysRole;
import com.jianxun.jwt.user.bean.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface UserInfoMapper {
    public UserInfo findUserByUsernameAndPassword(HashMap hashMap);
    public UserInfo findUserById(Long id);
    public List<SysRole> findRoleByUId(Long uId);
    public List<SysPermission> findPermissionByRoleId(Long roleId);
}
