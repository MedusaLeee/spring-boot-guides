package com.jianxun.jwt.user.mapper;


import com.jianxun.jwt.user.bean.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface UserInfoMapper {
    public UserInfo findUserByUsernameAndPassword(HashMap hashMap);
}
