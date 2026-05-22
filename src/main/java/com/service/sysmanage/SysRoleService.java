package com.service.sysmanage;

import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysRoleResource;
import com.model.sysmanage.SysUserRole;
import com.commons.result.Tree;

import java.util.List;

/**
 * @description：角色管理
 * @author：marq
 * @date：2016/11/03 10:35
 */
public interface SysRoleService {
	//根据Id查询Role
	SysRole findRoleById(Long roleId);
	//加载所有角色
    List<SysRole> findAllRoles();
    //查询角色树
    List<Tree> findTree(Long roleId);
    //根据角色id查子角色id
    List<Long> findChildrenId(Long roleId);
    //根据角色id查子角色
    List<SysRole> findChildren(Long roleId);
    //删除角色
    void delete(Long roleId);
    
    //根据用户ID查找UserRole
    List<SysUserRole> findUserRoleByUserId(Long userId);
    //根据角色ID查找UserRole
    List<SysUserRole> findUserRoleByRoleId(Long roleId);
    
    //根据角色ID查找RoleResource
    List<SysRoleResource> findRoleResourceByRoleId(Long roleId);
    
    //根据角色ID查找RoleResource_grant
    List<SysRoleResource> TranfindRoleResourceByRoleId(Long roleId);
    
    //根据角色ID查找资源ID
    List<Long> findResourceIdByRoleId(Long roleId);
    
    //根据角色ID查找资源ID(传播授权)
    List<Long> TranfindResourceIdByRoleId(Long roleId);
    //角色授权
    void grant(Long roleId,String resourceIds);
    
    //传播授权
    void Tran(Long roleId,String resourceIds);
}
