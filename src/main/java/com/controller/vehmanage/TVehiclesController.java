package com.controller.vehmanage;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
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
import java.util.Date;
import java.util.Map;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/business/tvehicles")
public class TVehiclesController extends BaseController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    public SysOrganizationService sysOrganizationService;

    @Autowired
    private PublicService publicService;
    @Autowired
    public SysOptionService sysOptionService;


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
        return "vehmanage/TVehicles";
    }

    @RequestMapping(value = "/dataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object dataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        String strWhere = "";
        if (params.containsKey("clxh") && StringUtils.isNoneBlank(params.get("clxh").toString())) {
            strWhere += " and clxh like '%" + params.get("clxh").toString() + "%'";
        }
        if (params.containsKey("fdjxh") && StringUtils.isNoneBlank(params.get("fdjxh").toString())) {
            strWhere += " and fdjxh like '%" + params.get("fdjxh").toString() + "%'";
        }
        String field = "*";
        publicService.selectPageObjects(pageInfo, "t_vehicles", field, strWhere, "filename desc");
        return pageInfo;
    }
}
