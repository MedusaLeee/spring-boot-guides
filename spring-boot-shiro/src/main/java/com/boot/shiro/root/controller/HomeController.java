package com.boot.shiro.root.controller;

import com.boot.shiro.core.bean.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class HomeController {
    @GetMapping(path = "/403")
    public String notAuthPage() {
        return "403";
    }
    @GetMapping(path = {"/", "/index"})
    public String index(Map<String, Object> map) {
        // 获取用户信息 Shiro --- SecurityUtils.getSubject()  获取UserInfo
        Subject subject = SecurityUtils.getSubject();
        UserInfo userInfo = (UserInfo) subject.getPrincipal();
        map.put("userInfo", userInfo);
        return "/index";
    }
    @GetMapping(path = "/login")
    public String login() {
        return "/login";
    }
    @PostMapping(value = "/login")
    public String login(HttpServletRequest request, Map<String, Object> map) {
        //登录失败从request对象中可以获取shiro处理异常的细信息.
        //shiroLoginFailure.
        String exception = (String)request.getAttribute("shiroLoginFailure");
        System.out.println("exception= " + exception);
        String msg = "";
        if(exception != null){
            if(UnknownAccountException.class.getName().equals(exception)){
                msg = "账号不存在";
            }else if(IncorrectCredentialsException.class.getName().equals(exception)){
                msg = "密码不正确";
            }else if(ExcessiveAttemptsException.class.getName().equals(exception)){
                msg = "密码输入错误次数过多";
            }else{
                msg = "else -->"+exception;
            }
        }
        map.put("msg", msg);
        return "/login";
    }
}
