package com.boot.shiro.core.repository;

import com.boot.shiro.core.bean.UserInfo;
import org.springframework.data.repository.CrudRepository;


/**
 * 用户持久化类.
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    //通过用户名查找用户信息.
    public UserInfo findByUsername(String name);
}
