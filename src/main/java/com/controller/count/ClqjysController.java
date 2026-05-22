package com.controller.count;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.commons.utils.TimeUtil;
import com.model.count.Cltxpf;
import com.model.login.PieFVo;
import com.model.login.PieVo;
import com.service.base.PublicService;
import com.service.configmanage.SysCodeService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.util.ConverUtil;
import com.util.ExcelCell;
import com.util.ExcelExportUtil;
import com.util.ExcelType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：ai识别结果查看
 */
@Controller
@RequestMapping("/count/clqjys")
public class ClqjysController extends BaseController {

    @Autowired
    public PublicService publicService;

    @Autowired
    public SysOrganizationService sysOrganizationService;

    @Autowired
    public SysOptionService sysOptionService;
    @Autowired
    public SysCodeService sysCodeService;

    @RequestMapping("/main")
    public String main() {
        return "count/ClqjysManager";
    }


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

    @RequestMapping(value = "/DataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        try {
            PageInfo pageInfo = new PageInfo(page, rows, sort, order);
            String strWhere = " 1=1 ";
            if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
                strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
            }
            if (params.containsKey("organ") && params.get("organ").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("organ").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            } else if (params.containsKey("organ") && (StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            String field = "pfbz,cls,jclc";
            String sql = "SELECT e.PFBZ,CLS,JCLC FROM " +
                    "(SELECT DISTINCT PFBZ,COUNT(*) AS CLS FROM (SELECT DISTINCT CPHM,CPYS,isnull(PFBZ,'') as pfbz FROM MJ_DATA_BASE WHERE " + strWhere +
                    " GROUP BY CPHM,CPYS,isnull(PFBZ,''))d GROUP BY PFBZ)e LEFT JOIN(SELECT isnull(PFBZ,'') as pfbz,COUNT(*) AS JCLC " +
                    "FROM MJ_DATA_BASE WHERE " + strWhere + " GROUP BY isnull(PFBZ,''))f " +
                    "ON e.PFBZ=f.pfbz  ";
            publicService.selectPageObjects(pageInfo, sql, field, "", "pfbz");
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return renderError(e.getMessage());
        }
    }

    @RequestMapping(value = "/clqjyssj", method = RequestMethod.POST) // 整车通过率统计
    @ResponseBody
    public Object cltxpfsj(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> reData = new HashMap<String, Object>();
        List<Cltxpf> cltxpfList = new ArrayList<Cltxpf>();
        String strWhere = " 1=1 ";
        if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
            strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
        }
        if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
            String organ = getCurrentUserOrganCode();
            String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = params.get("qybh").toString() + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
            String organ = params.get("qybh").toString();
            String strOrgans = sysOrganizationService.findChildrenCodes(organ);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = organ + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String sql = "SELECT e.PFBZ,CLS,JCLC FROM " +
                "(SELECT DISTINCT PFBZ,COUNT(*) AS CLS FROM (SELECT DISTINCT CPHM,CPYS,isnull(PFBZ,'') as pfbz FROM MJ_DATA_BASE WHERE " + strWhere +
                " GROUP BY CPHM,CPYS,isnull(PFBZ,''))d GROUP BY PFBZ)e LEFT JOIN(SELECT isnull(PFBZ,'') as pfbz,COUNT(*) AS JCLC " +
                "FROM MJ_DATA_BASE WHERE " + strWhere + " GROUP BY isnull(PFBZ,''))f " +
                "ON e.PFBZ=f.pfbz  order by pfbz";
        try {
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);
            int idslc=0;
            if (cltxpfs.size() > 0) {
                int inumcls=0;
                int inumjclc=0;
                String pfbz = "0";
                boolean bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "1";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "2";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "3";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "4";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                Cltxpf cltxpf1 = new Cltxpf();
                cltxpf1.setPfbz("国Ⅴ以下");
                cltxpf1.setCls(String.valueOf(inumcls));
                cltxpf1.setJclc(String.valueOf(inumjclc));
                cltxpfList.add(cltxpf1);
                pfbz = "5";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("国Ⅴ");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("国Ⅴ");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "6";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("国Ⅵ");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("国Ⅵ");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "D";
                bhave = false;

                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("电动");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        idslc= Integer.parseInt(cltxpfs.get(i).getJclc());
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("电动");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "X";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals("X")) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("未知");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                pfbz = "";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals("")) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("未录入");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                    }
                }
            }
            int icls = 0;
            int ijclc = 0;
            for (int i = 0; i < cltxpfList.size(); i++) {
                icls = icls + Integer.parseInt(cltxpfList.get(i).getCls());
                ijclc = ijclc + Integer.parseInt(cltxpfList.get(i).getJclc());
            }
            Cltxpf cltxpf = new Cltxpf();
            cltxpf.setPfbz("清洁占比");
            cltxpf.setCls(String.valueOf(icls));
            String qjzb = "0.00%";
            if(ijclc!=0){
                qjzb=String.format("%.2f", (double)idslc / ijclc*100)+"%";
            }
            cltxpf.setJclc(String.valueOf(qjzb));
            cltxpfList.add(cltxpf);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cltxpfList;
    }
    @RequestMapping(value = "/clqjyswsj", method = RequestMethod.POST) // 整车通过率统计
    @ResponseBody
    public Object cltxpfsjysw(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> reData = new HashMap<String, Object>();
        List<Cltxpf> cltxpfList = new ArrayList<Cltxpf>();
        String strWhere = " 1=1 ";
        if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
            strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
        }
        if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
            String organ = getCurrentUserOrganCode();
            String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = params.get("qybh").toString() + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
            String organ = params.get("qybh").toString();
            String strOrgans = sysOrganizationService.findChildrenCodes(organ);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = organ + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", organ);
            }
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String sql = "SELECT  isnull(PFBZ,'') as PFBZ,SUM(CAST(ISNULL(ysl, '0') AS float)) AS JCLC  FROM  mj_data_base where "+
                strWhere+"  GROUP BY  isnull(PFBZ,'')";
        try {
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);
            float idslc=0;
            if (cltxpfs.size() > 0) {
                float inumcls=0;
                float inumjclc=0;
                String pfbz = "0";
                boolean bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumjclc=inumjclc+Float.parseFloat(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "1";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumjclc=inumjclc+Float.parseFloat(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "2";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumjclc=inumjclc+Float.parseFloat(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "3";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumjclc=inumjclc+Float.parseFloat(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "4";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumjclc=inumjclc+Float.parseFloat(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                Cltxpf cltxpf1 = new Cltxpf();
                cltxpf1.setPfbz("国Ⅴ以下");
                cltxpf1.setJclc(String.valueOf(inumjclc));
                cltxpfList.add(cltxpf1);
                pfbz = "5";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("国Ⅴ");
                        cltxpf.setJclc(String.format("%.2f", Float.parseFloat(cltxpfs.get(i).getJclc())));
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("国Ⅴ");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "6";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("国Ⅵ");
                        cltxpf.setJclc(String.format("%.2f", Float.parseFloat(cltxpfs.get(i).getJclc())));
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("国Ⅵ");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "D";
                bhave = false;

                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("电动");
                        cltxpf.setJclc(String.format("%.2f", Float.parseFloat(cltxpfs.get(i).getJclc())));
                        cltxpfList.add(cltxpf);
                        idslc= Float.parseFloat(cltxpfs.get(i).getJclc());
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("电动");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "X";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals("X")) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("未知");
                        cltxpf.setJclc(String.format("%.2f", Float.parseFloat(cltxpfs.get(i).getJclc())));
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                pfbz = "";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals("")) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("未录入");
                        cltxpf.setJclc(String.format("%.2f", Float.parseFloat(cltxpfs.get(i).getJclc())));
                        cltxpfList.add(cltxpf);
                    }
                }
            }
            float ijclc = 0;
            for (int i = 0; i < cltxpfList.size(); i++) {
                ijclc = ijclc + Float.parseFloat(cltxpfList.get(i).getJclc());
            }
            Cltxpf cltxpf = new Cltxpf();
            cltxpf.setPfbz("清洁占比");
            cltxpf.setCls(String.valueOf(ijclc));
            String qjzb = "0.00%";
            if(ijclc!=0){
                qjzb=String.format("%.2f", (double)idslc / ijclc*100)+"%";
            }
            cltxpf.setJclc(String.valueOf(qjzb));
            cltxpfList.add(cltxpf);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cltxpfList;
    }

    @RequestMapping(value = "/clqjyspie", method = RequestMethod.POST) // 整车通过率统计
    @ResponseBody
    public Object cltxpfpie(@RequestParam Map<String, Object> params) {
        Map<String, Object> reData = new HashMap<String, Object>();
        List<PieVo> pfbzVos = new ArrayList<PieVo>();

        String strWhere = "1=1";
        if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
            strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
        }
        if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
            strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
        } else {
            String strCurrentOrgan = getCurrentUserOrganCode();
            String strOrgans = sysOrganizationService.findChildrenCodes(strCurrentOrgan);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = strCurrentOrgan + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
            }

        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String sql = "select distinct isnull(pfbz,'') as item,count(*) as inum from mj_data_base where " + strWhere + " group by isnull(pfbz,'') ";
        try {
            pfbzVos = publicService.selectObjs(sql, PieVo.class);
            List<Integer> g4arr = new ArrayList<Integer>();
            List<Integer> g5arr = new ArrayList<Integer>();
            List<Integer> g6arr = new ArrayList<Integer>();
            List<Integer> darr = new ArrayList<Integer>();
            List<Integer> xarr = new ArrayList<Integer>();
            int inum=0;
            for (PieVo o : pfbzVos) {
                if (o.getItem() != null) {
                    if (o.getItem().equals("0")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("1")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("2")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("3")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("4")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("5")) {
                        g5arr.add(o.getInum());
                    } else if (o.getItem().equals("6")) {
                        g6arr.add(o.getInum());
                    } else if (o.getItem().equals("D")) {
                        darr.add(o.getInum());
                    } else {
                        xarr.add(o.getInum());
                    }
                } else {
                    xarr.add(o.getInum());
                }
            }
            g4arr.add(inum);
            if (g5arr.size() == 0) {
                g5arr.add(0);
            }
            if (g6arr.size() == 0) {
                g6arr.add(0);
            }
            if (darr.size() == 0) {
                darr.add(0);
            }
            if (xarr.size() == 0) {
                xarr.add(0);
            }
            reData.put("g4arr", g4arr);
            reData.put("g5arr", g5arr);
            reData.put("g6arr", g6arr);
            reData.put("darr", darr);
            reData.put("xarr", xarr);
            reData.put("code", "1");
            reData.put("message", "查询成功");

        } catch (Exception e) {
            logger.error(e.getMessage());
            reData.put("code", "0");
            reData.put("message", "失败");
        }
        return reData;
    }
    @RequestMapping(value = "/clqjyswpie", method = RequestMethod.POST) // 整车通过率统计
    @ResponseBody
    public Object clqjyswpie(@RequestParam Map<String, Object> params) {
        Map<String, Object> reData = new HashMap<String, Object>();
        List<PieFVo> pfbzVos = new ArrayList<PieFVo>();

        String strWhere = "1=1";
        if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
            strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
        }
        if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
            strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
        } else {
            String strCurrentOrgan = getCurrentUserOrganCode();
            String strOrgans = sysOrganizationService.findChildrenCodes(strCurrentOrgan);
            if (StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = strCurrentOrgan + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and qybh in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
            }

        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String sql = "select isnull(pfbz,'') as item,SUM(CAST(ISNULL(ysl, '0') AS float)) as inum from mj_data_base where " + strWhere + " group by isnull(pfbz,'') ";
        try {
            pfbzVos = publicService.selectObjs(sql, PieFVo.class);
            List<Float> g4arr = new ArrayList<Float>();
            List<Float> g5arr = new ArrayList<Float>();
            List<Float> g6arr = new ArrayList<Float>();
            List<Float> darr = new ArrayList<Float>();
            List<Float> xarr = new ArrayList<Float>();
            float inum=0;
            for (PieFVo o : pfbzVos) {
                if (o.getItem() != null) {
                    if (o.getItem().equals("0")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("1")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("2")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("3")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("4")) {
                        inum=inum+o.getInum();
                    } else if (o.getItem().equals("5")) {
                        g5arr.add(o.getInum());
                    } else if (o.getItem().equals("6")) {
                        g6arr.add(o.getInum());
                    } else if (o.getItem().equals("D")) {
                        darr.add(o.getInum());
                    } else {
                        xarr.add(o.getInum());
                    }
                } else {
                    xarr.add(o.getInum());
                }
            }
            g4arr.add(inum);
            if (g5arr.size() == 0) {
                g5arr.add(Float.parseFloat("0"));
            }
            if (g6arr.size() == 0) {
                g6arr.add(Float.parseFloat("0"));
            }
            if (darr.size() == 0) {
                darr.add(Float.parseFloat("0"));
            }
            if (xarr.size() == 0) {
                xarr.add(Float.parseFloat("0"));
            }
            reData.put("g4arr", g4arr);
            reData.put("g5arr", g5arr);
            reData.put("g6arr", g6arr);
            reData.put("darr", darr);
            reData.put("xarr", xarr);
            reData.put("code", "1");
            reData.put("message", "查询成功");

        } catch (Exception e) {
            logger.error(e.getMessage());
            reData.put("code", "0");
            reData.put("message", "失败");
        }
        return reData;
    }

    @RequestMapping(value = "/getExcel", method = RequestMethod.GET)
    public void getExcel(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort,
                         String order, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExcelExportUtil util = new ExcelExportUtil(ExcelType.XLS);
        List<ExcelCell> list = new ArrayList<ExcelCell>();

        try {
            List<Cltxpf> cltxpfList = new ArrayList<Cltxpf>();
            String strWhere = " 1=1 ";
            if (params.containsKey("jclx") && StringUtils.isNoneBlank(params.get("jclx").toString())) {
                strWhere += String.format(" and jclx='%s'", params.get("jclx").toString());
            }
            if (params.containsKey("organ") && params.get("organ").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("organ").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            } else if (params.containsKey("organ") && (StringUtils.isNoneBlank(params.get("organ").toString()))) {
                String organ = params.get("organ").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and qybh in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and qybh='%s'", organ);
                }
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            String sql = "SELECT e.PFBZ,CLS,JCLC FROM " +
                    "(SELECT DISTINCT PFBZ,COUNT(*) AS CLS FROM (SELECT DISTINCT CPHM,CPYS,isnull(PFBZ,'') as pfbz FROM MJ_DATA_BASE WHERE " + strWhere +
                    " GROUP BY CPHM,CPYS,isnull(PFBZ,''))d GROUP BY PFBZ)e LEFT JOIN(SELECT isnull(PFBZ,'') as pfbz,COUNT(*) AS JCLC " +
                    "FROM MJ_DATA_BASE WHERE " + strWhere + " GROUP BY isnull(PFBZ,''))f " +
                    "ON e.PFBZ=f.pfbz  order by pfbz";
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);

            if (cltxpfs.size() > 0) {
                int inumcls=0;
                int inumjclc=0;
                String pfbz = "0";
                boolean bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "1";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "2";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "3";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                pfbz = "4";
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        inumcls=inumcls+Integer.parseInt(cltxpfs.get(i).getCls());
                        inumjclc=inumjclc+Integer.parseInt(cltxpfs.get(i).getJclc());
                        break;
                    }
                }
                Cltxpf cltxpf1 = new Cltxpf();
                cltxpf1.setPfbz("国Ⅴ以下");
                cltxpf1.setCls(String.valueOf(inumcls));
                cltxpf1.setJclc(String.valueOf(inumjclc));
                cltxpfList.add(cltxpf1);
                pfbz = "5";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("国Ⅴ");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("国Ⅴ");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "6";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("国Ⅵ");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("国Ⅵ");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "D";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals(pfbz)) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("电动");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                if (!bhave) {
                    Cltxpf cltxpf = new Cltxpf();
                    cltxpf.setPfbz("电动");
                    cltxpf.setCls("0");
                    cltxpf.setJclc("0");
                    cltxpfList.add(cltxpf);
                }
                pfbz = "X";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals("X")) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("未知");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                        bhave = true;
                        break;
                    }
                }
                pfbz = "";
                bhave = false;
                for (int i = 0; i < cltxpfs.size(); i++) {
                    if (cltxpfs.get(i).getPfbz().equals("")) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setPfbz("未录入");
                        cltxpf.setCls(cltxpfs.get(i).getCls());
                        cltxpf.setJclc(cltxpfs.get(i).getJclc());
                        cltxpfList.add(cltxpf);
                    }
                }
            }
            int icls = 0;
            int ijclc = 0;
            for (int i = 0; i < cltxpfList.size(); i++) {
                icls = icls + Integer.parseInt(cltxpfList.get(i).getCls());
                ijclc = ijclc + Integer.parseInt(cltxpfList.get(i).getJclc());
            }
            Cltxpf cltxpf = new Cltxpf();
            cltxpf.setPfbz("合计");
            cltxpf.setCls(String.valueOf(icls));
            cltxpf.setJclc(String.valueOf(ijclc));
            cltxpfList.add(cltxpf);

            int row = 0;
            int col = 0;
            list.add(new ExcelCell(row, col++, "排放阶段"));
            list.add(new ExcelCell(row, col++, "车辆数"));
            list.add(new ExcelCell(row, col++, "进出辆次"));
            ConverUtil converUtil = new ConverUtil();
            if (cltxpfList.size() > 0) {
                for (Cltxpf cltxpf1 : cltxpfList) {
                    row++;
                    col = 0;
                    list.add(new ExcelCell(row, col++, cltxpf1.getPfbz()));
                    list.add(new ExcelCell(row, col++, cltxpf1.getCls()));
                    list.add(new ExcelCell(row, col++, cltxpf1.getJclc()));
                }
            }
            Workbook book = util.export(list);
            response.setContentType("application/vnd.ms-excel");
            TimeUtil tm = new TimeUtil();
            String fileName = tm.getRandomFileName() + "车辆通行排放统计";
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
            ServletOutputStream out = response.getOutputStream();
            book.write(out);
            out.close();
        } catch (Exception e) {
            logger.error("导出错误:" + e.getMessage());
            //logger.error(e.getMessage());
            util.createTitle(0, e.getMessage(), 0, 0);
            Workbook book = util.export(list);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=error.xls");
            ServletOutputStream out = response.getOutputStream();
            book.write(out);
            out.close();
        }
    }

}
