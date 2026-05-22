package com.controller.sysmanage;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.logmange.SysLogInfo;
import com.model.sysmanage.SysOrganization;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysUser;
import com.model.sysmanage.SysUserRole;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysOrganizationService organService;

	@Autowired
	private PublicService publicService;



	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));

		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
	}

	/**
	 * 用户管理页
	 *
	 * @return
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String manager(HttpServletRequest request) {
		return "sysmanage/sysUser";
	}

	/**
	 * 首页初始化
	 *
	 * @return
	 */
	@RequestMapping(value = "/init")
	@ResponseBody
	public Object init(HttpServletRequest request) {
		SysUser sysUser = getCurrentUser();
		long yhdqdays = (sysUser.getZhyxq().getTime() - new Date().getTime()) / (24 * 60 * 60 * 1000);
		sysUser.setYhdqdays(String.valueOf(yhdqdays));
		return renderSuccess(sysUser);
	}

	/**
	 * 首页初始化历史信息
	 *
	 * @return
	 */
	@RequestMapping(value = "/history")
	@ResponseBody
	public Object history(HttpServletRequest request) {
		SysUser sysUser = getCurrentUser();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String scdlsj = sdf.format(sysUser.getScdlsj());
		List<SysLogInfo> loginfos = (List<SysLogInfo>)(List) publicService.selectObjects(
				"SELECT num_id,user_id,organization,user_name,terminal_id,operate_type,operate_time,operate_condition,operate_result,terminal_type,jyw FROM AI_SYS_LOG_INFO WHERE user_id='"
						+ sysUser.getId() + "' and operate_type='0' and operate_result='0' and operate_time>'" + scdlsj
						+ "' order by operate_time desc",
				SysLogInfo.class);
		if (loginfos == null) {
			return renderError("无对应数据记录");
		} else {
			return renderSuccess(loginfos);
		}
	}

	/**
	 * 用户管理列表
	 *
	 * @param sysUser
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/dataGrid", method = RequestMethod.POST)
	@ResponseBody
	public Object dataGrid(SysUser sysUser, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		String strWhere = "";

		if (StringUtils.isNoneBlank(sysUser.getOrgan())) {
			strWhere += String.format(" and organ='%s'",  sysUser.getOrgan());
		} else {
			String strCurrentOrgan = getCurrentUserOrganCode();
			String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
			if (StringUtils.isNoneBlank(strOrgans)) {
				strOrgans = strCurrentOrgan + "," + strOrgans;
				strOrgans = strOrgans.replace(",", "','");
				strWhere += String.format(" and organ in ('%s')",  strOrgans);
			} else {
				strWhere += String.format(" and organ='%s'",  strCurrentOrgan);
			}

		}
		// 获取当前账号角色所有子角色的用户id，然后作为查询用户表的条件
		List<Long> roleids = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
		String strRoleid = "";
		String strUserid = "";
		if (roleids != null) {
			for (Long roleid : roleids) {
				if (strRoleid.equals("")) {
					strRoleid = roleid.toString();
				} else {
					strRoleid = strRoleid + "," + roleid;
				}
			}
			strRoleid = strRoleid.replace(",", "','");
			List<SysUserRole> sysUserRoles = publicService.selectObjs("ai_sys_user_role", "password", "role_id", "role_id",
					" role_id in ('" + strRoleid + "')", new SysUserRole());
			if (sysUserRoles != null) {
				for (SysUserRole sysUserRole : sysUserRoles) {
					if (strUserid.equals("")) {
						strUserid = sysUserRole.getUser_id().toString();
					} else {
						strUserid = strUserid + "," + sysUserRole.getUser_id().toString();
					}
				}
				strUserid = strUserid.replace(",", "','");
			}
			if (!strUserid.equals("")) {
				strWhere += String.format(" and id in ('%s')", strUserid);
			}
		}
		if (StringUtils.isNoneBlank(sysUser.getLoginname())) {
			strWhere += String.format(" and loginname like '%%%s%%'",
					sysUser.getName());
		}
		// publicService.selectPageObjects(pageInfo, "ai_sys_user", "password",
		// "",
		// strWhere, new SysUser());
		String field = "id,sfzhm,loginname,name,organ organ,sex,phone,usertype,status,createdate,jyw";
		publicService.selectPageObjects(pageInfo, "ai_sys_user", field, strWhere, "organ");
		publicService.insertLogInfo("系统管理中的用户管理,查询了ai_sys_user", "查询", "1","用户管理");
		return pageInfo;
	}

	/**
	 * 添加用户页
	 *
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public String addPage(HttpServletRequest request) {
		request.setAttribute("currentuser", this.getCurrentUser());
		request.setAttribute("oper", "add");
		return "sysmanage/sysUserEdit";
	}

	/**
	 * 添加用户
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(SysUser sysUser) {
		List<String> bindResult = new ArrayList<String>();

		try {
			sysUser.setCreatedate(new Date());
			UUID uuid = UUID.randomUUID();
			String uuids = uuid.toString().replaceAll("-", "");
			sysUser.setUserid(uuids);

			bindResult = ValidateService.valid(sysUser);

			if (StringUtils.isBlank(sysUser.getPassword())) {
				bindResult.add("密码：不允许为空");
			}
			if (bindResult.size() == 0) {
				// 验证用户是否已存在
				SysUser u = sysUserService.findUserByLoginName(sysUser.getLoginname());
				if (u != null) {
					bindResult.add("登录名：已存在，不允许重复创建");
				}
				if (bindResult.size() == 0) {
					sysUser.setPassword(
							DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid()+sysUser.getLoginname() + sysUser.getPassword())));
					// 权限限制
					List<Long> roleids = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
					if (roleids.contains(sysUser.getRole_id())) {
						sysUserService.addUser(sysUser);

						publicService.insertLogInfo("系统管理的用户管理中，点击编辑，成功修改【" + "组织机构："
								+ organService.findOrganizationsByCode(sysUser.getOrgan()) + ",用户名："
								+ sysUser.getLoginname() + "】的登录用户信息", "新增", "1","用户管理");

					} else {
						bindResult.add("权限不足,不允许创建该用户");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo(
					"系统管理的用户管理中，点击新建，新建失败【" + "组织机构：" + organService.findOrganizationsByCode(sysUser.getOrgan())
							+ ",用户名：" + sysUser.getLoginname() + "】的新登录用户",
					"新增", "0","用户管理");
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("新建成功！") : renderError(bindResult.toString());
	}

	/**
	 * 编辑用户页
	 * 
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(Long id, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		SysUser sysUser = sysUserService.findUserById(id);
		request.setAttribute("userinfo", sysUser);
		request.setAttribute("currentuser", this.getCurrentUser());
		request.setAttribute("oper", "edit");
		SysOrganization sysOrganization=organService.findOrganizationByCode(sysUser.getOrgan());
		SysRole sysRole= sysRoleService.findRoleById(sysUser.getRole_id());
		request.setAttribute("organname",sysOrganization.getJc());
		request.setAttribute("rolename",sysRole.getName());
		request.setAttribute("yhLimitDate", sdf.format(null == sysUser.getZhyxq() ? new Date() : sysUser.getZhyxq()));

		return "sysmanage/sysUserEdit";
	}

	/**
	 * 编辑用户
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(SysUser sysUser) {
		List<String> bindResult = new ArrayList<String>();

		try {
			if (sysUser.getUserid()==null)
			{
				UUID uuid = UUID.randomUUID();
				String uuids = uuid.toString().replaceAll("-", "");
				sysUser.setUserid(uuids);
			}
			bindResult = ValidateService.valid(sysUser);
			if (bindResult.size() == 0) {
				if (StringUtils.isNoneBlank(sysUser.getPassword())) {
					sysUser.setPassword(
							DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid()+sysUser.getLoginname() + sysUser.getPassword())));
				} else {
					sysUser.setPassword(null);
				}
				// 权限限制
				List<Long> roleids = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
				if (roleids.contains(sysUser.getRole_id())) {
					SysUser u1 = publicService.selectObj("ai_sys_user", "", "id", sysUser);
					if (u1!=null)
					{
						if (u1.getZhyxq()!=null)
						{
							Date now = new Date();
							if ((sysUser.getZhyxq().after(u1.getZhyxq())&(now.after(u1.getZhyxq()))))
							{
								//过期账号激活需重置密码
								sysUser.setPassword(
										DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid()+sysUser.getLoginname() + "888888")));
								bindResult.add("延长有效期密码重置为888888");
								
							}
								
						}
					}
					sysUserService.updateUser(sysUser);
					if (u1!=null)
					{
						if (u1.getStatus()!=null)
						{
							if ((u1.getStatus()==1)&(sysUser.getStatus()==0))
							{
								sysUser.setErrortimes(0);
								sysUserService.updateUser(sysUser);
							}
								
						}
					}
					
					publicService.insertLogInfo(
							"系统管理的用户管理中，点击编辑，成功修改【" + "组织机构：" + organService.findOrganizationsByCode(sysUser.getOrgan())
									+ ",用户名：" + sysUser.getLoginname() + "】的登录用户信息",
							"修改", "1","用户管理");

				} else {
					bindResult.add("权限不足,不允许编辑该用户");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo(
					"系统管理的用户管理中，点击编辑，修改失败【" + "组织机构：" + organService.findOrganizationsByCode(sysUser.getOrgan())
							+ ",用户名：" + sysUser.getLoginname() + "】的登录用户信息,异常:" + e.getMessage(),
					"修改", "0","用户管理");
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("修改成功！") : renderError(bindResult.toString());
	}

	/**
	 * 修改密码页
	 *
	 * @return
	 */
	@RequestMapping("/editPwdPage")
	public String editPwdPage() {
		return "sysmanage/userEditPwd";
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPwd
	 * @param pwd
	 * @return
	 */
	@RequestMapping("/editUserPwd")
	@ResponseBody
	public Object editUserPwd(String oldPwd, String pwd) {
		SysUser currentUser = getCurrentUser();
		try {
			if (!currentUser.getPassword()
					.equals(DigestUtils.md5Hex(DigestUtils.md5Hex(currentUser.getUserid()+currentUser.getLoginname() + oldPwd)))) {
				return renderError("旧密码不正确!");
			}
			currentUser.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(currentUser.getUserid()+currentUser.getLoginname() + pwd)));
			publicService.update("ai_sys_user", "id", "id", "loginname,name,organ", "", currentUser);
			return renderSuccess("密码修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("密码修改异常：" + e.getMessage());
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(Long id) {
		try {
			// date:2018.03.06 author:Xukz description:限制删除权限
			List<Long> roleids = sysRoleService.findChildrenId(this.getCurrentUserRoleId());
			SysUserRole userrole = new SysUserRole();
			userrole = (SysUserRole) publicService
					.selectObj("select ROLE_ID from ai_sys_user_role where USER_ID='" + id + "'", SysUserRole.class);
			if (userrole == null) {
				return renderError("该用户无对应的ROLE_ID!");
			} else {
				if (roleids.contains(userrole.getRole_id())) {// 权限符合要求的可删除
					System.out.println("666666");
					SysUser sysUser = new SysUser();
					sysUser.setId(id);
					sysUser = publicService.selectObj("ai_sys_user", "", "id", sysUser);
					sysUserService.deleteUserById(id);
					publicService.insertLogInfo(
							"系统管理的用户管理中，点击删除，成功删除【" + "组织机构：" + organService.findOrganizationsByCode(sysUser.getOrgan())
									+ ",用户名：" + sysUser.getLoginname() + "】的登录用户",
							"删除", "1","用户管理");
					return renderSuccess("删除成功！");
				} else {
					publicService.insertLogInfo("系统管理的用户管理中，点击删除，删除用户ID为" + id + "的用户，删除失败,异常:权限不符合要求", "删除", "0","用户管理");
					return renderError("删除失败！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			publicService.insertLogInfo("系统管理的用户管理中，点击删除，删除用户ID为" + id + "的用户，删除失败,异常:" + e.getMessage(), "删除", "0","用户管理");

			return renderError("删除异常：" + e.getMessage());
		}
	}

	@RequestMapping(value = "/basedata/{param}")
	@ResponseBody
	public Object getBaseData(@PathVariable String param) {
		return sysUserService.findBaseData(param);
	}
}
