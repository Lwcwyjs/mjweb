package com.controller.local;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.local.YuYue;
import com.model.sysmanage.SysOrganization;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping("/local/yuyue")
public class YyController extends BaseController {

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
		return "local/yuYue";
	}


	/**
	 * 用户管理列表
	 *
	 * @param yuYue
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/dataGrid", method = RequestMethod.POST)
	@ResponseBody
	public Object dataGrid(YuYue yuYue, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		String strWhere = "";

		if (StringUtils.isNoneBlank(yuYue.getOrgan())) {
			strWhere += String.format(" and organ='%s'",  yuYue.getOrgan());
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
		if (StringUtils.isNoneBlank(yuYue.getLicenseplate())) {
			strWhere += String.format(" and LicensePlate like '%%%s%%'",
					yuYue.getLicenseplate());
		}
		if (StringUtils.isNoneBlank(yuYue.getLicenseplatecolor())) {
			strWhere += String.format(" and LicensePlateColor like '%%%s%%'",
					yuYue.getLicenseplatecolor());
		}
		String tblName = "(select dataid,d.name as organ,a.licensePlate,licensePlateColor,a.begintime,a.endtime,a.cjsj,getecode " +
				" from (select * from yuyue  where 1=1 " + strWhere + ")a " +
				" left join (select * from ai_sys_organization) d on a.organ = d.organ)a";
		logger.info(tblName);
		publicService.selectPageObjects(pageInfo, tblName, "*", "", "cjsj desc");
		return pageInfo;
	}

	/**
	 * 添加用户页
	 *
	 * @return
	 */
	@RequestMapping(value = "/addPage", method = RequestMethod.GET)
	public String addPage(HttpServletRequest request) {
		request.setAttribute("oper", "add");
		return "local/yuYueEdit";
	}

	/**
	 * 添加用户
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(YuYue yuYue) {
		List<String> bindResult = new ArrayList<String>();

		try {
			yuYue.setCjsj(new Date());
			UUID uuid = UUID.randomUUID();
			String uuids = uuid.toString().replaceAll("-", "");
			yuYue.setDataid(uuids);

			bindResult = ValidateService.valid(yuYue);

			if (bindResult.size() == 0) {
				// 验证用户是否已存在
				YuYue yuyue = publicService.selectObj("yuyue","","licensePlate,licensePlateColor,begintime",yuYue);
				if (yuyue != null) {
					bindResult.add("该车已存在，不能重复创建");
				}
				if (bindResult.size() == 0) {
					publicService.insert("yuyue","","",yuYue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	public String editPage(String id, HttpServletRequest request) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		YuYue yuYue = publicService.selectObj("select * from yuyue where dataid='"+id+"'",YuYue.class);
		request.setAttribute("oper", "edit");
		request.setAttribute("yuyue", yuYue);
		SysOrganization sysOrganization=organService.findOrganizationByCode(yuYue.getOrgan());
		request.setAttribute("organname",sysOrganization.getJc());
		request.setAttribute("begintime", sdf.format(null == yuYue.getBegintime() ? new Date() : yuYue.getBegintime()));
		request.setAttribute("endtime", sdf.format(null == yuYue.getEndtime() ? new Date() : yuYue.getEndtime()));
		return "local/yuYueEdit";
	}

	/**
	 * 编辑用户
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Object edit(YuYue yuYue) {
		List<String> bindResult = new ArrayList<String>();

		try {
			bindResult = ValidateService.valid(yuYue);
			if (bindResult.size() == 0) {
				publicService.update("yuyue","","id",yuYue);
			}

		} catch (Exception e) {
			e.printStackTrace();
			bindResult.add("异常：" + e.getMessage());
		}

		return bindResult.size() == 0 ? renderSuccess("修改成功！") : renderError(bindResult.toString());
	}

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(String id) {
		try {
			publicService.delete("delete from yuyue where id='"+id+"'");
			return renderSuccess("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return renderError("删除异常：" + e.getMessage());
		}
	}
}
