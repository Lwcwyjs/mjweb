package com.service.configmanage;

import com.commons.result.Tree;
import com.model.configmange.SysResource;
import com.model.sysmanage.SysUser;

import java.util.List;

/**
 * @description：资源管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public interface SysResourceService {

    /**
     * 根据用户查询树形菜单列表
     *
     * @param currentUser
     * @return
     */
    List<Tree> findTree(SysUser currentUser);

    /**
     * 根据用户查询树形菜单列表
     *
     * @param currentUser
     * @return
     */
    List<Tree> findTree(SysUser currentUser,Long id,String type);
//    List<Tree> findTree(SysUser currentUser,Long id,Integer resourceType);

    /**
     * 查询所有资源
     *
     * @return
     */
    List<SysResource> findResourceAll();

    /**
     * 查询所有资源
     *
     * @return
     */
    List<SysResource> findResourceAll(Long id);
    
    /**
     * 查询一级树
     *
     * @return
     */
    List<Tree> findAllTreesOne();

    /**
     * 查询二级数
     *
     * @return
     */
    List<Tree> findAllTree();

    /**
     * 查询三级数(访问授权)
     *
     * @return
     */
    List<Tree> findAllTrees(Long roleId);
    
    Tree findAllTrees_resourceSon(int regroup,Tree treeOne,int ParentRole);

    //查询所有下级节点新
    Tree findAllTrees_resourceSonEX(Tree treeOne,int ParentRole);
    
    Tree findAllTrees_resourceSon_grant(int regroup,Tree treeOne,int ParentRole);


    /**
     * 查询三级数(传播授权)
     *
     * @return
     */
    List<Tree> findAllTransmissionTrees(Long roleId);
    
    /**
     * 根据id查询资源
     *
     * @param id
     * @return
     */
    SysResource findResourceById(Long id);

    /**
     * 根据id删除资源
     *
     * @param id
     */
    void deleteResourceById(Long id);
    
    /**
     * 根据id删除资源
     *
     * @param sysResource
     */
    void insert(SysResource sysResource);

}
