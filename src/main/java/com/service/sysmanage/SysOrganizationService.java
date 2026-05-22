package com.service.sysmanage;

import com.commons.result.OrgTreeVo;
import com.commons.result.TreeVo;
import com.model.sysmanage.SysOrganization;

import java.util.List;

/**
 * @description：部门管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public interface SysOrganizationService {
    /**
     * 查询部门资源树
     *
     * @return
     */
    List<TreeVo> findTree(String code,String organtype);

    /**
     * 查询部门数据表格
     *
     * @return
     */
    List<SysOrganization> findOrganizationsByCode(String code);
    public String getQymcByQybh(String qybh);
    public String getQyjcByQybh(String qybh);
    
    /**
     * 查询部门数据表格
     *
     * @return
     */
    SysOrganization findOrganizationByCode(String code);
    
    /**
     * 根据code删除部门
     *
     * @param code
     */
    void deleteOrganizationByCode(String code);
    

    /**
     * 获取下属组织机构
     * @param code 组织机构
     * @return
     */    
    List<SysOrganization> findChildren(String code,List<SysOrganization> organs);
    
    /**
     * 获取下属组织机构Code
     * @param code 组织机构
     * @return
     */ 
    String findChildrenCodes(String code);

    List<OrgTreeVo> findOrgTree(String currentUserOrganCode, String organtype);
    /**
     * 查询所有组织机构
     *
     * @return
     */
    public List<SysOrganization> findOrganAll(String porgan,String organ);

    /**
     * 查询所有组织机构
     *
     * @return
     */
    public List<SysOrganization> findAllOrgan();

    public List<OrgTreeVo> findOrgTreeForOrgan(String code, String organtype);

}
