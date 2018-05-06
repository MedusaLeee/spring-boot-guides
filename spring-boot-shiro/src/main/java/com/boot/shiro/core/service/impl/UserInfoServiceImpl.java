package com.boot.shiro.core.service.impl;

import com.boot.shiro.core.bean.UserInfo;
import com.boot.shiro.core.repository.UserInfoRepository;
import com.boot.shiro.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Override
	public UserInfo findByUsername(String name) {
		return userInfoRepository.findByUsername(name);
	}

}
