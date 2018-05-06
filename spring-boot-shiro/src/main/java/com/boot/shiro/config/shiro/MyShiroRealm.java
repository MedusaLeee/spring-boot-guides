package com.boot.shiro.config.shiro;

import com.boot.shiro.core.bean.SysPermission;
import com.boot.shiro.core.bean.SysRole;
import com.boot.shiro.core.bean.UserInfo;
import com.boot.shiro.core.service.UserInfoService;
import org.apache.coyote.http2.ByteUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

// 需要注入到shiro config中
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoService;

    /*
    身份认证 --- 登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("MyShiroRealm doGetAuthenticationInfo");
        /**
         * 1. 获取用户输入的账号
         * 2. 通过username从数据库中从数据库中查找，获取到UserInfo对象.
         * 3. 加密，使用SimpleAuthenticationInfo进行身份处理
         * 4. 返回身份处理对象
         */
        // 1. 获取用户输入的账号
        String username = (String) token.getPrincipal();
        // 2. 通过username从数据库中从数据库中查找，获取到UserInfo对象.
        UserInfo userInfo = userInfoService.findByUsername(username);
        if (userInfo == null) {
            return null;
        }
        // 3. 加密，使用SimpleAuthenticationInfo进行身份处理
        SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(userInfo, userInfo.getPassword(), ByteSource.Util.bytes(userInfo.getUserNameAndSalt()), this.getName());
        return sai;
    }
    /*
    授权，权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("MyShiroRealm doGetAuthorizationInfo");
        // shiro提供的授权对象
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        // 获取到用户的权限信息
        UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();
        for (SysRole role: userInfo.getRoles()) {
            // 添加role
            sai.addRole(role.getRole());
            for (SysPermission p: role.getPermissions()) {
                sai.addStringPermission(p.getPermission());
                // sai.addObjectPermissions();
            }
        }
        return sai;
    }
}
