package com.service.configmanage;

import com.commons.sql.SQLSet;
import com.commons.utils.Config;
import com.mapper.base.DataItemMapper;
import com.mapper.configmanage.ResourceMapper;
import com.model.configmange.SysResource;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysRoleResource;
import com.model.sysmanage.SysUser;
import com.model.sysmanage.SysUserRole;
import com.service.base.PublicService;
import com.commons.result.Tree;

import com.service.sysmanage.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysResourceServiceImpl implements SysResourceService {

	private static Logger LOGGER = LoggerFactory.getLogger(SysResourceServiceImpl.class);

	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private PublicService publicService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private DataItemMapper dataItemMapper;

	@Override
	public List<Tree> findTree(SysUser sysUser) {
		List<Tree> trees = new ArrayList<Tree>();
		// 超级管理
		if (sysUser.getLoginname().equals("admin")) {
			List<SysResource> resourceFather = resourceMapper.findResourceAllByTypeAndPidNull(Config.RESOURCE_GUID);
			if (resourceFather == null) {
				return null;
			}

			for (SysResource resourceOne : resourceFather) {
				Tree treeOne = new Tree();

				treeOne.setId(resourceOne.getId());
				treeOne.setText(resourceOne.getName());
				treeOne.setTitle(resourceOne.getName());
				treeOne.setSpread(true);
				treeOne.setIconCls(resourceOne.getIcon());
				treeOne.setAttributes(resourceOne.getUrl());
				List<SysResource> resourceSon = resourceMapper.findResourceAllByTypeAndPid(Config.RESOURCE_MENU,
						resourceOne.getId());

				if (resourceSon != null) {
					List<Tree> tree = new ArrayList<Tree>();
					for (SysResource resourceTwo : resourceSon) {
						Tree treeTwo = new Tree();
						treeTwo.setId(resourceTwo.getId());
						treeTwo.setText(resourceTwo.getName());
						treeTwo.setTitle(resourceTwo.getName());
						treeTwo.setSpread(true);
						treeTwo.setIconCls(resourceTwo.getIcon());
						treeTwo.setAttributes(resourceTwo.getUrl());

						List<SysResource> resourceSontwo = resourceMapper.findResourceAllByTypeAndPid(Config.RESOURCE_MENU,
								resourceTwo.getId());

						if (resourceSontwo != null) {
							List<Tree> treetwo = new ArrayList<Tree>();
							for (SysResource resourceThree : resourceSontwo) {
								Tree treeThree = new Tree();
								treeThree.setId(resourceThree.getId());
								treeThree.setText(resourceThree.getName());
								treeThree.setTitle(resourceThree.getName());
								treeThree.setSpread(true);
								treeThree.setIconCls(resourceThree.getIcon());
								treeThree.setAttributes(resourceThree.getUrl());
                                treetwo.add(treeThree);
							}
							treeTwo.setChildren(treetwo);
						}
						else {
							treeTwo.setState("closed");
						}
						tree.add(treeTwo);
					}
					treeOne.setChildren(tree);
				} else {
					treeOne.setState("closed");
				}
				trees.add(treeOne);
			}
			return trees;
		}

		// 普通用户
		// Set<Resource> resourceIdList = new HashSet<Resource>();
		List<SysResource> resourceIdLists = null;
		List<SysUserRole> roleIdList = sysRoleService.findUserRoleByUserId(sysUser.getId());
		for (SysUserRole sysUserRole : roleIdList) {
			SysRole sysRole = new SysRole();
			sysRole.setId(sysUserRole.getRole_id());
			sysRole = publicService.selectObj("ai_sys_role", "", "id", sysRole);
			if (sysRole.getStatus() == 0) {
				resourceIdLists = resourceMapper.findResourceListByRoleIdAndType(sysUserRole.getRole_id(),
						Config.RESOURCE_MENU.toString(), "", -1, -1);
			}
		}
		for (SysResource sysResource : resourceIdLists) {
			if (sysResource != null && ((sysResource.getPid() == null)||(sysResource.getPid() == -1))) {
				Tree treeOne = new Tree();
				treeOne.setId(sysResource.getId());
				treeOne.setText(sysResource.getName());
				treeOne.setIconCls(sysResource.getIcon());
				treeOne.setAttributes(sysResource.getUrl());
				List<Tree> tree = new ArrayList<Tree>();
				for (SysResource resourceTwo : resourceIdLists) {
					if (resourceTwo.getPid() != null
							&& sysResource.getId().longValue() == resourceTwo.getPid().longValue()) {
						Tree treeTwo = new Tree();
						treeTwo.setId(resourceTwo.getId());
						treeTwo.setText(resourceTwo.getName());
						treeTwo.setIconCls(resourceTwo.getIcon());
						treeTwo.setAttributes(resourceTwo.getUrl());
						tree.add(treeTwo);
					}
				}
				treeOne.setChildren(tree);
				trees.add(treeOne);
			}
		}
		return trees;
	}

	@Override
	public List<Tree> findTree(SysUser sysUser, Long id, String type) {
		List<Tree> trees = new ArrayList<Tree>();
		List<SysResource> resources = publicService.selectObjs(SQLSet.getResource(sysUser.getId(), id, type),
				SysResource.class);
//		List<Map<Object, Object>> maps = dataItemMapper.selectMultiObject("select c.* from ai_sys_user_role a left join ai_sys_role_resource b on b.role_id=a.role_id left join ai_sys_resource c on c.id=b.resource_id where a.user_id=1  and c.pid is null  and c.resourcetype=9  order by pid,seq");
		for (SysResource r : resources) {
			Tree tree = new Tree();
			tree.setId(r.getId());
			tree.setText(r.getName());
			tree.setIconCls(r.getIcon());
			tree.setAttributes(r.getUrl());
			List<Tree> treeChildren = findTree(sysUser, r.getId(), type);
			tree.setChildren(treeChildren);
			trees.add(tree);
		}
		return trees;
	}


	@Override
	public List<SysResource> findResourceAll() {
		return publicService.selectObjs("ai_sys_resource", "", "", "pid asc,seq asc", new SysResource());
	}

	@Override
	public List<SysResource> findResourceAll(Long id) {
		List<SysResource> resourceList = new ArrayList();
		String strWhere = "";
		if (id != null) {
			strWhere = String.format("id=%s or pid=%s", id.toString(), id.toString());
		} else {
			strWhere = "1>1";
		}
		List<SysResource> sysResources = publicService.selectObjs("ai_sys_resource", "", "", "pid,seq", strWhere,
				new SysResource());
		for (SysResource sysResource : sysResources) {
			resourceList.add(sysResource);
			// Long rid=Long.valueOf(resource.getId());
			if (!sysResource.getId().equals(id)) {
				strWhere = String.format("pid=%s", sysResource.getId().toString());
				List<SysResource> rs = publicService.selectObjs("ai_sys_resource", "", "", "pid,seq", strWhere,
						new SysResource());
				if (rs.size() > 0) {
					for(int i=0;i<rs.size();i++){
						resourceList.add(rs.get(i));
						strWhere = String.format("pid=%s", rs.get(i).getId());
						List<SysResource> rssid = publicService.selectObjs("ai_sys_resource", "", "", "pid,seq", strWhere,
								new SysResource());
						if (rssid.size() > 0) {
							for(int j=0;j<rssid.size();j++){
								resourceList.add(rssid.get(j));
								strWhere = String.format("pid=%s", rssid.get(j).getId());
								List<SysResource> rssided = publicService.selectObjs("ai_sys_resource", "", "", "pid,seq", strWhere,
										new SysResource());
								if (rssided.size() > 0) {
									resourceList.addAll(rssided);
								}
							}
						}
					}
				}
			}
		}
		return resourceList;
	}

	@Override
	public List<Tree> findAllTreesOne() {
		List<Tree> trees = new ArrayList<Tree>();

		// 查询所有的一级树
		// List<Resource> resources =
		// resourceMapper.findResourceAllByTypeAndPidNull(Config.RESOURCE_MENU);
		List<SysResource> sysResources = publicService.selectObjs("ai_sys_resource", "", "", "seq asc", "pid='-1' or pid is null",
				new SysResource());
		if (sysResources == null) {
			return null;
		}
		for (SysResource resourceOne : sysResources) {
			Tree treeOne = new Tree();

			treeOne.setId(resourceOne.getId());
			treeOne.setText(resourceOne.getName());
			treeOne.setTitle(resourceOne.getName());
			treeOne.setIconCls(resourceOne.getIcon());
			treeOne.setAttributes(resourceOne.getUrl());

			trees.add(treeOne);
		}
		return trees;
	}

	@Override
	public List<Tree> findAllTree() {
		List<Tree> trees = new ArrayList<Tree>();
		// 查询所有的一级树
		List<SysResource> sysResources = resourceMapper.findResourceAllByTypeAndPidNull(Config.RESOURCE_MENU);
		if (sysResources == null) {
			return null;
		}
		for (SysResource resourceOne : sysResources) {
			Tree treeOne = new Tree();

			treeOne.setId(resourceOne.getId());
			treeOne.setText(resourceOne.getName());
			treeOne.setIconCls(resourceOne.getIcon());
			treeOne.setAttributes(resourceOne.getUrl());
			// 查询所有一级树下的菜单
			List<SysResource> resourceSon = resourceMapper.findResourceAllByTypeAndPid(Config.RESOURCE_MENU,
					resourceOne.getId());

			if (resourceSon != null) {
				List<Tree> tree = new ArrayList<Tree>();
				for (SysResource resourceTwo : resourceSon) {
					Tree treeTwo = new Tree();
					treeTwo.setId(resourceTwo.getId());
					treeTwo.setText(resourceTwo.getName());
					treeTwo.setIconCls(resourceTwo.getIcon());
					treeTwo.setAttributes(resourceTwo.getUrl());
					tree.add(treeTwo);
				}
				treeOne.setChildren(tree);
			} else {
				treeOne.setState("closed");
			}
			trees.add(treeOne);
		}
		return trees;
	}

	// 查询大功能下的子功能
	@Override
	public Tree findAllTrees_resourceSon(int regroup, Tree treeOne, int Roleid) {
		List<SysResource> resourceSon = resourceMapper.findResourceSonByregroup(regroup,
				Config.RESOURCE_MENU.toString(), -1, Roleid);
		List<Tree> treeTwoList = new ArrayList<Tree>();
		for (SysResource resourceTwo : resourceSon) {
			Tree treeTwo = new Tree();

			treeTwo.setId(resourceTwo.getId());
			treeTwo.setText(resourceTwo.getName());
			treeTwo.setTitle(resourceTwo.getName());
			treeTwo.setSpread(true);
			treeTwo.setIconCls(resourceTwo.getIcon());
			treeTwo.setAttributes(resourceTwo.getUrl());

			int pid = treeTwo.getId().intValue();
			List<SysResource> resourceSons = resourceMapper.findResourceSonByregroup(regroup,
					Config.RESOURCE_MENU.toString(), pid, Roleid);
			List<Tree> treeThreeList = new ArrayList<Tree>();

			for (SysResource resourceThree : resourceSons) {
				Tree treeThree = new Tree();

				treeThree.setId(resourceThree.getId());
				treeThree.setText(resourceThree.getName());
				treeThree.setTitle(resourceThree.getName());
				treeThree.setSpread(true);
				treeThree.setIconCls(resourceThree.getIcon());
				treeThree.setAttributes(resourceThree.getUrl());
				pid = treeThree.getId().intValue();
				List<SysResource> resourceSonbts = resourceMapper.findResourceSonByregroup(regroup,
						Config.RESOURCE_BUTTON.toString(), pid, Roleid);
				List<Tree> treeFourList = new ArrayList<Tree>();

				for (SysResource resourceFour : resourceSonbts) {
					Tree treeFour = new Tree();

					treeFour.setId(resourceFour.getId());
					treeFour.setText(resourceFour.getName());
					treeFour.setTitle(resourceFour.getName());
					treeFour.setSpread(true);
					treeFour.setIconCls(resourceFour.getIcon());
					treeFour.setAttributes(resourceFour.getUrl());
					treeFourList.add(treeFour);
				}
				treeThree.setChildren(treeFourList);
				treeThreeList.add(treeThree);
			}
			treeTwo.setChildren(treeThreeList);
			treeTwoList.add(treeTwo);
		}
		treeOne.setChildren(treeTwoList);
		return treeOne;
	}

	//查询所有下级节点
	@Override
	public Tree findAllTrees_resourceSonEX(Tree treeOne, int Roleid) {
		List<SysResource> resourceSon = resourceMapper.findResourceSon(
				Config.RESOURCE_GUID.toString(), -1, Roleid);
		List<Tree> treeTwoList = new ArrayList<Tree>();
		for (SysResource resourceTwo : resourceSon) {
			Tree treeTwo = new Tree();

			treeTwo.setId(resourceTwo.getId());
			treeTwo.setText(resourceTwo.getName());
			treeTwo.setTitle(resourceTwo.getName());
			treeTwo.setSpread(true);
			treeTwo.setIconCls(resourceTwo.getIcon());
			treeTwo.setAttributes(resourceTwo.getUrl());

			int pid = treeTwo.getId().intValue();
			List<SysResource> resourceSons = resourceMapper.findResourceSon(
					Config.RESOURCE_MENU.toString(), pid, Roleid);
			List<Tree> treeThreeList = new ArrayList<Tree>();

			for (SysResource resourceThree : resourceSons) {
				Tree treeThree = new Tree();

				treeThree.setId(resourceThree.getId());
				treeThree.setText(resourceThree.getName());
				treeThree.setTitle(resourceThree.getName());
				treeThree.setSpread(true);
				treeThree.setIconCls(resourceThree.getIcon());
				treeThree.setAttributes(resourceThree.getUrl());
				pid = treeThree.getId().intValue();
				List<SysResource> resourceSonsmemus = resourceMapper.findResourceSon(
						Config.RESOURCE_MENU.toString(), pid, Roleid);
				List<Tree> treeFourList = new ArrayList<Tree>();

				for (SysResource resourceFour : resourceSonsmemus) {
					Tree treeFour = new Tree();

					treeFour.setId(resourceFour.getId());
					treeFour.setText(resourceFour.getName());
					treeFour.setTitle(resourceFour.getName());
					treeFour.setSpread(true);
					treeFour.setIconCls(resourceFour.getIcon());
					treeFour.setAttributes(resourceFour.getUrl());
					pid = treeFour.getId().intValue();
					List<SysResource> resourceSonbts = resourceMapper.findResourceSon(
							Config.RESOURCE_BUTTON.toString(), pid, Roleid);
					List<Tree> treeFiveList = new ArrayList<Tree>();
					for (SysResource resourceFive : resourceSonbts) {
						Tree treeFive = new Tree();

						treeFive.setId(resourceFive.getId());
						treeFive.setText(resourceFive.getName());
						treeFive.setTitle(resourceFive.getName());
						treeFive.setSpread(true);
						treeFive.setIconCls(resourceFive.getIcon());
						treeFive.setAttributes(resourceFive.getUrl());
						treeFiveList.add(treeFive);
					}
					treeFour.setChildren(treeFiveList);
					treeFourList.add(treeFour);
				}
				treeThree.setChildren(treeFourList);
				treeThreeList.add(treeThree);
			}
			treeTwo.setChildren(treeThreeList);
			treeTwoList.add(treeTwo);
		}
		treeOne.setChildren(treeTwoList);
		return treeOne;
	}

	// 访问授权树
	@Override
	public List<Tree> findAllTrees(Long roleId) {
		List<Tree> treeOneList = new ArrayList<Tree>();

		// 根据roleID给定一级树
		SysRole sysRoles = resourceMapper.findresourceOneByRoleId(roleId);
		int ParentRole = sysRoles.getPid().intValue();
		Tree treeCommon = new Tree();
		treeCommon.setId(Long.parseLong("-1"));
		treeCommon.setText("监管平台");
		treeCommon.setTitle("监管平台");
		treeCommon.setSpread(true);
		treeCommon.setIconCls("icon-sz");
		treeCommon.setAttributes(null);
		Tree commonTree = findAllTrees_resourceSonEX( treeCommon, -1);
		if (commonTree.getChildren().size() > 0) {
			treeOneList.add(commonTree);
		}

		// zengxj 2016-04-13 增加按钮级，没有父节点的按钮资料（比如修改密码）
		/********************* zengxj 2016-04-13 ****************/
		List<SysResource> resourceButtonSource = resourceMapper.findResourceAllByTypeAndPidNull(Config.RESOURCE_BUTTON);

		if (resourceButtonSource != null) {
			// List<Tree> treeThreeList = Lists.newArrayList();

			for (SysResource resourceThree : resourceButtonSource) {
				Tree treeThree = new Tree();

				treeThree.setId(resourceThree.getId());
				treeThree.setText(resourceThree.getName());
				treeThree.setSpread(true);
				treeThree.setTitle(resourceThree.getName());
				treeThree.setIconCls(resourceThree.getIcon());
				treeThree.setAttributes(resourceThree.getUrl());
				treeOneList.add(treeThree);
			}
		}
		/***************************************************/

		return treeOneList;
	}

	@Override
	public Tree findAllTrees_resourceSon_grant(int regroup, Tree treeOne, int ParentRole) {
		List<SysResource> resourceSon = resourceMapper.findResourceSonByregroupForGrant(regroup,
				Config.RESOURCE_MENU.toString(), ParentRole);
		List<Tree> treeTwoList = new ArrayList<Tree>();
		for (SysResource resourceTwo : resourceSon) {
			Tree treeTwo = new Tree();

			treeTwo.setId(resourceTwo.getId());
			treeTwo.setText(resourceTwo.getName());
			treeTwo.setIconCls(resourceTwo.getIcon());
			treeTwo.setAttributes(resourceTwo.getUrl());

			List<SysResource> resourceSons = resourceMapper.findResourceButtonByregroupForGrant(
					resourceTwo.getId().toString(), Config.RESOURCE_BUTTON.toString());
			List<Tree> treeThreeList = new ArrayList<Tree>();

			for (SysResource resourceThree : resourceSons) {
				Tree treeThree = new Tree();

				treeThree.setId(resourceThree.getId());
				treeThree.setText(resourceThree.getName());
				treeThree.setIconCls(resourceThree.getIcon());
				treeThree.setAttributes(resourceThree.getUrl());
				treeThreeList.add(treeThree);
			}
			treeTwo.setChildren(treeThreeList);
			treeTwoList.add(treeTwo);
		}
		treeOne.setChildren(treeTwoList);
		return treeOne;
	}

	// 传播授权树
	@Override
	public List<Tree> findAllTransmissionTrees(Long roleId) {
		List<Tree> treeOneList = new ArrayList<Tree>();

		// 根据roleID给定一级树
		SysRole sysRoles = resourceMapper.findresourceOneByRoleId(roleId);
		int ParentRole = sysRoles.getPid().intValue();
		Tree treeOne = new Tree();
		Tree treeCommon = new Tree();
		treeCommon.setId(Long.parseLong("-1"));
		treeCommon.setText("通用模块");
		treeCommon.setIconCls("icon-sz");
		treeCommon.setAttributes(null);
		Tree commonTree = findAllTrees_resourceSon_grant(0, treeCommon, -1);
		treeOneList.add(commonTree);

		Tree treextsz = new Tree();
		treextsz.setId(Long.parseLong("-1"));
		treextsz.setText("系统管理模块");
		treextsz.setIconCls("icon-chevron");
		treextsz.setAttributes(null);
		Tree xtglTree = findAllTrees_resourceSon_grant(1, treextsz, ParentRole);
		treeOneList.add(xtglTree);

		Tree treeywgl = new Tree();
		treeywgl.setId(Long.parseLong("-1"));
		treeywgl.setText("业务管理模块");
		treeywgl.setIconCls("icon-chevron");
		treeywgl.setAttributes(null);
		Tree ywglTree = findAllTrees_resourceSon_grant(2, treeywgl, ParentRole);
		treeOneList.add(ywglTree);

		Tree treesjgl = new Tree();
		treesjgl.setId(Long.parseLong("-1"));
		treesjgl.setText("审计管理模块");
		treesjgl.setIconCls("icon-chevron");
		treesjgl.setAttributes(null);
		Tree sjglTree = findAllTrees_resourceSon_grant(3, treesjgl, ParentRole);
		treeOneList.add(sjglTree);

		Tree treesafe = new Tree();
		treesafe.setId(Long.parseLong("-1"));
		treesafe.setText("安全管理模块");
		treesafe.setIconCls("icon-chevron");
		treesafe.setAttributes(null);
		Tree safeTree = findAllTrees_resourceSon_grant(4, treesafe, ParentRole);
		treeOneList.add(safeTree);

		// zengxj 2016-04-13 增加按钮级，没有父节点的按钮资料（比如修改密码）
		/********************* zengxj 2016-04-13 ****************/
		List<SysResource> resourceButtonSource = resourceMapper.findResourceAllByTypeAndPidNull(Config.RESOURCE_BUTTON);

		if (resourceButtonSource != null) {
			// List<Tree> treeThreeList = Lists.newArrayList();

			for (SysResource resourceThree : resourceButtonSource) {
				Tree treeThree = new Tree();

				treeThree.setId(resourceThree.getId());
				treeThree.setText(resourceThree.getName());
				treeThree.setIconCls(resourceThree.getIcon());
				treeThree.setAttributes(resourceThree.getUrl());
				treeOneList.add(treeThree);
			}
		}
		/***************************************************/

		return treeOneList;
	}

	@Override
	public SysResource findResourceById(Long id) {
		SysResource sysResource = new SysResource();
		sysResource.setId(id);
		return publicService.selectObj("ai_sys_resource", "", "id", sysResource);
	}

	@Override
	public void deleteResourceById(Long id) {
		SysResource sysResource = new SysResource();
		SysRoleResource sysRoleResource = new SysRoleResource();

		sysResource.setId(id);
		publicService.delete("ai_sys_resource", "", "id", sysResource);

		sysRoleResource.setResource_id(id);
		publicService.delete("ai_sys_role_resource", "", "resource_id", sysRoleResource);

		deleteResourceByPid(id);
	}

	private void deleteResourceByPid(Long pid) {
		SysResource sysResource = new SysResource();
		sysResource.setPid(pid);
		List<SysResource> sysResources = publicService.selectObjs("ai_sys_resource", "", "pid", sysResource);

		for (SysResource r : sysResources) {
			publicService.delete("ai_sys_resource", "", "id", r);

			SysRoleResource sysRoleResource = new SysRoleResource();
			sysRoleResource.setResource_id(r.getId());
			publicService.delete("ai_sys_role_resource", "", "resource_id", sysRoleResource);

			deleteResourceByPid(r.getId());
		}
	}

	@Override
	public void insert(SysResource sysResource) {
		SysRoleResource sysRoleResource = new SysRoleResource();

		// 新建资源
		publicService.insert("ai_sys_resource", "id", "", sysResource);

		sysResource = publicService.selectObj("ai_sys_resource", "", "name,url,resourcetype,pid,seq", sysResource);

		if (sysResource != null) {
			// 默认将新增资源权限分配给超级管理员
			sysRoleResource.setRole_id(Long.valueOf(1));
			sysRoleResource.setResource_id(sysResource.getId());
			publicService.insert("ai_sys_role_resource", "id", "", sysRoleResource);
		}
	}

}
