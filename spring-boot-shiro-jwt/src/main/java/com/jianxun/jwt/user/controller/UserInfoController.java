package com.jianxun.jwt.user.controller;

import com.jianxun.jwt.user.bean.LoginForm;
import com.jianxun.jwt.user.bean.UserInfo;
import com.jianxun.jwt.user.service.UserInfoService;
import com.jianxun.jwt.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;
    @Value("${sys.token.secret}")
    private String secret;
    @Value("${sys.token.jwt-expire-time}")
    private int jwtExpireTime;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public HashMap login(@Valid @RequestBody LoginForm loginForm) {
        UserInfo userInfo = userInfoService.findAdminByUsernamePassword(loginForm.getUsername(), loginForm.getPassword());
        String token = JWTUtil.sign(String.valueOf(userInfo.getUid()), secret, jwtExpireTime);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        return hashMap;
    }
    @RequiresUser
    @GetMapping(value = "/user")
    public UserInfo getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        UserInfo userInfo = (UserInfo)subject.getPrincipal();
        return userInfo;
    }
    @RequiresRoles("admin")
    @GetMapping(value = "/role")
    public UserInfo getUserInfo2() {
        Subject subject = SecurityUtils.getSubject();
        UserInfo userInfo = (UserInfo)subject.getPrincipal();
        return userInfo;
    }
    @RequiresPermissions("userInfo/userAdd")
    @GetMapping(value = "/permission")
    public UserInfo getUserInfo3() {
        Subject subject = SecurityUtils.getSubject();
        UserInfo userInfo = (UserInfo)subject.getPrincipal();
        return userInfo;
    }
    @GetMapping(value = "/hello")
    public String hello() {
        // 如果抛出这个异常说明成功 org.apache.shiro.subject.support.DisabledSessionException
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        System.out.println(session);
        return "hello";
    }
}
