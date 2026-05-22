package com.controller.configmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.service.configmanage.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.commons.base.BaseController;
import com.commons.result.Tree;
import com.commons.utils.PageInfo;
import com.commons.utils.StringUtils;
import com.model.sysmanage.SysOrganization;
import com.model.configmange.SysOptions;
import com.service.base.PublicService;

@Controller
@RequestMapping("/admin/cysysoptions")
public class SysOptionsController extends BaseController {

	@Autowired
	@Resource
	private PublicService publicService;

	@Resource
	private ConfigService configService;

	@RequestMapping("/manager")
	public String manager() {
		return "configmanage/sysOptions";
	}

	/**
	 * 左侧配置类别资源树
	 *
	 * @return
	 */
	@RequestMapping(value = "/tree", method = RequestMethod.POST)
	@ResponseBody
	public Object tree() {
		List<Tree> trees = new ArrayList<Tree>();
		List<Tree> roots = new ArrayList<Tree>();
		Tree roottree = new Tree();
		roottree.setText("配置类别");
		roottree.setCode("配置类别");
		roottree.setIconCls("icon-company");
		SysOptions sysOptionsx = new SysOptions();
		sysOptionsx.setOption_kind("配置类别");
		List<SysOptions> sysOptionsses = findcySysOptionsAll(sysOptionsx, "option_kind");
		if (sysOptionsses != null) {
			for (Object obj : sysOptionsses) {
				SysOptions sysOptions = (SysOptions) obj;
				Tree tree = new Tree();
				tree.setPcode(sysOptions.getOption_value());
				tree.setText(sysOptions.getOption_value());
				tree.setCode(sysOptions.getOption_value());
				tree.setIconCls("icon-folder");
				roots.add(tree);
			}
		}
		roottree.setChildren(roots);
		trees.add(roottree);
		return trees;
	}

	/**
	 * 分页显示
	 *
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Object dataGrid(SysOptions sysOptions, Integer page, Integer rows, String sort, String order) {

		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		String strWhere = "";
		if (!StringUtils.isBlank(sysOptions.getOption_kind())) {
			strWhere += String.format(" and option_kind like '%%%s%%'", sysOptions.getOption_kind());
		}
		if (!StringUtils.isBlank(sysOptions.getOption_value())) {
			strWhere += String.format(" and option_value  like '%%%s%%'",
					sysOptions.getOption_value());
		}
		String fields = "option_kind,option_des,option_value,id,option_organ,option_params,status";
		publicService.insertLogInfo("配置管理中的系统配置管理,查询了AI_SYS_OPTIONS", "查询", "1","系统参数管理");
		publicService.selectPageObjects(pageInfo, "AI_SYS_OPTIONS", fields, strWhere, "option_kind,option_value");
		return pageInfo;
	}

	/**
	 * 添加系统配置页
	 *
	 * @return
	 */
	@RequestMapping(value = "/addPage")
	public String addPage() {
		return "configmanage/sysOptionsAdd";
	}

	/**
	 * 添加系统配置
	 *
	 * @param sysOptions
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public Object add(SysOptions sysOptions) {
		if (findcySysOptionsAll(sysOptions, "option_kind,option_des").size() == 0) {
			String status = null;
			try {
				UUID uuid = UUID.randomUUID();
				String uuids = uuid.toString().replaceAll("-", "");
				sysOptions.setId(uuids);
				SysOptions sysOptionsex =new SysOptions();
				sysOptionsex =publicService.selectObj("select * from AI_SYS_OPTIONS where option_value='"+ sysOptions.getOption_value()+"'", SysOptions.class);
				if (sysOptionsex !=null){
					return renderError("已有此代码,不可重复添加");
				}
				publicService.insert("AI_SYS_OPTIONS", "", "", "option_value", sysOptions);

				if (sysOptions.getStatus().equals("1")) {
					status = "观察";
				} else if (sysOptions.getStatus().equals("0")) {
					status = "停用";
				}else if (sysOptions.getStatus().equals("2")) {
					status = "回写";
				}

				publicService.insertLogInfo("配置管理的系统配置管理中，点击新建，成功添加【" + "配置类别：" + sysOptions.getOption_kind()
						+ ",配置项代码：" + sysOptions.getOption_value() + ",配置项：" + sysOptions.getOption_des() + ",配置参数："
						+ sysOptions.getOption_params() + ",状态：" + status + "】的系统配置", "添加", "1","系统参数管理");
				return renderSuccess("添加成功");
			} catch (RuntimeException e) {
				publicService.insertLogInfo(
						"配置管理的系统配置管理中，点击新建，添加失败【" + "配置类别：" + sysOptions.getOption_kind() + ",配置项代码："
								+ sysOptions.getOption_value() + ",配置项：" + sysOptions.getOption_des() + ",配置参数："
								+ sysOptions.getOption_params() + ",状态：" + status + "】的系统配置,异常:" + e.getMessage(),
						"添加", "0","系统参数管理");
				return renderError(e.getMessage());
			}
		} else {
			return renderError("该项配置项目已存在，再次添加失败");
		}
	}

	/**
	 * 修改系统配置页
	 *
	 * @return
	 */
	@RequestMapping(value = "/editPage", method = RequestMethod.GET)
	public String editPage(HttpServletRequest request, String ID) {
		SysOptions sysOptions = new SysOptions();
		sysOptions.setId(ID);
		sysOptions = (SysOptions) cySysOptions(sysOptions);

		request.setAttribute("cysysoptions", sysOptions);
		request.setAttribute("oper", "edit");
		String name = sysOptions.getOption_kind();
		return "configmanage/sysOptionsEdit";

	}

	/**
	 * 编辑系统配置
	 *
	 * @param sysOptions
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public Object edit(SysOptions sysOptions) {
		String status = null;
		try {
			if (sysOptions.getOption_params() != null) {
			} else {
				sysOptions.setOption_params("");
			}
			if (sysOptions.getOption_organ() != null) {
			} else {
				sysOptions.setOption_organ("");
			}
			SysOptions sysOptionsex =new SysOptions();
			sysOptionsex =publicService.selectObj("select * from AI_SYS_OPTIONS where option_value='"+ sysOptions.getOption_value()+"' and id<>'"+ sysOptions.getId()+"'", SysOptions.class);
			if (sysOptionsex !=null){
				return renderError("已有此代码,不可重复配置");
			}
			publicService.update("AI_SYS_OPTIONS", "ID", "ID", "option_value", "", sysOptions);

			if (sysOptions.getStatus().equals("1")) {
				status = "观察";
			} else if (sysOptions.getStatus().equals("0")) {
				status = "停用";
			}else if (sysOptions.getStatus().equals("2")) {
				status = "回写";
			}
			publicService.insertLogInfo("配置管理的系统配置管理中，点击编辑，成功修改【" + "配置类别：" + sysOptions.getOption_kind() + ",配置项代码："
					+ sysOptions.getOption_value() + ",配置项：" + sysOptions.getOption_des() + ",配置参数："
					+ sysOptions.getOption_params() + ",状态：" + status + "】的系统配置", "修改", "1","系统参数管理");
			return renderSuccess("修改成功");
		} catch (RuntimeException e) {
			publicService.insertLogInfo(
					"配置管理的系统配置管理中，点击编辑，修改失败【" + "配置类别：" + sysOptions.getOption_kind() + ",配置项代码："
							+ sysOptions.getOption_value() + ",配置项：" + sysOptions.getOption_des() + ",配置参数："
							+ sysOptions.getOption_params() + ",状态：" + status + "】的系统配置,异常:" + e.getMessage(),
					"修改", "0","系统参数管理");
			return renderError(e.getMessage());
		}
	}
	/**
	 * 获取照片列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/zpzlcombox", method = RequestMethod.POST)
	@ResponseBody
	public Object zpzlcombox(HttpServletRequest request) {
		SysOptions sysOptions = new SysOptions();
		List<SysOptions> sysOptionsList =  publicService.selectObjs("select option_value,status,option_des " +
				" from AI_SYS_OPTIONS where option_kind='照片参数' and (status='1' or status='2') order by option_value", SysOptions.class);
		return JSON.toJSONString(sysOptionsList);
	}
	/**
	 * 获取识别项列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/bdcscombox", method = RequestMethod.POST)
	@ResponseBody
	public Object bdcscombox(HttpServletRequest request) {
		SysOptions sysOptions = new SysOptions();
		List<SysOptions> sysOptionsList =  publicService.selectObjs("select option_value,status,option_des " +
				" from AI_SYS_OPTIONS where option_kind='比对参数' and (status='1' or status='2') order by option_value", SysOptions.class);
		return JSON.toJSONString(sysOptionsList);
	}

	/**
	 * 删除系统配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(String ID) {
		try {
			SysOptions sysOptions = new SysOptions();
			sysOptions.setId(ID);
			sysOptions = publicService.selectObj("AI_SYS_OPTIONS", "", "ID", "option_value", "", "", sysOptions);
			publicService.delete("AI_SYS_OPTIONS", "", "ID", sysOptions);
			String status = null;
			if (sysOptions.getStatus().equals("1")) {
				status = "观察";
			} else if (sysOptions.getStatus().equals("0")) {
				status = "停用";
			}else if (sysOptions.getStatus().equals("2")) {
				status = "回写";
			}
			publicService.insertLogInfo("配置管理的系统配置管理中，点击删除，成功删除【" + "配置类别：" + sysOptions.getOption_kind() + ",配置项代码："
					+ sysOptions.getOption_value() + ",配置项：" + sysOptions.getOption_des() + ",配置参数："
					+ sysOptions.getOption_params() + ",状态：" + status + "】的系统配置", "删除","1","系统参数管理");
			return renderSuccess("删除成功！");
		} catch (RuntimeException e) {
			publicService.insertLogInfo("配置管理的系统配置管理中，点击删除，删除ID为"+ID+"的数据，删除失败,异常:"+e.getMessage(), "删除","0","系统参数管理");
			return renderError(e.getMessage());
		}
	}
	/**
	 * 刷新系统配置
	 *
	 * @return
	 */
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	@ResponseBody
	public Object refresh() {
		try {
			String rt=configService.setvalue();
			if (rt.equals("1")){
				return renderSuccess("刷新成功！");
			}
			else{
				return renderError(rt);
			}

		} catch (RuntimeException e) {
			publicService.insertLogInfo("配置管理的系统配置管理中，点击刷新内存，刷新失败,异常:"+e.getMessage(), "修改","1","系统参数管理");
			return renderError(e.getMessage());
		}
	}

	/**
	 * 基础代码资源所有combox，包括类别
	 *
	 * @return
	 */
	@RequestMapping(value = "/comboxall", method = RequestMethod.POST)
	@ResponseBody
	public Object comboxall() {
		SysOrganization organ = new SysOrganization();
		organ.setName("所有站");
		organ.setOrgan("ALL");
		organ.setOrgantype("3");
		List<SysOrganization> organlist = (List<SysOrganization>) (List) publicService.selectObjects("ai_sys_organization",
				"", "organtype", organ);
		organlist.add(0, organ);
		return JSON.toJSONString(organlist);
	}

	/**
	 * 加载option_kind里面的资源所有combox，包括配置类别
	 *
	 * @return
	 */
	@RequestMapping(value = "/kindtree", method = RequestMethod.POST)
	@ResponseBody
	public Object comboxkind() {
		SysOptions sysOptions = new SysOptions();
		sysOptions.setOption_kind("配置类别");
		sysOptions.setOption_value("配置类别");
		List<SysOptions> sysOptionslist = findcySysOptionsAll(sysOptions, "option_kind");
		sysOptionslist.add(0, sysOptions);
		return JSON.toJSONString(sysOptionslist);
	}

	/**
	 * 查询单个cySysOptions对象数据。共用
	 *
	 * @return
	 */
	public Object cySysOptions(SysOptions sysOptions) {
		return publicService.selectObj("AI_SYS_OPTIONS", "", "ID", "option_value", "", "", sysOptions);
	}

	/**
	 * 查询数据。共用
	 *
	 * @return
	 */
	public List<SysOptions> findcySysOptionsAll(SysOptions sysOptions, String tj) {
		// TODO Auto-generated method stub
		return (List<SysOptions>) (List) publicService.selectObjs("AI_SYS_OPTIONS", "", tj, "option_value", "", "",
				sysOptions);
	}
}
