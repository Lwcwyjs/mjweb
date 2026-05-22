package com.controller.videomanage;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.videomanage.MjSpxx;
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
@RequestMapping("/admin/spxx")
public class SpxxController extends BaseController {

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
        return "videomanage/Spxx";
    }

    @RequestMapping(value = "/dataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object dataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
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
        if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
            strWhere += String.format(" and jclx='%s'",
                    params.get("jclx").toString());
        }
        String field = "*";
        publicService.selectPageObjects(pageInfo, "mj_spxx", field, strWhere, "qybh,dzbh");
        publicService.insertLogInfo("系统管理中的用户管理,查询了mj_spxx", "查询", "1", "视频管理");
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
        return "videomanage/SpxxEdit";
    }

    /**
     * 添加用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(MjSpxx mjSpxx) {
        List<String> bindResult = new ArrayList<String>();

        try {
            mjSpxx.setCjsj(new Date());
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString().replaceAll("-", "");
            mjSpxx.setId(uuids);
            if (mjSpxx.getDzbh() != null && !mjSpxx.getDzbh().equals("")) {
                mjSpxx.setCrkbh(mjSpxx.getDzbh().substring(0, 1));
            }

            bindResult = ValidateService.valid(mjSpxx);

            if (bindResult.size() == 0) {
                // 验证用户是否已存在
                MjSpxx mjSpxx1 = publicService.selectObj("mj_Spxx", "", "qybh,sxtbh", mjSpxx);
                if (mjSpxx1 != null) {
                    bindResult.add("该视频编号，已存在，不允许重复创建");
                }
                if (bindResult.size() == 0) {
                    mjSpxx.setSczt("0");
                    mjSpxx.setScjg("");
                    mjSpxx.setGxlx("I");

                    publicService.insert("mj_Spxx", "", "", mjSpxx);

                    publicService.insertLogInfo("系统管理的视频信息管理中，点击新建，新建失败【" + "组织机构："
                                    + organService.findOrganizationsByCode(mjSpxx.getQybh())
                                    + ",视频编号：" + mjSpxx.getSxtbh() + "】的视频信息",
                            "新增", "1", "视频信息管理");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "系统管理的视频信息管理中，点击新建，新建失败【" + "组织机构：" + organService.findOrganizationsByCode(mjSpxx.getQybh())
                            + ",视频编号：" + mjSpxx.getSxtbh() + "】的视频信息",
                    "新增", "0", "视频信息管理");
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
        MjSpxx mjSpxx = new MjSpxx();
        mjSpxx.setId(id);
        mjSpxx = publicService.selectObj("mj_Spxx", "", "id", mjSpxx);
        request.setAttribute("mjspxx", mjSpxx);
        String organname = organService.getQyjcByQybh(mjSpxx.getQybh());
        request.setAttribute("organname", organname);
        request.setAttribute("oper", "edit");

        return "videomanage/SpxxEdit";
    }

    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(MjSpxx mjSpxx) {
        List<String> bindResult = new ArrayList<String>();

        try {
            if (mjSpxx.getDzbh() != null && !mjSpxx.getDzbh().equals("")) {
                mjSpxx.setCrkbh(mjSpxx.getDzbh().substring(0, 1));
            }
            bindResult = ValidateService.valid(mjSpxx);

            if (bindResult.size() == 0) {
                MjSpxx mjSpxx1 = publicService.selectObj("select * from mj_spxx where qybh='"+mjSpxx.getQybh()+"' and sxtbh='"+mjSpxx.getSxtbh()+
                        "' and id<>'"+mjSpxx.getId()+"'", MjSpxx.class);
                if (mjSpxx1 != null) {
                    bindResult.add("该摄像头，已存在，不允许重复创建");
                }
                mjSpxx.setSczt("0");
                mjSpxx.setScjg("");
                mjSpxx.setGxlx("U");
                publicService.update("mj_Spxx", "", "id", mjSpxx);

                publicService.insertLogInfo("系统管理的视频信息管理中，点击编辑，成功修改【" + "组织机构："
                                + organService.findOrganizationsByCode(mjSpxx.getQybh())
                                + ",视频编号：" + mjSpxx.getSxtbh() + "】的视频信息",
                        "编辑", "1", "视频信息管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "系统管理的视频信息管理中点击编辑，修改失败【" + "组织机构：" + organService.findOrganizationsByCode(mjSpxx.getQybh())
                            + ",视频编号：" + mjSpxx.getSxtbh() + "】的视频信息",
                    "编辑", "0", "视频信息管理");
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
        return "videomanage/userEditPwd";
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
            MjSpxx mjSpxx = new MjSpxx();
            mjSpxx.setId(id);
            publicService.delete("mj_spxx", "", "id", mjSpxx);
            publicService.insertLogInfo(
                    "系统管理的视频管理中，点击删除，成功删除【" + id + "】的视频记录",
                    "删除", "1", "视频管理");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("系统管理的视频管理中，点击删除，删除ID为" + id + "的视频记录，删除失败,异常:" + e.getMessage(), "删除", "0", "视频管理");

            return renderError("删除异常：" + e.getMessage());
        }
    }

    @RequestMapping(value = "/basedata/{param}")
    @ResponseBody
    public Object getBaseData(@PathVariable String param) {
        return sysUserService.findBaseData(param);
    }
}
