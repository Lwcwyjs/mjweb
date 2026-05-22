package com.controller.configmanage;

import com.alibaba.fastjson.JSON;
import com.commons.base.BaseController;
import com.commons.result.Result;
import com.commons.utils.PageInfo;
import com.commons.utils.StringUtils;
import com.model.configmange.SysCode;
import com.model.sysmanage.MjDzxx;
import com.service.base.PublicService;
import com.service.configmanage.SysCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @description：基础代码管理
 * @author：zengxj @date：2016/06/10
 */
@Controller
@RequestMapping("/admin/syscode")
public class SysCodeController extends BaseController {

    @Autowired
    @Resource
    private SysCodeService syscodeService;

    @Autowired
    @Resource
    private PublicService publicService;

    /**
     * 基础代码管理主页
     *
     * @return
     */
    @RequestMapping("/manager")
    public String manager() {
        return "configmanage/sysCode";
    }

    /**
     * 左侧基础代码资源树
     *
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    @ResponseBody
    public Object tree() {
        return syscodeService.findTree();
    }

    /**
     * 左侧基础代码资源树
     *
     * @return
     */
    @RequestMapping(value = "/treeType", method = RequestMethod.POST)
    @ResponseBody
    public Object treeType() {
        return syscodeService.findTreeType();
    }

    /**
     * 基础代码资源combox
     *
     * @return
     */
    @RequestMapping(value = "/combox", method = RequestMethod.POST)
    @ResponseBody
    public Object combox(HttpServletRequest request) {
        String oi_name = request.getParameter("oi_name");
        return syscodeService.findSyscodeCombox(oi_name);
    }
    @RequestMapping(value = "/comboxdzbh", method = RequestMethod.POST)
    @ResponseBody
    public Object comboxdzbh(HttpServletRequest request) {
        String qybh = request.getParameter("qybh");
        MjDzxx mjDzxx = new MjDzxx();
        mjDzxx.setQybh(qybh);
        List<MjDzxx> mjDzxxes =publicService.selectObjs("mj_dzxx","","qybh",mjDzxx);
        return JSON.toJSONString(mjDzxxes);
    }

    /**
     * 基础代码资源所有combox，包括类别
     *
     * @return
     */
    @RequestMapping(value = "/comboxall", method = RequestMethod.POST)
    @ResponseBody
    public Object comboxall() {
        return syscodeService.findSyscodeComboxAll();
    }

    /**
     * 分页显示
     *
     * @return
     */
    @RequestMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(SysCode sysCode, Integer page, Integer rows, String sort, String order) {

        PageInfo pageInfo = new PageInfo(page, rows);
        String strWhere = "";
        if (!StringUtils.isBlank(sysCode.getOi_name())) {
            strWhere += String.format(" and oi_name='%s'", sysCode.getOi_name());
        }
        if (!StringUtils.isBlank(sysCode.getOi_value())) {
            strWhere += String.format(" and oi_value='%s'", sysCode.getOi_value());
        }
        publicService.insertLogInfo("配置管理中数据字典管理,查询了ai_sys_code", "查询", "1", "基础数据管理");
        publicService.selectPageObjects(pageInfo, "ai_sys_code", "", strWhere, "oi_code");
        return pageInfo;
    }

    /**
     * 添加基础代码页
     *
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage() {
        return "configmanage/sysCodeAdd";
    }

    /**
     * 添加基础代码
     *
     * @param sysCode
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(SysCode sysCode) {
        Result result = new Result();
        if (syscodeService.findNotSyscode(sysCode) == null) {
            try {
                UUID uuid = UUID.randomUUID();
                String uuids = uuid.toString().replaceAll("-", "");
                sysCode.setId(uuids);
                publicService.insert("ai_sys_code", "", "", sysCode);
                publicService.insertLogInfo(
                        "配置管理中数据字典管理点击添加按钮，成功添加:ID=" + sysCode.getId() + ",oi_name=" + sysCode.getOi_name()
                                + ",oi_value=" + sysCode.getOi_value() + ",oi_code=" + sysCode.getOi_code() + "的数据",
                        "新增", "1", "基础数据管理");
                return renderSuccess("添加成功");
            } catch (RuntimeException e) {
                publicService.insertLogInfo("配置管理中数据字典管理点击添加按钮,添加失败oi_name=" + sysCode.getOi_name() + ",oi_value="
                                + sysCode.getOi_value() + ",oi_code=" + sysCode.getOi_code() + "的数据,异常:" + e.getMessage(),
                        "新增", "0", "基础数据管理");
                return renderError(e.getMessage());
            }
        } else {
            return renderError("该项基础代码已存在，再次添加失败");
        }
    }

    /**
     * 编辑基础代码页
     *
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(HttpServletRequest request, String id) {

        SysCode sysCode = syscodeService.findSyscodeById(id);
        request.setAttribute("syscode", sysCode);
        return "configmanage/sysCodeEdit";
    }

    /**
     * 编辑基础代码
     *
     * @param sysCode
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(SysCode sysCode) {
        Result result = new Result();
        SysCode syscodeother = syscodeService.findNotSyscode(sysCode);
        if (syscodeother != null) {
            if (!syscodeother.getId().equals(sysCode.getId())) {
                publicService.insertLogInfo("配置管理中数据字典管理点击编辑按钮，编辑:【ID=" + sysCode.getId() + ",oi_name="
                        + sysCode.getOi_name() + ",oi_value=" + sysCode.getOi_value() + ",oi_code="
                        + sysCode.getOi_code() + "】的数据编辑失败,异常:数据项和别的基础代码记录有冲突", "编辑", "0", "基础数据管理");

                return renderError("数据项和别的基础代码记录有冲突");
            }
        }
        try {
            publicService.update("ai_sys_code", "Id", "Id", sysCode);
            publicService.insertLogInfo(
                    "配置管理中数据字典管理点击编辑按钮，成功编辑:ID=" + sysCode.getId() + ",oi_name=" + sysCode.getOi_name()
                            + ",oi_value=" + sysCode.getOi_value() + ",oi_code=" + sysCode.getOi_code() + "的数据",
                    "编辑", "1", "基础数据管理");
            return renderSuccess("保存成功");
        } catch (RuntimeException e) {
            publicService.insertLogInfo("配置管理中数据字典管理点击编辑按钮，编辑:【ID=" + sysCode.getId() + ",oi_name="
                    + sysCode.getOi_name() + ",oi_value=" + sysCode.getOi_value() + ",oi_code="
                    + sysCode.getOi_code() + "】的数据编辑失败,异常:" + e.getMessage(), "编辑", "0", "基础数据管理");

            return renderError(e.getMessage());
        }
    }

    /**
     * 删除基础代码
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
        Result result = new Result();
        try {
            SysCode sysCode = new SysCode();
            sysCode.setId(id);
            publicService.delete("ai_sys_code", "", "Id", sysCode);
            publicService.insertLogInfo("配置管理中数据字典管理,点击删除按钮,成功删除【ID=" + sysCode.getId() + "】的数据", "删除", "1", "基础数据管理");
            return renderSuccess("删除成功");
        } catch (RuntimeException e) {
            publicService.insertLogInfo("配置管理中数据字典管理,点击删除按钮,删除ID为" + id + "的数据,删除失败", "删除", "0", "基础数据管理");
            return renderError(e.getMessage());
        }
    }

    @RequestMapping(value = "/CodeValue", method = RequestMethod.GET)
    @ResponseBody
    public Object getCodeValueByName(@RequestParam("oi_name") String oiname) {
        try {
            String where = "and oi_name = '" + oiname + "'";
            List<Map<Object, Object>> mapList = publicService.selectObjects("ai_sys_code", "oi_code,oi_value", where, "oi_code");
            return renderSuccess(mapList);
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("getCodeValueByName异常", "查询", "0", "基础数据管理");
            return renderError(e.getMessage());
        }
    }
    @RequestMapping(value = "/getdzbh", method = RequestMethod.POST)
    @ResponseBody
    public Object getdzbh(HttpServletRequest request) {
        String organ = request.getParameter("organ");
        MjDzxx mjDzxx = new MjDzxx();
        mjDzxx.setQybh(organ);
        List<MjDzxx> mjDzxxes =publicService.selectObjs("mj_dzxx","","qybh","dzbh",mjDzxx);
        return JSON.toJSONString(mjDzxxes);
    }
}
