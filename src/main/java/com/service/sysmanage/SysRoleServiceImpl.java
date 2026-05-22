package com.service.sysmanage;

import com.service.base.PublicService;
import com.commons.result.Tree;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysRoleResource;
import com.model.configmange.SysResource;
import com.model.sysmanage.SysUserRole;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	private static Logger LOGGER = LoggerFactory.getLogger(SysRoleServiceImpl.class);

	@Autowired
	private PublicService publicService;

	@Override
	public SysRole findRoleById(Long roleId) {
		SysRole sysRole = new SysRole();
		sysRole.setId(roleId);
		return publicService.selectObj("ai_sys_role", "", "id", sysRole);
	}

	@Override
	public List<SysRole> findAllRoles() {
		return publicService.selectObjs("ai_sys_role", "", "","pid asc,seq asc",new SysRole());
	}

	@Override
	public List<Tree> findTree(Long roleId) {
		List<Tree> trees = new ArrayList<Tree>();

		List<SysRole> sysRoles = findAllRoles();

		if (roleId == 1) {
			for (SysRole sysRole : sysRoles) {
				if (sysRole.getId() == roleId) {
					Tree tree = new Tree();
					tree.setId(sysRole.getId());
					tree.setText(sysRole.getName());
					tree.setTitle(sysRole.getName());
					tree.setSpread(true);
					tree.setAttributes(sysRole.getStatus());

					tree.setChildren(findChildren(sysRoles, sysRole.getId()));
					trees.add(tree);
				}
			}
		} else {
			trees = findChildren(sysRoles, roleId);
		}
		return trees;
	}

	private List<Tree> findChildren(List<SysRole> sysRoles, Long roleId) {
		List<Tree> trees = new ArrayList<Tree>();
		for (SysRole sysRole : sysRoles) {
			if (Integer.parseInt(String.valueOf(sysRole.getPid())) == Integer.parseInt(String.valueOf(roleId))) {
				Tree tree = new Tree();
				tree.setId(sysRole.getId());
				tree.setText(sysRole.getName());
				tree.setTitle(sysRole.getName());
				tree.setSpread(true);
				tree.setAttributes(sysRole.getStatus());
				tree.setChildren(findChildren(sysRoles, sysRole.getId()));
				trees.add(tree);
			}
		}
		return trees;
	}

	@Override
	public List<Long> findChildrenId(Long roleId) {
		List<SysRole> sysRoles = findAllRoles();
		List<Long> roleIds = new ArrayList<Long>();
		// 超级管理员
		if (roleId == 1) {
			for (SysRole sysRole : sysRoles) {
				roleIds.add(sysRole.getId());
			}
			return roleIds;
		}

		for (SysRole sysRole : sysRoles) {
			if (sysRole.getPid() == roleId) {
				roleIds.add(sysRole.getId());

				List<Long> rIds = findChildrenId(sysRole.getId());
				for (Long id : rIds) {
					roleIds.add(id);
				}
			}
		}

		return roleIds;
	}

	@Override
	public List<SysRole> findChildren(Long roleId) {
		List<SysRole> sysRoles = findAllRoles();
		List<SysRole> roleChildren = new ArrayList<SysRole>();
		// 超级管理员
		if (roleId == 1) {
			return sysRoles;
		}

		for (SysRole sysRole : sysRoles) {
			if (sysRole.getPid()!=null) {
				if ((sysRole.getPid()).longValue() == roleId.longValue()) {
					roleChildren.add(sysRole);

					List<SysRole> rs = findChildren(sysRole.getId());
					for (SysRole r : rs) {
						roleChildren.add(r);
					}
				}
			}
		}

		return roleChildren;
	}

	@Override
	public void delete(Long roleId) {
		List<SysRole> roleChildren = findChildren(roleId);
		// 非超级管理员不包含自身，需要再添加
		if (roleId > 1) {
			SysRole sysRole = new SysRole();
			sysRole.setId(roleId);
			roleChildren.add(sysRole);
		}

		for (SysRole sysRole : roleChildren) {
			// 删除角色（包含下级角色）
			publicService.delete("ai_sys_role", "", "id", sysRole);

//			UserRole userRole = new UserRole();
//			userRole.setRole_id(role.getId());
//			// 删除ai_sys_user_role表
//			publicService.delete("ai_sys_user_role", "", "role_id", userRole);

			// 删除ai_sys_user_role表
			SysRoleResource sysRoleResource =new SysRoleResource();
			sysRoleResource.setRole_id(sysRole.getId());
			publicService.delete("ai_sys_role_resource", "", "role_id", sysRoleResource);
		}
	}

	@Override
	public List<SysUserRole> findUserRoleByUserId(Long userId) {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUser_id(userId);
		return publicService.selectObjs("ai_sys_user_role", "", "user_id", sysUserRole);
	}

	@Override
	public List<SysUserRole> findUserRoleByRoleId(Long roleId) {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setRole_id(roleId);
		return publicService.selectObjs("ai_sys_user_role", "", "role_id", sysUserRole);
	}

	@Override
	public List<SysRoleResource> findRoleResourceByRoleId(Long roleId) {
		SysRoleResource sysRoleResource = new SysRoleResource();
		sysRoleResource.setRole_id(roleId);
		return publicService.selectObjs("ai_sys_role_resource", "", "role_id", sysRoleResource);
	}
	
	@Override
	public List<SysRoleResource> TranfindRoleResourceByRoleId(Long roleId) {
		SysRoleResource sysRoleResource = new SysRoleResource();
		sysRoleResource.setRole_id(roleId);
		return publicService.selectObjs("ai_sys_role_resource_grant", "", "role_id", sysRoleResource);
	}

	@Override
	public List<Long> findResourceIdByRoleId(Long roleId) {
		List<Long> rIds = new ArrayList<Long>();

		List<SysRoleResource> sysRoleResources = findRoleResourceByRoleId(roleId);
		for (SysRoleResource resource : sysRoleResources) {
			Long id=resource.getResource_id();
			SysResource sysResource=new SysResource();
			sysResource.setId(id);
			sysResource=(SysResource)publicService.selectObject("ai_sys_resource","","id",sysResource);
			if ((sysResource!=null)&&((sysResource.getPid()!=-1)||(sysResource.getName().equals("修改密码")))) {
				rIds.add(id);
			}
		}
		return rIds;
	}
	
	@Override
	public List<Long> TranfindResourceIdByRoleId(Long roleId) {
		List<Long> rIds = new ArrayList<Long>();

		List<SysRoleResource> sysRoleResources = TranfindRoleResourceByRoleId(roleId);
		for (SysRoleResource resource : sysRoleResources) {
			Long id=resource.getResource_id();
			SysResource sysResource=new SysResource();
			sysResource.setId(id);
			sysResource=(SysResource)publicService.selectObject("ai_sys_resource","","id",sysResource);
			if ((sysResource!=null)&&((sysResource.getPid()!=-1)||(sysResource.getName().equals("修改密码")))) {
				rIds.add(id);
			}
		}
		return rIds;
	}

	@Override
	public void grant(Long roleId, String resourceIds) {
		// 先删除后添加
		SysRoleResource sysRoleResource = new SysRoleResource();
		sysRoleResource.setRole_id(roleId);
		publicService.delete("ai_sys_role_resource", "", "role_id", sysRoleResource);
		List<String> ids = new ArrayList<>();

		String[] resources = resourceIds.split(",");
		for (String resourceId : resources) {
			if (Integer.valueOf(resourceId) != -1) {
				SysResource sysResource = new SysResource();
				sysResource.setId(Long.valueOf(resourceId));
				sysResource = publicService.selectObj("ai_sys_resource", "", "id",sysResource);
				Long pid = sysResource.getPid();
				sysResource.setId(pid);
				sysResource = publicService.selectObj("ai_sys_resource", "", "id",sysResource);
				if (sysResource!=null) {
					if (sysResource.getPid() == null) {
						if (!(ids.contains(sysResource.getId().toString()))) {
							publicService.insert("insert into ai_sys_role_resource(role_id,resource_id) values(" + roleId + "," + sysResource.getId() + ")");
							ids.add(sysResource.getId().toString());

						}
					}
				}
			}
			if (StringUtils.isNotBlank(resourceId)) {
				if (Integer.valueOf(resourceId) != -1) {
					publicService.insert("insert into ai_sys_role_resource(role_id,resource_id) values("+roleId+","+resourceId+")");
					
				}
			}
		}
	}
	
	@Override
	public void Tran(Long roleId, String resourceIds) {
		// 先删除后添加
		SysRoleResource sysRoleResource = new SysRoleResource();
		sysRoleResource.setRole_id(roleId);
		publicService.delete("ai_sys_role_resource_grant", "", "role_id", sysRoleResource);
		//publicService.delete("");

		String[] resources = resourceIds.split(",");
		for (String resourceId : resources) {
			if (StringUtils.isNotBlank(resourceId)) {
				sysRoleResource.setRole_id(roleId);
				sysRoleResource.setResource_id(Long.parseLong(resourceId));
				publicService.insert("ai_sys_role_resource_grant", "id", "id", sysRoleResource);
				//publicService.insert("insert into ai_sys_role_resource(role_id,resource_id) select ID,"+roleResource.getId()+" from ai_sys_role where pid="+roleId);
			}
		}
	}

}
