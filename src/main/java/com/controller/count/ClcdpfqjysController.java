package com.controller.count;

import com.commons.base.BaseController;
import com.commons.utils.StringEscapeEditor;
import com.model.count.Cltxpf;
import com.model.count.MjCdpf;
import com.model.login.PieVo;
import com.service.base.PublicService;
import com.service.configmanage.SysCodeService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.util.ConverUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：ai识别结果查看
 */
@Controller
@RequestMapping("/count/clcdpfqjys")
public class ClcdpfqjysController extends BaseController {

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
        return "count/ClcdpfqjysManager";
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

    @RequestMapping(value = "/clqjyssj", method = RequestMethod.POST) // 整车通过率统计
    @ResponseBody
    public Object cltxpfsj(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> reData = new HashMap<String, Object>();
        List<Cltxpf> cltxpfList = new ArrayList<Cltxpf>();
        String strWhere = " 1=1 ";
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
        if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
            String cpys="";
            JSONArray jsonArray = new JSONArray(params.get("cpys").toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.getString("value");
                cpys += value.equals("") ? "" : "'"+value+"',";
                System.out.println(value);
            }
            if (!cpys.equals("")) {
                cpys=cpys.substring(0,cpys.length()-1);
                strWhere += String.format(" and cpys in (%s)", cpys);
            }
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        strWhere+=" and (yshwmc='水泥销售' or yshwmc='水泥产品')";
        String sql = "select distinct yshwmc,isnull(pfbz,'') as pfbz,SUM(CAST(ISNULL(ysl, '0') AS float)) AS ysl from mj_data_base where "+strWhere+
                "  group by yshwmc,pfbz order by yshwmc,isnull(pfbz,'')";
        ConverUtil converUtil=new ConverUtil();
        try {
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);
            String lastYshwmc="";
            String currentYshwmc="";
            double yslhjz=0;
            double yslxny=0;
            double ysl6=0;
            double ysl5=0;
            double cdpfz=0;
            strWhere = " 1=1 ";
            if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("qybh").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
                String organ = params.get("qybh").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and yssj  >= '%s'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and yssj  <= '%s'", params.get("jssj").toString());
            }

            strWhere+=" and (ysw='水泥销售' or ysw='水泥产品')";
            List<MjCdpf> mjCdpfs=publicService.selectObjs("select distinct ysw,SUM(CAST(ISNULL(ysl, '0') AS float)) " +
                    "AS ysl from mj_cdpf where "+strWhere+"  group by ysw",MjCdpf.class);
            if(mjCdpfs!=null&&mjCdpfs.size()>0){
                for (MjCdpf mjCdpf : mjCdpfs) {
                    cdpfz=cdpfz+Float.parseFloat(mjCdpf.getYsl());
                }

            }
            if (cltxpfs.size() > 0) {
                for(int i=0;i<cltxpfs.size();i++){
                    yslhjz=yslhjz+Float.parseFloat(cltxpfs.get(i).getYsl());
                    String pfbz=cltxpfs.get(i).getPfbz();
                    if(pfbz.equals("D")) {
                        yslxny=yslxny+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else if(pfbz.equals("5")) {
                        ysl5=ysl5+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else if(pfbz.equals("6")) {
                        ysl6=ysl6+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                }
                yslhjz=yslhjz+cdpfz;
                Cltxpf cltxpf = new Cltxpf();
                cltxpf.setYshwmc("新能源车合计（吨）");
                cltxpf.setYslhj(String.format("%.2f",yslxny));
                String qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)yslxny / yslhjz*100)+"%";
                }
                cltxpf.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf);

                Cltxpf cltxpf6 = new Cltxpf();
                cltxpf6.setYshwmc("国六车合计（吨）");
                cltxpf6.setYslhj(String.format("%.2f",ysl6));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)ysl6 / yslhjz*100)+"%";
                }
                cltxpf6.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf6);

                Cltxpf cltxpf5 = new Cltxpf();
                cltxpf5.setYshwmc("其他（吨）");
                cltxpf5.setYslhj(String.format("%.2f",ysl5));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)ysl5 / yslhjz*100)+"%";
                }
                cltxpf5.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf5);

                Cltxpf cltxpfcd = new Cltxpf();
                cltxpfcd.setYshwmc("皮带传输量（吨）");
                cltxpfcd.setYslhj(String.format("%.2f",cdpfz));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)cdpfz / yslhjz*100)+"%";
                }
                cltxpfcd.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpfcd);

                Cltxpf cltxpfz = new Cltxpf();
                cltxpfz.setYshwmc("货物运输量（吨）");
                cltxpfz.setYslhj(String.format("%.2f",yslhjz));
                cltxpfList.add(cltxpfz);

                Cltxpf cltxpfzb = new Cltxpf();
                cltxpfzb.setYshwmc("新能源车运输比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)yslxny / yslhjz*100)+"%";
                }
                cltxpfzb.setYslhj(String.valueOf(qjzb));
                cltxpfList.add(cltxpfzb);

                Cltxpf cltxpdcs = new Cltxpf();
                cltxpdcs.setYshwmc("皮带传输比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)cdpfz / yslhjz*100)+"%";
                }
                cltxpdcs.setYslhj(String.valueOf(qjzb));
                cltxpfList.add(cltxpdcs);

                Cltxpf cltxcdpf = new Cltxpf();
                cltxcdpf.setYshwmc("超低排放比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)(cdpfz+yslxny) / yslhjz*100)+"%";
                }
                cltxcdpf.setYslhj(String.valueOf(qjzb));
                cltxpfList.add(cltxcdpf);
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cltxpfList;
    }
    @RequestMapping(value = "/clqjyswsj", method = RequestMethod.POST)
    @ResponseBody
    public Object cltxpfsjysw(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Map<String, Object> reData = new HashMap<String, Object>();
        List<Cltxpf> cltxpfList = new ArrayList<Cltxpf>();
        String strWhere = " 1=1 ";
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
        if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
            String cpys="";
            JSONArray jsonArray = new JSONArray(params.get("cpys").toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.getString("value");
                cpys += value.equals("") ? "" : "'"+value+"',";
                System.out.println(value);
            }
            if (!cpys.equals("")) {
                cpys=cpys.substring(0,cpys.length()-1);
                strWhere += String.format(" and cpys in (%s)", cpys);
            }
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        strWhere+=" and isnull(yshwmc,'')<>'' and yshwmc<>'未知' and yshwmc<>'水泥销售' and yshwmc<>'骨料出厂' and yshwmc<>'水泥产品'";
        String sql = "select distinct yshwmc,isnull(pfbz,'') as pfbz,SUM(CAST(ISNULL(ysl, '0') AS float)) AS ysl from mj_data_base where "+strWhere+
                "  group by yshwmc,pfbz order by yshwmc,isnull(pfbz,'')";
        ConverUtil converUtil=new ConverUtil();
        try {
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);
            String lastYshwmc="";
            String currentYshwmc="";
            double yslhjz=0;
            double yslxny=0;
            double ysl6=0;
            double ysl5=0;
            double cdpfz=0;
            strWhere = " 1=1 ";
            if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("qybh").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
                String organ = params.get("qybh").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and yssj  >= '%s'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and yssj  <= '%s'", params.get("jssj").toString());
            }
            strWhere+=" and ysw<>'未知' and ysw<>'水泥销售' and ysw<>'骨料出厂' and ysw<>'水泥产品'";
            List<MjCdpf> mjCdpfs=publicService.selectObjs("select distinct ysw,SUM(CAST(ISNULL(ysl, '0') AS float)) " +
                    "AS ysl from mj_cdpf where "+strWhere+"  group by ysw",MjCdpf.class);
            if(mjCdpfs!=null&&mjCdpfs.size()>0){
                for (MjCdpf mjCdpf : mjCdpfs) {
                    cdpfz=cdpfz+Float.parseFloat(mjCdpf.getYsl());
                }

            }
            if (cltxpfs.size() > 0) {
                for(int i=0;i<cltxpfs.size();i++){
                    yslhjz=yslhjz+Float.parseFloat(cltxpfs.get(i).getYsl());
                    String pfbz=cltxpfs.get(i).getPfbz();
                    if(pfbz.equals("D")) {
                        yslxny=yslxny+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else if(pfbz.equals("5")) {
                        ysl5=ysl5+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else if(pfbz.equals("6")) {
                        ysl6=ysl6+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                }
                yslhjz=yslhjz+cdpfz;
                Cltxpf cltxpf = new Cltxpf();
                cltxpf.setYshwmc("新能源车合计（吨）");
                cltxpf.setYslhj(String.format("%.2f",yslxny));
                String qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)yslxny / yslhjz*100)+"%";
                }
                cltxpf.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf);

                Cltxpf cltxpf6 = new Cltxpf();
                cltxpf6.setYshwmc("国六车合计（吨）");
                cltxpf6.setYslhj(String.format("%.2f",ysl6));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)ysl6 / yslhjz*100)+"%";
                }
                cltxpf6.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf6);

                Cltxpf cltxpf5 = new Cltxpf();
                cltxpf5.setYshwmc("其他（吨）");
                cltxpf5.setYslhj(String.format("%.2f",ysl5));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)ysl5 / yslhjz*100)+"%";
                }
                cltxpf5.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf5);

                Cltxpf cltxpfcd = new Cltxpf();
                cltxpfcd.setYshwmc("皮带传输量（吨）");
                cltxpfcd.setYslhj(String.format("%.2f",cdpfz));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)cdpfz / yslhjz*100)+"%";
                }
                cltxpfcd.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpfcd);

                Cltxpf cltxpfz = new Cltxpf();
                cltxpfz.setYshwmc("货物运输量（吨）");
                cltxpfz.setYslhj(String.format("%.2f",yslhjz));
                cltxpfList.add(cltxpfz);

                Cltxpf cltxpfzb = new Cltxpf();
                cltxpfzb.setYshwmc("新能源车运输比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)yslxny / yslhjz*100)+"%";
                }
                cltxpfzb.setYslhj(String.valueOf(qjzb));
                cltxpfList.add(cltxpfzb);

                Cltxpf cltxpdcs = new Cltxpf();
                cltxpdcs.setYshwmc("皮带传输比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)cdpfz / yslhjz*100)+"%";
                }
                cltxpdcs.setYslhj(String.valueOf(qjzb));
                cltxpfList.add(cltxpdcs);

                Cltxpf cltxcdpf = new Cltxpf();
                cltxcdpf.setYshwmc("超低排放比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)(cdpfz+yslxny) / yslhjz*100)+"%";
                }
                cltxcdpf.setYslhj(String.valueOf(qjzb));
                cltxpfList.add(cltxcdpf);
            }


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
        if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
            String cpys="";
            JSONArray jsonArray = new JSONArray(params.get("cpys").toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.getString("value");
                cpys += value.equals("") ? "" : "'"+value+"',";
                System.out.println(value);
            }
            if (!cpys.equals("")) {
                cpys=cpys.substring(0,cpys.length()-1);
                strWhere += String.format(" and cpys in (%s)", cpys);
            }
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        strWhere+=" and (yshwmc='水泥销售' or yshwmc='水泥产品')";
        String sql = "select distinct yshwmc,isnull(pfbz,'') as pfbz,SUM(CAST(ISNULL(ysl, '0') AS float)) AS ysl from mj_data_base where "+strWhere+
                "  group by yshwmc,pfbz order by yshwmc,isnull(pfbz,'')";
        ConverUtil converUtil=new ConverUtil();
        try {
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);
            String lastYshwmc="";
            String currentYshwmc="";
            double yslhjz=0;
            double yslxny=0;
            double ysl6=0;
            double ysl5=0;
            double cdpfz=0;
            strWhere = " 1=1 ";
            if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("qybh").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
                String organ = params.get("qybh").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and yssj  >= '%s'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and yssj  <= '%s'", params.get("jssj").toString());
            }
            strWhere+=" and (ysw='水泥销售' or ysw='水泥产品')";
            List<MjCdpf> mjCdpfs=publicService.selectObjs("select distinct ysw,SUM(CAST(ISNULL(ysl, '0') AS float)) " +
                    "AS ysl from mj_cdpf where "+strWhere+"  group by ysw",MjCdpf.class);
            if(mjCdpfs!=null&&mjCdpfs.size()>0){
                for (MjCdpf mjCdpf : mjCdpfs) {
                    cdpfz=cdpfz+Float.parseFloat(mjCdpf.getYsl());
                }

            }
            pfbzVos = publicService.selectObjs(sql, PieVo.class);
            List<Integer> g5arr = new ArrayList<Integer>();
            List<Integer> g6arr = new ArrayList<Integer>();
            List<Integer> darr = new ArrayList<Integer>();
            List<Integer> cdarr = new ArrayList<Integer>();
            if (cltxpfs.size() > 0) {
                for(int i=0;i<cltxpfs.size();i++){
                    yslhjz=yslhjz+Float.parseFloat(cltxpfs.get(i).getYsl());
                    String pfbz=cltxpfs.get(i).getPfbz();
                    if(pfbz.equals("D")) {
                        yslxny=yslxny+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else if(pfbz.equals("6")) {
                        ysl6=ysl6+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else{
                        ysl5=ysl5+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                }
                yslhjz=yslhjz+cdpfz;

                g5arr.add((int) ysl5);
                g6arr.add((int) ysl6);
                darr.add((int) yslxny);
                cdarr.add((int) cdpfz);
                reData.put("g5arr", g5arr);
                reData.put("g6arr", g6arr);
                reData.put("darr", darr);
                reData.put("cdarr", cdarr);
                reData.put("code", "1");
                reData.put("message", "查询成功");
            }

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
        List<PieVo> pfbzVos = new ArrayList<PieVo>();
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
        if (params.containsKey("cpys") && StringUtils.isNoneBlank(params.get("cpys").toString())) {
            String cpys="";
            JSONArray jsonArray = new JSONArray(params.get("cpys").toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String value = jsonObject.getString("value");
                cpys += value.equals("") ? "" : "'"+value+"',";
                System.out.println(value);
            }
            if (!cpys.equals("")) {
                cpys=cpys.substring(0,cpys.length()-1);
                strWhere += String.format(" and cpys in (%s)", cpys);
            }
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and tgkssj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and tgkssj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        strWhere+=" and isnull(yshwmc,'')<>'' and yshwmc<>'未知' and yshwmc<>'水泥销售' and yshwmc<>'骨料出厂'  and yshwmc<>'水泥产品'";
        String sql = "select distinct yshwmc,isnull(pfbz,'') as pfbz,SUM(CAST(ISNULL(ysl, '0') AS float)) AS ysl from mj_data_base where "+strWhere+
                "  group by yshwmc,pfbz order by yshwmc,isnull(pfbz,'')";
        ConverUtil converUtil=new ConverUtil();
        try {
            List<Cltxpf> cltxpfs = publicService.selectObjs(sql, Cltxpf.class);
            String lastYshwmc="";
            String currentYshwmc="";
            double yslhjz=0;
            double yslxny=0;
            double ysl6=0;
            double ysl5=0;
            double cdpfz=0;
            strWhere = " 1=1 ";
            if (params.containsKey("qybh") && params.get("qybh").toString().equals("")) {
                String organ = getCurrentUserOrganCode();
                String strOrgans = sysOrganizationService.findChildrenCodes(getCurrentUserOrganCode());
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("qybh").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            } else if (params.containsKey("qybh") && (StringUtils.isNoneBlank(params.get("qybh").toString()))) {
                String organ = params.get("qybh").toString();
                String strOrgans = sysOrganizationService.findChildrenCodes(organ);
                if (StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = organ + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", organ);
                }
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and yssj  >= '%s'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and yssj  <= '%s'", params.get("jssj").toString());
            }
            strWhere+=" and ysw<>'未知' and ysw<>'水泥销售' and ysw<>'骨料出厂' and ysw<>'水泥产品'";
            List<MjCdpf> mjCdpfs=publicService.selectObjs("select distinct ysw,SUM(CAST(ISNULL(ysl, '0') AS float)) " +
                    "AS ysl from mj_cdpf where "+strWhere+"  group by ysw",MjCdpf.class);
            if(mjCdpfs!=null&&mjCdpfs.size()>0){
                for (MjCdpf mjCdpf : mjCdpfs) {
                    cdpfz=cdpfz+Float.parseFloat(mjCdpf.getYsl());
                }

            }
            pfbzVos = publicService.selectObjs(sql, PieVo.class);
            List<Integer> g5arr = new ArrayList<Integer>();
            List<Integer> g6arr = new ArrayList<Integer>();
            List<Integer> darr = new ArrayList<Integer>();
            List<Integer> cdarr = new ArrayList<Integer>();
            if (cltxpfs.size() > 0) {
                for(int i=0;i<cltxpfs.size();i++){
                    yslhjz=yslhjz+Float.parseFloat(cltxpfs.get(i).getYsl());
                    String pfbz=cltxpfs.get(i).getPfbz();
                    if(pfbz.equals("D")) {
                        yslxny=yslxny+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else if(pfbz.equals("6")) {
                        ysl6=ysl6+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                    else{
                        ysl5=ysl5+Float.parseFloat(cltxpfs.get(i).getYsl());
                    }
                }
                yslhjz=yslhjz+cdpfz;

                g5arr.add((int) ysl5);
                g6arr.add((int) ysl6);
                darr.add((int) yslxny);
                cdarr.add((int) cdpfz);
                reData.put("g5arr", g5arr);
                reData.put("g6arr", g6arr);
                reData.put("darr", darr);
                reData.put("cdarr", cdarr);
                reData.put("code", "1");
                reData.put("message", "查询成功");
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            reData.put("code", "0");
            reData.put("message", "失败");
        }
        return reData;
    }

}
