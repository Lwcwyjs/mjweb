package com.mapper.configmanage;

import org.apache.ibatis.annotations.Param;

import com.model.configmange.SysResource;
import com.model.sysmanage.SysRole;

import java.util.List;

public interface ResourceMapper {

    /**
     * 查询菜单资源
     *
     * @param resourceType
     * @param pid
     * @return
     */
    List<SysResource> findResourceAllByTypeAndPid(@Param("resourceType") Integer resourceType, @Param("pid") Long pid);

    /**
     * 查询一级资源
     *
     * @param resourceMenu
     * @return
     */
    List<SysResource> findResourceAllByTypeAndPidNull(Integer resourceMenu);
    
    /**
     * 查询角色下的菜单列表(访问授权)
     *
     * @param roleId 角色ID
     * @param resourceType 资源类型
     * @return
     */
    List<SysResource> findResourceListByRoleIdAndType(@Param("roleId") Long roleId,@Param("resourceType") String resourceType,@Param("pid") String pid,@Param("type") int type,@Param("parent") long parent);


/**
 * 查询角色下的菜单列表(传播授权)
 *
 * @param roleId 角色ID
 * @param resourceType 资源类型
 * @return
 */
List<SysResource> findResourceListByRoleIdAndTypeTran(@Param("roleId") Long roleId,@Param("resourceType") String resourceType,@Param("pid") String pid,@Param("type") int type);


/**
 * 查询角色的角色类型(访问授权)
 *
 * @param roleId 角色ID
 * @param resourceType 资源类型
 * @return
 */
SysRole findresourceOneByRoleId(@Param("roleId") Long roleId);


List<SysResource> findResourceSonByregroup(@Param("regroup") int regroup,@Param("resourceType") String resourceType,@Param("Pid") int Pid,@Param("Roleid") int Roleid);

List<SysResource> findResourceSon(@Param("resourceType") String resourceType,@Param("Pid") int Pid,@Param("Roleid") int Roleid);


    List<SysResource> findResourceSonByregroupForGrant(@Param("regroup") int regroup,@Param("resourceType") String resourceType,@Param("ParentRole") int ParentRole);

List<SysResource> findResourceButtonByregroupForGrant(@Param("pid") String pid,@Param("resourceType") String resourceType);

}
