package com.boot.shiro.core.service;

import com.boot.shiro.core.bean.UserInfo;

public interface UserInfoService {
	
	public UserInfo findByUsername(String name);
	
}
