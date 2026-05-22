package com.controller.count;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.count.MjCdpf;
import com.model.sysmanage.SysOrganization;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
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
@RequestMapping("/count/cdpf")
public class MjCdpfController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysOrganizationService organService;

    @Autowired
    public SysOptionService sysOptionService;

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
        return "count/mjCdpf";
    }


    /**
     * 用户管理列表
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping(value = "/DataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        String strWhere = "";

        if (params.containsKey("organ") && params.get("organ").toString().equals("")) {
            String organ = getCurrentUserOrganCode();
            String strOrgans = organService.findChildrenCodes(organ);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = params.get("organ").toString() + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and organ in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and organ='%s'", organ);
            }
        } else if (params.containsKey("organ") && (StringUtils.isNoneBlank(params.get("organ").toString()))) {
            String organ = params.get("organ").toString();
            String strOrgans = organService.findChildrenCodes(organ);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = organ + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }

        }
        if (params.containsKey("ysw") && StringUtils.isNoneBlank(params.get("ysw").toString())) {
            strWhere += String.format(" and ysw like '%%%s%%'",
                    params.get("ysw").toString());
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and yssj  >='%s'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and yssj  <='%s'", params.get("jssj").toString());
        }
        String tblName = "(select id,d.name as organ,a.ysw,ysl,a.yssj,a.cjsj " +
                " from (select * from mj_cdpf  where 1=1 " + strWhere + " and ysw<>'骨料出厂')a " +
                " left join (select * from ai_sys_organization) d on a.organ = d.organ)a";
        logger.info(tblName);
        publicService.selectPageObjects(pageInfo, tblName, "*", "", "yssj desc");
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
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        request.setAttribute("yssj", sdf.format(new Date()));
        return "count/mjCdpfEdit";
    }

    /**
     * 添加用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(MjCdpf mjCdpf) {
        List<String> bindResult = new ArrayList<String>();

        try {
            mjCdpf.setCjsj(new Date());
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString().replaceAll("-", "");
            mjCdpf.setId(uuids);
            bindResult = ValidateService.valid(mjCdpf);

            if (bindResult.size() == 0) {
                // 验证用户是否已存在
                MjCdpf mj = publicService.selectObj("mj_cdpf", "", "organ,ysw,yssj", mjCdpf);
                if (mj != null) {
                    bindResult.add("今天该运输物已录入，不能重复创建");
                }
                if (bindResult.size() == 0) {
                    publicService.insert("mj_cdpf", "", "", mjCdpf);
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
        MjCdpf mjCdpf = publicService.selectObj("select * from mj_cdpf where id='" + id + "'", MjCdpf.class);
        request.setAttribute("oper", "edit");
        request.setAttribute("mjcdpf", mjCdpf);
        SysOrganization sysOrganization = organService.findOrganizationByCode(mjCdpf.getOrgan());
        request.setAttribute("organname", sysOrganization.getJc());
        request.setAttribute("yssj", sdf.format(null == mjCdpf.getYssj() ? new Date() : mjCdpf.getYssj()));
        return "count/mjCdpfEdit";
    }

    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(MjCdpf mjCdpf) {
        List<String> bindResult = new ArrayList<String>();

        try {
            bindResult = ValidateService.valid(mjCdpf);
            mjCdpf.setCjsj(new Date());
            if (bindResult.size() == 0) {
                publicService.update("mj_cdpf", "", "id", mjCdpf);
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
            publicService.delete("delete from mj_cdpf where id='" + id + "'");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return renderError("删除异常：" + e.getMessage());
        }
    }
}
