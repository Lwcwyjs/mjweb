package com.controller.configmanage;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.result.Tree;
import com.commons.utils.DealString;
import com.model.configmange.SysResource;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysUser;
import com.service.configmanage.SysResourceService;
import com.service.sysmanage.SysRoleService;
import com.service.base.PublicService;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description：资源管理
 * @author：marq @date：2016/11/04 14:51
 */
@Controller
@RequestMapping("/admin/resource")
//@Api(value = "资源树信息")
public class SysResourceController extends BaseController {

    @Autowired
    private SysResourceService resourceService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private PublicService publicService;

    /**
     * 菜单树
     *
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    @ResponseBody
    public Object tree() {
        SysUser currentUser = getCurrentUser();
        return resourceService.findTree(currentUser);
    }

    /**
     * 菜单树
     *
     * @return
     */
    @RequestMapping(value = "/treePara", method = RequestMethod.POST)
    @ResponseBody
    public Object tree(@RequestParam Map<String, Object> params) {
        //获取当前登录用户信息
        SysUser currentUser = getCurrentUser();
        Long id = null;
        String resourcetype = "";
        if (params.containsKey("id") && DealString.toString(params.get("id")) != "") {
            id = Long.valueOf(DealString.toString(params.get("id")));
        }
        if (params.containsKey("resourcetype")) {
//            resourcetype = Integer.parseInt(DealString.toString(params.get("resourcetype")));
            resourcetype = DealString.toString(params.get("resourcetype"));
        }
        List<Tree> trees = resourceService.findTree(currentUser, id, resourcetype);
        for (Tree tree : trees) {
            String text = tree.getText();
        }
        return trees;
    }

    /**
     * 菜单树(加载所有最高节点)
     *
     * @return
     */
    @RequestMapping(value = "/treelist", method = RequestMethod.POST)
    @ResponseBody
    public Object treelist() {
        publicService.insertLogInfo("配置管理中功能配置管理，查询了ai_sys_resource", "查询", "1", "功能模块管理");
        return resourceService.findAllTreesOne();
    }

    /**
     * 资源管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "configmanage/sysResource";
    }

    /**
     * 资源管理列表
     *
     * @return
     */
//	@ApiOperation(value = "资源树查询", notes = "资源树查询")
//	@ApiImplicitParam(name = "params", value = "参数MAP", paramType = "PID-父节点", required = true, dataType = "Map<String, Object>")
    @RequestMapping(value = "/treeGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object treeGrid(@RequestParam Map<String, Object> params) {
        Long id;
        Map<String, Object> map = new HashMap<>();
        if (params.containsKey("pid") && StringUtils.isNoneBlank(params.get("pid").toString())) {
            id = Long.valueOf(params.get("pid").toString());
        } else {
            id = null;
        }
        List<SysResource> sysResources = resourceService.findResourceAll(id);
        map.put("data", sysResources);
        return map;
    }

    /**
     * 添加资源页
     *
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage(Model model) {
        model.addAttribute("oper", "add");
        return "configmanage/sysResourceEdit";
    }

    /**
     * 添加资源
     *
     * @param sysResource
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(SysResource sysResource) {
        List<String> bindResult = new ArrayList<String>();
        sysResource.setCreatedate(new Date());
        sysResource.setStatus(0);
        String resourcetype = null;
        String status = null;
        if (sysResource.getPid()==null){
            sysResource.setPid(Long.parseLong("-1"));
        }
        try {
            bindResult = ValidateService.valid(sysResource);
            if (bindResult.size() == 0) {
                resourceService.insert(sysResource);

                if (sysResource.getResourcetype() == 0) {
                    resourcetype = "菜单";
                }else if (sysResource.getResourcetype() == 1){
                    resourcetype = "按钮";
                }else {
                    resourcetype = "导航栏";
                }
                if (sysResource.getStatus() == 0) {
                    status = "启用";
                } else {
                    status = "禁用";
                }
                publicService.insertLogInfo("配置管理的功能配置管理中，点击新建，成功添加【" + "资源名称：" + sysResource.getName() + ",访问地址："
                        + sysResource.getUrl() + ",资源类型：" + resourcetype + ",状态：" + status + "】的功能配置信息", "新建", "1", "功能模块管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "配置管理的功能配置管理中，点击新建，添加失败【" + "资源名称：" + sysResource.getName() + ",访问地址：" + sysResource.getUrl()
                            + ",资源类型：" + resourcetype + ",状态：" + status + "】的功能配置信息,异常:" + e.getMessage(),
                    "新建", "0", "功能模块管理");
            bindResult.add("异常：" + e.getMessage());
        }
        return bindResult.size() == 0 ? renderSuccess("添加成功！") : renderError(bindResult.toString());
    }

    /**
     * 二级资源树
     *
     * @return
     */
    @RequestMapping("/allTree")
    @ResponseBody
    public Object allTree() {
        return resourceService.findAllTree();
    }

    /**
     * 三级资源树(访问授权)
     *
     * @return
     */
    @RequestMapping(value = "/allTrees/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public Object allTrees(@PathVariable Long roleId, HttpServletRequest request) {
        return resourceService.findAllTrees(roleId);
    }

    /**
     * 三级资源树(传播授权)
     *
     * @return
     */
    @RequestMapping(value = "/TransmissionallTrees/{roleId}", method = RequestMethod.POST)
    @ResponseBody
    public Object TransmissionallTrees(@PathVariable Long roleId, HttpServletRequest request) {
        SysRole sysRole = sysRoleService.findRoleById(this.getCurrentUserRoleId());
        // Long pid = sysRole.getPid() == null ? roleId : sysRole.getPid();
        int type = sysRole.getType();
        return resourceService.findAllTransmissionTrees(roleId);
    }

    /**
     * 编辑资源页
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(HttpServletRequest request, Long id) {
            SysResource sysResource = resourceService.findResourceById(id);
        if (sysResource != null) {
            Long pid = sysResource.getPid();
            if (pid != null) {
                SysResource sysResourceex = resourceService.findResourceById(pid);
                if (sysResourceex!=null){
                    sysResource.setPidname(sysResourceex.getName());
                }
            }
        }
        request.setAttribute("resource", sysResource);
        request.setAttribute("oper", "edit");
        return "configmanage/sysResourceEdit";
//        return "login/index";
    }

    /**
     * 编辑资源
     *
     * @param sysResource
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(SysResource sysResource) {
        List<String> bindResult = new ArrayList<String>();
        String resourcetype = null;
        String status = null;
        try {
            bindResult = ValidateService.valid(sysResource);
            if (bindResult.size() == 0) {
                // 更新ai_sys_resource表的数据
                publicService.update("ai_sys_resource", "id", "id", sysResource);
                // 判断是否修改为通用功能
				/*if (sysResource.getRegroup() != 0) {
					// 不是通用功能,先删掉中间表中除admin外所有拥有该资源的数据再添加，防止重复
					publicService.delete(
							"delete from ai_sys_role_resource where resource_id=" + sysResource.getId() + " and role_id !=1");
					publicService.insert("INSERT INTO ai_sys_role_resource (role_id, resource_id) select id,"
							+ sysResource.getId() + " from ai_sys_role where type=" + sysResource.getRegroup());
				} else {
					// 是通用功能,为下面所有用户添加该资源
					publicService.delete("delete from ai_sys_role_resource where resource_id=" + sysResource.getId());
					publicService.insert("insert into ai_sys_role_resource (role_id, resource_id)select id,"
							+ sysResource.getId() + " from ai_sys_role");

				}*/
                sysResource = publicService.selectObj("ai_sys_resource", "", "id", sysResource);

                if (sysResource.getResourcetype() == 0) {
                    resourcetype = "菜单";
                } else {
                    resourcetype = "按钮";
                }
                if (sysResource.getStatus() == 0) {
                    status = "启用";
                } else {
                    status = "禁用";
                }

                publicService.insertLogInfo("配置管理的功能配置管理中，点击编辑，成功修改为【" + "资源名称：" + sysResource.getName() + ",访问地址："
                        + sysResource.getUrl() + ",资源类型：" + resourcetype + ",状态：" + status + "】的功能配置信息", "修改", "1", "功能模块管理");

            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "配置管理的功能配置管理中，点击编辑，修改失败,【" + "资源名称：" + sysResource.getName() + ",访问地址：" + sysResource.getUrl()
                            + ",资源类型：" + resourcetype + ",状态：" + status + "】的功能配置信息,异常:" + e.getMessage(),
                    "修改", "0", "功能模块管理");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("编辑成功！") : renderError(bindResult.toString());
    }

    /**
     * 删除资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        List<String> bindResult = new ArrayList<String>();
        try {
            SysResource sysResource = new SysResource();
            sysResource.setId(id);
            sysResource = publicService.selectObj("ai_sys_resource", "", "id", sysResource);
            resourceService.deleteResourceById(id);
            String resourcetype = null;
            String status = null;
            if (sysResource.getResourcetype() == 0) {
                resourcetype = "菜单";
            } else {
                resourcetype = "按钮";
            }
            if (sysResource.getStatus() == 0) {
                status = "启用";
            } else {
                status = "禁用";
            }

            publicService.insertLogInfo("配置管理的功能配置管理中，点击删除，成功删除【" + "资源名称：" + sysResource.getName() + ",访问地址："
                    + sysResource.getUrl() + ",资源类型：" + resourcetype + ",状态：" + status + "】的功能配置信息", "删除", "1", "功能模块管理");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("配置管理的功能配置管理中，点击删除，删除ID为" + id + "的信息，删除失败", "删除", "0", "功能模块管理");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("删除成功！") : renderError(bindResult.toString());
    }

}
