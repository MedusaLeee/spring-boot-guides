package com.jianxun.jwt.config;

import com.jianxun.jwt.config.StatelessAuthenticationToken;
import com.jianxun.jwt.user.bean.SysPermission;
import com.jianxun.jwt.user.bean.SysRole;
import com.jianxun.jwt.user.bean.UserInfo;
import com.jianxun.jwt.user.service.UserInfoService;
import com.jianxun.jwt.util.JWTUtil;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

public class StatelessAuthorizingRealm extends AuthorizingRealm {

    @Value("${sys.token.secret}")
    private String secret;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessAuthenticationToken;
    }
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //如果打印信息只执行一次的话，说明缓存生效了，否则不生效. --- 配置缓存成功之后，只会执行1次/每个用户，因为每个用户的权限是不一样的.
        System.out.println("MyShiroRealm.doGetAuthorizationInfo()");
        //这是shiro提供的.
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //获取到用户的权限信息.
        UserInfo userInfo = (UserInfo)principals.getPrimaryPrincipal();
        if(userInfo != null) {
            for(SysRole role:userInfo.getRoles()){
                //添加角色.
                authorizationInfo.addRole(role.getRole());
                //添加权限.
                for(SysPermission p: role.getPermissions()){
                    authorizationInfo.addStringPermission(p.getPermission());
                }
            }
        }
        return authorizationInfo;
    }
    // 身份校验
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        StatelessAuthenticationToken statelessToken = (StatelessAuthenticationToken) token;
        String jwtToken = statelessToken.getToken();
        HashMap<String, String> hashMap = JWTUtil.verify(jwtToken, secret);
        if (hashMap == null) {
            throw new AuthenticationException("token invalid");
        }
        Long uid = Long.valueOf(hashMap.get("uId"));
        UserInfo userInfo = userInfoService.findUserById(uid);
        if (userInfo == null) {
            throw new AuthenticationException("token invalid");
        }
        return new SimpleAuthenticationInfo(userInfo, jwtToken, getName());
    }
}
