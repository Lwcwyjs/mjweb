package com.controller.sysmanage;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.sysmanage.MjDzxx;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/dzxx")
public class DzxxController extends BaseController {

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
        return "sysmanage/Dzxx";
    }

    @RequestMapping(value = "/DataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        try {
            PageInfo pageInfo = new PageInfo(page, rows, sort, order);
            String strWhere = "";
            if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
                strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
            } else {
                String strCurrentOrgan = getCurrentUserOrganCode();
                String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = strCurrentOrgan + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
                }

            }
            if (params.containsKey("dzbh") && StringUtils.isNoneBlank(params.get("dzbh").toString())) {
                strWhere += String.format(" and dzbh like '%%%s%%'",
                        params.get("dzbh").toString());
            }
            String tblName = "mj_dzxx";
            logger.info(tblName);
            publicService.selectPageObjects(pageInfo, tblName, "*", strWhere, "qybh,dzbh");
            pageInfo.setCode(0);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return renderError(e.getMessage());
        }
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String addPage(HttpServletRequest request) {
        request.setAttribute("oper", "add");
        return "sysmanage/DzxxEdit";
    }

    /**
     * 添加用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(MjDzxx mjDzxx) {
        List<String> bindResult = new ArrayList<String>();

        try {
            mjDzxx.setCjsj(new Date());
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString().replaceAll("-", "");
            mjDzxx.setId(uuids);

            bindResult = ValidateService.valid(mjDzxx);

            if (bindResult.size() == 0) {
                // 验证用户是否已存在
                MjDzxx mjDzxx1 = publicService.selectObj("mj_dzxx", "", "qybh,dzbh", mjDzxx);
                if (mjDzxx1 != null) {
                    bindResult.add("该道闸编号，已存在，不允许重复创建");
                }
                if (bindResult.size() == 0) {
                    mjDzxx.setSczt("0");
                    mjDzxx.setScjg("");
                    mjDzxx.setGxlx("I");
                    publicService.insert("mj_dzxx", "", "", mjDzxx);

                    publicService.insertLogInfo("系统管理的道闸信息管理中，点击新建，新建失败【" + "组织机构："
                                    + organService.findOrganizationsByCode(mjDzxx.getQybh())
                                    + ",道闸编号：" + mjDzxx.getDzbh() + "】的道闸信息",
                            "新增", "0", "道闸信息管理");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "系统管理的道闸信息管理中，点击新建，新建失败【" + "组织机构：" + organService.findOrganizationsByCode(mjDzxx.getQybh())
                            + ",道闸编号：" + mjDzxx.getDzbh() + "】的道闸信息",
                    "新增", "0", "道闸信息管理");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        MjDzxx mjDzxx=new MjDzxx();
        mjDzxx.setId(id);
        mjDzxx = publicService.selectObj("mj_dzxx", "", "id", mjDzxx);
        request.setAttribute("mjdzxx", mjDzxx);
        String organname=organService.getQyjcByQybh(mjDzxx.getQybh());
        request.setAttribute("organname", organname);
        request.setAttribute("oper", "edit");

        return "sysmanage/DzxxEdit";
    }

    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(MjDzxx mjDzxx) {
        List<String> bindResult = new ArrayList<String>();

        try {
            bindResult = ValidateService.valid(mjDzxx);

                if (bindResult.size() == 0) {
                    MjDzxx mjDzxx1 = publicService.selectObj("select * from mj_dzxx where qybh='"+mjDzxx.getQybh()+"' and dzbh='"+mjDzxx.getDzbh()+
                            "' and id<>'"+mjDzxx.getId()+"'", MjDzxx.class);
                    if (mjDzxx1 != null) {
                        bindResult.add("该道闸编号，已存在，不允许重复创建");
                    }
                    mjDzxx.setSczt("0");
                    mjDzxx.setScjg("");
                    mjDzxx.setGxlx("U");
                    publicService.update("mj_dzxx", "", "id", mjDzxx);

                    publicService.insertLogInfo("系统管理的道闸信息管理中，点击编辑，成功修改【" + "组织机构："
                                    + organService.findOrganizationsByCode(mjDzxx.getQybh())
                                    + ",道闸编号：" + mjDzxx.getDzbh() + "】的道闸信息",
                            "新增", "0", "道闸信息管理");
                }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "系统管理的道闸信息管理中点击编辑，成功修改【" + "组织机构：" + organService.findOrganizationsByCode(mjDzxx.getQybh())
                            + ",道闸编号：" + mjDzxx.getDzbh() + "】的道闸信息",
                    "新增", "0", "道闸信息管理");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
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
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
        try {
            MjDzxx mjDzxx = new MjDzxx();
            mjDzxx.setId(id);
            publicService.delete("mj_dzxx", "", "id", mjDzxx);
            publicService.insertLogInfo(
                    "系统管理的道闸管理中，点击删除，成功删除【" + id + "】的道闸记录",
                    "删除", "1", "道闸管理");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("系统管理的道闸管理中，点击删除，删除ID为" + id + "的道闸记录，删除失败,异常:" + e.getMessage(), "删除", "0", "道闸管理");

            return renderError("删除异常：" + e.getMessage());
        }
    }

    @RequestMapping(value = "/basedata/{param}")
    @ResponseBody
    public Object getBaseData(@PathVariable String param) {
        return sysUserService.findBaseData(param);
    }
}
