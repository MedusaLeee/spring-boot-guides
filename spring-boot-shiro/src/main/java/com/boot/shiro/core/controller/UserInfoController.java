package com.boot.shiro.core.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
    @GetMapping(path = "/userAdd")
    @RequiresPermissions("userInfo:add")
    public String userAdd() {
        return "userInfoAdd";
    }
    @GetMapping(path = "/userDel")
    @RequiresPermissions("userInfo:del")
    public String userDel() {
        return "userInfoDel";
    }
    @GetMapping(path = "/userList")
    @RequiresPermissions("userInfo:view")
    public String userList() {
        return "userInfoList";
    }
}
