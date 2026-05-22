package com.commons.shiro;

import com.model.configmange.SysResource;
import com.model.sysmanage.SysUser;
import com.model.sysmanage.SysUserRole;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import com.service.base.DataItemService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description：shiro权限认证
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public class ShiroDbRealm extends AuthorizingRealm {

    private static Logger LOGGER = LoggerFactory.getLogger(ShiroDbRealm.class);

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
	private DataItemService dataItemService;

    /**
     * Shiro登录认证(原理：用户提交 用户名和密码  --- shiro 封装令牌 ---- realm 通过用户名将密码查询返回 ---- shiro 自动去比较查询出密码和用户输入密码是否一致---- 进行登陆控制 )
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        LOGGER.info("Shiro开始登录认证");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        SysUser sysUser = sysUserService.findUserByLoginName(token.getUsername());
        // 账号不存在
        if (sysUser == null) {
            return null;
        }
        // 账号未启用
        if (sysUser.getStatus() == 1) {
            return null;
        }
        //List<Long> roleList = roleService.findRoleIdListByUserId(user.getId());
        List<SysUserRole> roleList=(List<SysUserRole>)(List)dataItemService.selectObjects(String.format("select role_id as id from ai_sys_user_role where user_id=%s", sysUser.getId().toString()), SysUserRole.class);
        
        ShiroUser shiroUser = new ShiroUser(sysUser.getId(), sysUser.getLoginname(), sysUser.getName(), roleList);
        // 认证缓存信息
        return new SimpleAuthenticationInfo(shiroUser, sysUser.getPassword().toCharArray(), getName());

    }

    /**
     * Shiro权限认证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {

        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        List<SysUserRole> roleList = shiroUser.roleList;

        Set<String> urlSet = new HashSet<String>();
        for (SysUserRole sysUserRole : roleList) {
//        	String sql=String.format(" SELECT e.id AS id,s.url AS url FROM ai_sys_role r LEFT JOIN ai_sys_role_resource e ON r.id = e.role_id LEFT JOIN ai_sys_resource s ON e.resource_id = s.id WHERE r.id = %s", userRole.getId().toString());
        	String sql=String.format(" SELECT e.id AS id,s.url AS url FROM ai_sys_role r LEFT JOIN ai_sys_role_resource e ON r.id = e.role_id LEFT JOIN ai_sys_resource s ON e.resource_id = s.id WHERE r.id = %s", sysUserRole.getId().toString());
            
        	List<SysResource> roleResourceList = (List<SysResource>)(List)dataItemService.selectObjects(sql, SysResource.class);;
            if (roleResourceList != null) {
                for (SysResource map : roleResourceList) {
                    if (StringUtils.isNoneBlank(map.getUrl())) {
                        urlSet.add(map.getUrl());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(urlSet);
        return info;
    }
}
