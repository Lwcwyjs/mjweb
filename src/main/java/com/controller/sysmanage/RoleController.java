package com.controller.sysmanage;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.model.configmange.SysResource;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysRoleResource;
import com.service.sysmanage.SysRoleService;
import com.service.base.PublicService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description：角色管理
 * @author：marq @date：2016/11/03 14:51
 */
@Controller
@RequestMapping("/admin/role")
public class RoleController extends BaseController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	@Resource
	private PublicService publicService;

	/**
	 * 角色管理页
	 *
	 * @return
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String manager(HttpServletRequest request) {
		request.setAttribute("id", this.getCurrentUserRoleId());
		return "sysmanage/sysRole";
	}

	/**
	 * 角色列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/treeGrid", method = RequestMethod.GET)
	@ResponseBody
	public Object treeGrid() {
		Map<String, Object> map = new HashMap<>();
		publicService.insertLogInfo("系统管理中角色管理,查询了ai_sys_role", "查询", "1","角色管理");
		List<SysRole> sysRoles= sysRoleService.findChildren(this.getCurrentUserRoleId());
		map.put("data", sysRoles);
		return map;
	}

	/**
	 * 角色树
	 *
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.POST)
	@ResponseBody
	public Object tree() {
		return sysRoleService.findTree(this.getCurrentUserRoleId());
	}

	/**
	 * 添加角色页
	 *
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public String addPage(HttpServletRequest request) {
		request.setAttribute("role", new SysRole());
		request.setAttribute("oper", "add");
		return "sysmanage/sysRoleEdit";
	}

	/**
	 * 添加角色
	 *
	 * @param sysRole
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(SysRole sysRole) {
		List<String> bindResult = new ArrayList<String>();

		if (sysRole.getPid() == null) {
			sysRole.setPid(this.getCurrentUserRoleId());
		}
		try {
			bindResult = ValidateService.valid(sysRole);
			if (bindResult.size() == 0) {
				// 设置权限
				List<Long> roleChildren = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
				if (roleChildren.contains(sysRole.getPid())) {
					publicService.insert("ai_sys_role", "id", "id", sysRole);
					publicService.insertLogInfo("系统管理的角色管理中，点击新建，成功添加【" + "角色名称：" + sysRole.getName() + "】的角色信息", "新建",
							"1","角色管理");
				} else {
					bindResult.add("权限不足,不允许创建该角色");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo(
					"系统管理的角色管理中，点击新建，添加失败【" + "角色名称：" + sysRole.getName() + "】的角色信息,异常:" + e.getMessage(), "新建", "0","角色管理");
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("添加成功！") : renderError(bindResult.toString());
	}

	/**
	 * 删除角色
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(Long id, HttpServletRequest request) {
		List<String> bindResult = new ArrayList<String>();
		try {
			// date:2018.03.06 author:Xukz description:删除权限限制
			List<Long> roleChildren = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
			if (roleChildren.contains(id)) {
				SysRole sysRole = new SysRole();
				sysRole.setId(id);
				sysRole = publicService.selectObj("ai_sys_role", "", "id", sysRole);
				sysRoleService.delete(id);
				publicService.insertLogInfo("系统管理的角色管理中，点击删除，成功删除【ID:"+id + "角色名称：" + sysRole.getName() + "】的角色信息", "删除",
						"1","角色管理");
			}
		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo("系统管理的角色管理中，点击删除，ID为"+id+"的删除失败,异常:" + e.getMessage(), "删除", "0","角色管理");
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("删除成功！") : renderError(bindResult.toString());
	}

	/**
	 * 编辑角色页
	 *
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(Long id, HttpServletRequest request) {
		SysRole sysRole = sysRoleService.findRoleById(id);
		if (sysRole != null) {
			Long pid = sysRole.getPid();
			if (pid != null) {
				SysRole sysRoleex = sysRoleService.findRoleById(pid);
				if (sysRoleex!=null){
					sysRole.setPidname(sysRoleex.getName());
				}
			}
		}
		request.setAttribute("role", sysRole);
		request.setAttribute("oper", "edit");
		return "sysmanage/sysRoleEdit";
	}

	/**
	 * 编辑角色
	 *
	 * @param sysRole
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(SysRole sysRole) {
		List<String> bindResult = new ArrayList<String>();

		try {
			bindResult = ValidateService.valid(sysRole);
			if (sysRole.getId() == sysRole.getPid()) {
				bindResult.add("上级角色不能是其本身！");
			}
			if (bindResult.size() == 0) {
				// 设置权限
				List<Long> roleChildren = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
				if (roleChildren.contains(sysRole.getPid())) {
					publicService.update("ai_sys_role", "id", "id", sysRole);
					publicService.delete("delete from ai_sys_role_resource where role_id=" + sysRole.getId());
					publicService.update("insert into ai_sys_role_resource(role_id,resource_id) select " + sysRole.getId()
							+ ", id from ai_sys_resource where regroup=" + sysRole.getType());
					sysRole = publicService.selectObj("ai_sys_role", "", "id", sysRole);
					publicService.insertLogInfo("系统管理的角色管理中，点击编辑，成功修改【" + "角色名称：" + sysRole.getName() + "】的角色信息", "修改","1","角色管理");
				} else {
					bindResult.add("权限不足,不允许编辑该角色");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo("系统管理的角色管理中，点击编辑，修改失败【" + "角色名称：" + sysRole.getName() + "】的角色信息,异常:"+e.getMessage(), "修改","0","角色管理");
			
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("编辑成功！") : renderError(bindResult.toString());
	}

	/**
	 * 授权页面
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/transmissionPage")
	public String TransmissionPage(Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("pid", this.getCurrentUserRoleId());
		return "sysmanage/sysRoleTransmission";
	}
	
	
	/**
	 * 授权页面
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/grantPage")
	public String grantPage(Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("pid", this.getCurrentUserRoleId());
		return "sysmanage/sysRoleGrant";
	}

	/**
	 * 授权页面根据角色查询资源(访问授权)
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/findResourceIdListByRoleId/{id}")
	@ResponseBody
	public Object findResourceByRoleId(@PathVariable Long id, HttpServletRequest request) {
		List<Long> rIds = sysRoleService.findResourceIdByRoleId(id);

		return renderSuccess(rIds);
	}
	
	/**
	 * 授权页面根据角色查询资源(传播授权)
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/TranfindResourceIdListByRoleId/{id}")
	@ResponseBody
	public Object TranfindResourceByRoleId(@PathVariable Long id, HttpServletRequest request) {
		List<Long> rIds = sysRoleService.TranfindResourceIdByRoleId(id);
		return renderSuccess(rIds);
	}

	/**
	 * 角色授权(访问授权)
	 *
	 * @param id
	 * @param resourceIds
	 * @return
	 */
	@RequestMapping("/grant/{id}/{resourceIds}")
	@ResponseBody
	public Object grant(@PathVariable Long id, @PathVariable String resourceIds) {
		List<String> bindResult = new ArrayList<String>();
		try {	
		List<Long> pid = new ArrayList<Long>();

		String[] resources = resourceIds.split(",");
		for (String resourceId : resources) {
			
			SysRole sysRole=new SysRole();
			sysRole.setId(id);
			sysRole=publicService.selectObj("ai_sys_role", "", "id", sysRole);
			//获取当前角色的父角色所拥有的资源ID(子角色权限不可大于父角色权限)
			SysRoleResource sysRoleResource = new SysRoleResource();
			
			if (sysRole.getPid().longValue() != -1) {
				sysRoleResource.setRole_id(sysRole.getPid());
				List<SysRoleResource> sysRoleResources = (List<SysRoleResource>)(List)publicService.selectObjects("ai_sys_role_resource", "", "role_id", sysRoleResource);
				for (SysRoleResource sysRoleResource2 : sysRoleResources) {
					pid.add(sysRoleResource2.getResource_id());
				}
				List<SysRoleResource> tranroleresource = (List<SysRoleResource>)(List)publicService.selectObjects("ai_sys_role_resource_grant", "", "role_id", sysRoleResource);
				for (SysRoleResource tranroleResourceSys : tranroleresource) {
					pid.add(tranroleResourceSys.getResource_id());
				}
				SysResource sysResource = new SysResource();
				if (StringUtils.isNotBlank(resourceId)) {
					if (Integer.valueOf(resourceId) != -1) {
					
					sysResource.setId(Long.parseLong(resourceId));
					sysResource = publicService.selectObj("ai_sys_resource", "", "id",sysResource);
					try {
//						//判断父角色权限是否包含子角色权限
//						if (pid.contains(sysResource.getId())) {}
//						else {
//							return renderError("授权失败:授权权限中包含父角色没有的权限");
//						}
	
					} catch (Exception e) {
						// TODO: handle exception
							e.printStackTrace();
							bindResult.add("异常：");
							return bindResult.size() == 0 ? renderSuccess("授权成功！") : renderError("您还有未选择功能分组的资源:"+sysResource.getName());
						}	
					}					
				}
			}
		}

		try {
			sysRoleService.grant(id, resourceIds);
		} catch (Exception e) {
			e.printStackTrace();
			bindResult.add("异常：" + e.getMessage());
		}
		return bindResult.size() == 0 ? renderSuccess("授权成功！") : renderError(bindResult.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			bindResult.add("异常：" + e.getMessage());
		}
		return bindResult.size() == 0 ? renderSuccess("授权成功！") : renderError(bindResult.toString());

	}

	/**
	 * 角色授权(传播授权)
	 *
	 * @param id
	 * @param resourceIds
	 * @return
	 */
	@RequestMapping("/transmission/{id}/{resourceIds}")
	@ResponseBody
	public Object transmission(@PathVariable Long id, @PathVariable String resourceIds) {
		List<String> bindResult = new ArrayList<String>();

		try {
			// date:2018.03.06 author:Xukz description:角色授权权限限制
			List<Long> roleChildren = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
			if (roleChildren.contains(id)) {
				sysRoleService.Tran(id, resourceIds);
				SysRole sysRole = new SysRole();
				sysRole.setId(id);
				sysRole = publicService.selectObj("ai_sys_role", "", "id", sysRole);
				publicService.insertLogInfo("系统管理的角色管理中，点击授权，成功为【" + "角色名称：" + sysRole.getName() + "】的角色授权", "修改","1","角色管理");
			}
		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo("系统管理的角色管理中，点击授权，ID为"+id+"的角色授权失败", "修改","0","角色管理");
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("授权成功！") : renderError(bindResult.toString());
	}

}
