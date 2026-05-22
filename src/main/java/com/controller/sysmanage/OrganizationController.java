package com.controller.sysmanage;

import com.alibaba.fastjson.JSON;
import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.result.OrgTreeVo;
import com.commons.result.TreeVo;
import com.commons.utils.PageInfo;
import com.commons.utils.StringUtils;
import com.model.sysmanage.QryOrganizationCondition;
import com.model.sysmanage.SysOrganization;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @description：部门管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/organization")
public class OrganizationController extends BaseController {

    @Autowired
    @Resource
    private SysOrganizationService sysOrganizationService;
    @Autowired
    private PublicService publicService;

    /**
     * 部门管理主页
     *
     * @return
     */
    @RequestMapping("/manager")
    public String manager(HttpServletRequest request) {
        request.setAttribute("organ", getCurrentUserOrganCode());
        return "sysmanage/sysOrganization";
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    @ResponseBody
    public Object tree() {
        return sysOrganizationService.findTree(getCurrentUserOrganCode(), "");
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @RequestMapping(value = "/treeGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object treeGrid(@RequestParam Map<String, Object> params) {
        String porgan = "";
        String organ = "";
        Map<String, Object> map = new HashMap<>();
        if (params.containsKey("porgan")) {
            porgan = params.get("porgan").toString();
        } else {
            porgan = "无";
        }
        if (params.containsKey("organ")) {
            organ = params.get("organ").toString();
        } else {
            organ = "";
        }
        List<SysOrganization> sysOrganizations = sysOrganizationService.findOrganAll(porgan, organ);
        map.put("data", sysOrganizations);
        return map;
    }

    @RequestMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(QryOrganizationCondition qCondition, Integer page, Integer rows, String sort, String order) {

        PageInfo pageInfo = new PageInfo(page, rows);
        String strWhere = "";
        String porgan = "";

        if (!StringUtils.isBlank(qCondition.getOrgan())) {
            strWhere += String.format(" and organ='%s'", qCondition.getOrgan());
        }
        if (!StringUtils.isBlank(qCondition.getName())) {
            strWhere += String.format(" and name like '%%%s%%'", qCondition.getName());
        }

        if (qCondition.getPorgan() != null) {
            porgan = qCondition.getPorgan();
        } else {
            porgan = getCurrentUserOrganCode();
        }
        strWhere += String.format(" and %s", publicService.getWhereOrgan("porgan", porgan));
        publicService.selectPageObjects(pageInfo, "ai_sys_organization", "", "", strWhere, new SysOrganization());
        publicService.insertLogInfo("系统管理中机构管理列表查询,查询了ai_sys_organization", "查询", "1", "机构管理");
        System.out.println(JSON.toJSONString(pageInfo));
        return pageInfo;
    }

    /**
     * 添加部门页
     *
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage(HttpServletRequest request) {
        request.setAttribute("user", getCurrentUser());
        request.setAttribute("oper", "add");
        return "sysmanage/sysOrganizationEdit";
    }


    /**
     * 添加部门
     *
     * @param organization
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(SysOrganization organization) {
        List<String> bindResult = new ArrayList<String>();

        organization.setCreatedate(new Date());

        if (StringUtils.isBlank(organization.getPorgan())) {
            organization.setPorgan(getCurrentUserOrganCode());
        }

        try {
            bindResult = ValidateService.valid(organization);

            if (bindResult.size() == 0) {
                // 验证机构编号是否已存在
                SysOrganization organ = publicService.selectObj("ai_sys_organization", "", "organ", organization);

                if (organ != null) {
                    bindResult.add("机构编号：已存在，不允许重复创建");
                } else {

                    publicService.insert("ai_sys_organization", "", "organ", organization);
                    publicService.insertLogInfo("系统管理的机构管理中，点击新建，成功添加【"
                            + "机构编号：" + organization.getOrgan()
                            + ",机构名称：" + organization.getName()
                            + "】的机构信息", "新增", "1", "机构管理");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("系统管理的机构管理中，点击新建，添加失败【"
                    + "机构编号：" + organization.getOrgan()
                    + ",机构名称：" + organization.getName()
                    + "】的机构信息", "新增", "0", "机构管理");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("新建成功！") : renderError(bindResult.toString());
    }

    /**
     * 编辑资源页
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(HttpServletRequest request, String code) {
        SysOrganization organization = new SysOrganization();
        organization.setOrgan(code);
        organization = publicService.selectObj("ai_sys_organization", "", "organ", organization);
        if (organization != null) {
            String porgan = organization.getPorgan();
            if (porgan != null) {
                SysOrganization organizationex = sysOrganizationService.findOrganizationByCode(porgan);
                if (organizationex != null) {
                    organization.setPorganname(organizationex.getJc());
                }
            }
        }
        request.setAttribute("organ", organization);
        request.setAttribute("oper", "edit");
        return "sysmanage/sysOrganizationEdit";
    }


    /**
     * 编辑部门
     *
     * @param organization
     * @return
     */
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Object edit(@RequestParam("oldorgan") String oldorgan, SysOrganization organization) {
        //System.out.println(oldorgan+"11111111111111111111111");
        List<String> bindResult = new ArrayList<String>();

        if (StringUtils.isBlank(organization.getPorgan())) {
            organization.setPorgan(getCurrentUserOrganCode());
        }

        try {
            bindResult = ValidateService.valid(organization);
            if (!StringUtils.isBlank(organization.getPorgan())
                    && organization.getOrgan().equals(organization.getPorgan())) {
                bindResult.add("上级机构不能是其本身！");
            }
            if (bindResult.size() == 0) {

                publicService.update("UPDATE ai_sys_organization SET NAME='" + organization.getName() + "',ORGAN='" + organization.getOrgan() +
                        "',ORGANTYPE='" + organization.getOrgantype() + "',PORGAN='" + organization.getPorgan() + "',JC='" + organization.getJc() +
                        "',STATUS='" + organization.getStatus() + "',SEQ='" + organization.getSeq() + "',address='" + organization.getAddress() +
                        "',tyshxybm='" + organization.getTyshxybm() + "',lng='" + organization.getLng() + "',lat='" + organization.getLat() +
                        "',ssxq='" + organization.getSsxq() + "',frdb='" + organization.getFrdb() + "',hylx='" + organization.getHylx() +
                        "',hyfz='" + organization.getHyfz() + "',jxfjgklx='" + organization.getJxfjgklx() + "',lxr='" + organization.getLxr() +
                        "',lxrdh='" + organization.getLxrdh() + "',zhcrksl=" + organization.getZhcrksl() + ",dzsl=" + organization.getDzsl() +
                        ",ysclsl=" + organization.getYsclsl() + ",cnysclsl=" + organization.getCnysclsl() + ",fdlydjxsl=" + organization.getFdlydjxsl() +
                        ",organnew='" + organization.getOrgannew() + "' WHERE ORGAN='" + oldorgan + "' ");
                if(!oldorgan.equals(organization.getOrgan())){
                    publicService.update("update ai_sys_user set organ='"+organization.getOrgan()+"' where organ='"+oldorgan+"'");
                }
                publicService.insertLogInfo("系统管理的机构管理中，点击编辑，成功修改【"
                        + "机构编号：" + organization.getOrgan()
                        + ",机构名称：" + organization.getName()
                        + "】的机构信息", "修改", "1", "机构管理");

            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("系统管理的机构管理中，点击编辑，修改失败【"
                    + "机构编号：" + organization.getOrgan()
                    + ",机构名称：" + organization.getName()
                    + "】的机构信息", "修改", "0", "机构管理");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("编辑成功！") : renderError(bindResult.toString());
    }

    /**
     * 删除部门
     *
     * @param
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String code) {
        try {
            SysOrganization organization = new SysOrganization();
            organization.setOrgan(code);
            organization = publicService.selectObj("ai_sys_organization", "", "organ", organization);

            sysOrganizationService.deleteOrganizationByCode(code);
//			publicService.h_index_synch("ai_sys_organization", "organ",organization.getOrgan(), "D", organization.getOrgan().substring(0,4)+"0000", "0", "TB002");//上传信息。organ设置成市局的organ。上传程序扫描的时候根据市局的organ	

            publicService.insertLogInfo("系统管理的机构管理中，点击删除，成功删除【"
                    + "机构编号：" + organization.getOrgan()
                    + ",机构名称：" + organization.getName()
                    + "】的机构信息", "删除", "1", "机构管理");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("系统管理的机构管理中，点击删除，删除organ为" + code + "的数据异常," + e.getMessage(), "删除", "0", "机构管理");
            return renderError("删除异常：" + e.getMessage());
        }
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @RequestMapping(value = "/treelevel", method = RequestMethod.POST)
    @ResponseBody
    public Object treelevel(@RequestParam("organtype") String organtype) {
        System.out.print("organtype" + organtype);
        List<TreeVo> trees = sysOrganizationService.findTree(this.getCurrentUserOrganCode(), organtype);
        System.out.print(trees);
        return trees;
    }

    /**
     * 部门资源树
     *
     * @return
     */
    @RequestMapping(value = "/orgtreelevel", method = RequestMethod.POST)
    @ResponseBody
    public Object orgTreelevel(@RequestParam("organtype") String organtype) {
//		System.out.print("organtype" + organtype);
        List<OrgTreeVo> trees = sysOrganizationService.findOrgTree(this.getCurrentUserOrganCode(), organtype);
        System.out.print(trees);
        return trees;
    }

    /**
     * 部门资源树组织机构管理
     *
     * @return
     */
    @RequestMapping(value = "/orgtreelevelForOrgan", method = RequestMethod.POST)
    @ResponseBody
    public Object orgTreelevelForOrgan(@RequestParam("organtype") String organtype) {
//		System.out.print("organtype" + organtype);
        List<OrgTreeVo> trees = sysOrganizationService.findOrgTreeForOrgan(this.getCurrentUserOrganCode(), organtype);
        System.out.print(trees);
        return trees;
    }
    @RequestMapping(value = "/getorgan", method = RequestMethod.POST)
    @ResponseBody
    public Object getorgan(String qybh, HttpServletRequest request) throws Exception {
        SysOrganization sysOrganization=new SysOrganization();
        sysOrganization.setOrgan(qybh);
        sysOrganization=publicService.selectObj("ai_sys_organization","","organ",sysOrganization);
        return sysOrganization;
    }
}
