package com.controller.count;

import com.commons.base.BaseController;
import com.commons.utils.StringEscapeEditor;
import com.commons.utils.TimeUtil;
import com.model.count.Cltxpf;
import com.model.count.MjCdpf;
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
import org.json.JSONArray;
import org.json.JSONObject;
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
@RequestMapping("/count/clcdpfDetail")
public class ClcdpfDetailController extends BaseController {

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
        return "count/ClcdpfDetailManager";
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
    @RequestMapping(value = "/clqjyswsj", method = RequestMethod.POST)
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
        strWhere+=" and isnull(yshwmc,'')<>'' and yshwmc<>'未知' and yshwmc<>'水泥销售' and yshwmc<>'骨料出厂'";
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
                for(int i=0;i<cltxpfs.size();i++){
                    double yslhj=0;
                    currentYshwmc=cltxpfs.get(i).getYshwmc();
                    if(!currentYshwmc.equals(lastYshwmc)){
                        for(int m=0;m<cltxpfs.size();m++){
                            if(currentYshwmc.equals(cltxpfs.get(m).getYshwmc())){
                                yslhj=yslhj+Float.parseFloat(cltxpfs.get(m).getYsl());
                            }
                        }
                    }
                    Cltxpf cltxpf = new Cltxpf();
                    String pfbz=cltxpfs.get(i).getPfbz();
                    if(pfbz.equals("")){
                        pfbz="未录入";
                    }
                    cltxpf.setYshwmc(currentYshwmc);
                    cltxpf.setPfbz(converUtil.pfsztopfjd(pfbz));
                    cltxpf.setYsl(String.format("%.2f",Float.parseFloat(cltxpfs.get(i).getYsl())));
                    cltxpf.setYslhj(String.format("%.2f",yslhj));
                    String zb=String.format("%.2f", (double)Float.parseFloat(cltxpfs.get(i).getYsl()) / yslhjz*100)+"%";
                    cltxpf.setYslzb(zb);
                    cltxpfList.add(cltxpf);
                }
                if(mjCdpfs!=null&&mjCdpfs.size()>0) {
                    for (MjCdpf mjCdpf : mjCdpfs) {
                        Cltxpf cltxpf = new Cltxpf();
                        cltxpf.setYshwmc(mjCdpf.getYsw());
                        cltxpf.setPfbz("皮带运输");
                        cltxpf.setYsl(String.format("%.2f", (double)Float.parseFloat(mjCdpf.getYsl())));
                        cltxpf.setYslhj(String.format("%.2f", (double)Float.parseFloat(mjCdpf.getYsl())));
                        String zb = String.format("%.2f", (double)Float.parseFloat(mjCdpf.getYsl()) / yslhjz * 100) + "%";
                        cltxpf.setYslzb(zb);
                        cltxpfList.add(cltxpf);
                    }
                }
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
                cltxpf5.setYshwmc("国五车合计（吨）");
                cltxpf5.setYslhj(String.format("%.2f",ysl5));
                qjzb = "0.00%";
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)ysl5 / yslhjz*100)+"%";
                }
                cltxpf5.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpf5);

                Cltxpf cltxpfz = new Cltxpf();
                cltxpfz.setYshwmc("货物运输量（吨）");
                cltxpfz.setYslhj(String.format("%.2f",yslhjz));
                cltxpfList.add(cltxpfz);

                Cltxpf cltxpfzb = new Cltxpf();
                cltxpfzb.setYshwmc("新能源车运输比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)yslxny / yslhjz*100)+"%";
                }
                cltxpfzb.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpfzb);

                Cltxpf cltxpdcs = new Cltxpf();
                cltxpdcs.setYshwmc("皮带传输比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)cdpfz / yslhjz*100)+"%";
                }
                cltxpdcs.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxpdcs);

                Cltxpf cltxcdpf = new Cltxpf();
                cltxcdpf.setYshwmc("超低排放比例（%）");
                if(yslhjz!=0){
                    qjzb=String.format("%.2f", (double)(cdpfz+yslxny) / yslhjz*100)+"%";
                }
                cltxcdpf.setYslzb(String.valueOf(qjzb));
                cltxpfList.add(cltxcdpf);
            }


        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return cltxpfList;
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
