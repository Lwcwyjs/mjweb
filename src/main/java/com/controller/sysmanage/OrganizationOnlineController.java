package com.controller.sysmanage;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.model.sysmanage.MjOnlineQy;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description：部门管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/organizationOnline")
public class OrganizationOnlineController extends BaseController {

    @Autowired
    @Resource
    private SysOrganizationService sysOrganizationService;
    @Autowired
    private PublicService publicService;
    @Autowired
    private SysOrganizationService organService;

    /**
     * 部门管理主页
     *
     * @return
     */
    @RequestMapping("/manager")
    public String manager(HttpServletRequest request) {
        request.setAttribute("organ", getCurrentUserOrganCode());
        return "sysmanage/sysOrganizationOnline";
    }

    @RequestMapping(value = "/DataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        try {
            PageInfo pageInfo = new PageInfo(page, rows, sort, order);
            String strWhere = "";
            if (params.containsKey("qybh") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("qybh").toString())) {
                String strOrgans = organService.findChildrenCodes(params.get("qybh").toString());
                if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = params.get("qybh").toString() + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", params.get("qybh").toString());
                }
            } else {
                String strCurrentOrgan = getCurrentUserOrganCode();
                String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
                if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                    strOrgans = strCurrentOrgan + "," + strOrgans;
                    strOrgans = strOrgans.replace(",", "','");
                    strWhere += String.format(" and organ in ('%s')", strOrgans);
                } else {
                    strWhere += String.format(" and organ='%s'", strCurrentOrgan);
                }

            }
            if (params.containsKey("isonline") && StringUtils.isNoneBlank(params.get("isonline").toString())) {
                strWhere += String.format(" and isonline='%s'",
                        params.get("isonline").toString());
            }
            String tblName = "mj_online_qy";
            logger.info(tblName);
            publicService.selectPageObjects(pageInfo, tblName, "*", strWhere, "organ");
            pageInfo.setCode(0);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return renderError(e.getMessage());
        }
    }
    @RequestMapping(value = "/getExcel", method = RequestMethod.GET)
    public void getExcel(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort,
                         String order, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExcelExportUtil util = new ExcelExportUtil(ExcelType.XLS);
        List<ExcelCell> list = new ArrayList<ExcelCell>();
        String strWhere = " 1=1 ";
        if (params.containsKey("pfbz") && StringUtils.isNoneBlank(params.get("pfbz").toString())) {
            strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
        }
        if (params.containsKey("qybh") && org.apache.commons.lang3.StringUtils.isNoneBlank(params.get("qybh").toString())) {
            String strOrgans = organService.findChildrenCodes(params.get("qybh").toString());
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = params.get("qybh").toString() + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and organ in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and organ='%s'", params.get("qybh").toString());
            }
        } else {
            String strCurrentOrgan = getCurrentUserOrganCode();
            String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
            if (org.apache.commons.lang3.StringUtils.isNoneBlank(strOrgans)) {
                strOrgans = strCurrentOrgan + "," + strOrgans;
                strOrgans = strOrgans.replace(",", "','");
                strWhere += String.format(" and organ in ('%s')", strOrgans);
            } else {
                strWhere += String.format(" and organ='%s'", strCurrentOrgan);
            }

        }
        if (params.containsKey("isonline") && StringUtils.isNoneBlank(params.get("isonline").toString())) {
            strWhere += String.format(" and isonline='%s'",
                    params.get("isonline").toString());
        }
        SimpleDateFormat sdfsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String sql="select * from mj_online_qy  where "+strWhere+" order by organ";
            List<MjOnlineQy>  mjOnlineQIES= publicService.selectObjs(sql,MjOnlineQy.class);

            int row = 0;
            int col = 0;
            list.add(new ExcelCell(row, col++, "企业编号"));
            list.add(new ExcelCell(row, col++, "企业名称"));
            list.add(new ExcelCell(row, col++, "在线状态"));
            list.add(new ExcelCell(row, col++, "更新时间"));
            ConverUtil converUtil=new ConverUtil();
            if (mjOnlineQIES.size()>0) {
                for (MjOnlineQy mjOnlineQy:mjOnlineQIES) {
                    row++;
                    col = 0;
                    list.add(new ExcelCell(row, col++, mjOnlineQy.getOrgan()));
                    list.add(new ExcelCell(row, col++, mjOnlineQy.getName()));
                    list.add(new ExcelCell(row, col++, getzxzt(mjOnlineQy.getIsonline())));
                    list.add(new ExcelCell(row, col++, sdfsj.format(mjOnlineQy.getOnlinetime())));
                }
            }
            Workbook book = util.export(list);
            response.setContentType("application/vnd.ms-excel");
            TimeUtil tm=new TimeUtil();
            String fileName=tm.getRandomFileName()+"企业在线状态";
            response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
            ServletOutputStream out = response.getOutputStream();
            book.write(out);
            out.close();
            request.setAttribute("msg","导出成功");
        }catch(Exception e){
            logger.error("导出错误:"+e.getMessage());
            request.setAttribute("msg","导出失败");
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
    private String getzxzt(String zxzt) {
        String result="";
        if(zxzt.equals("-1")){
            result="离线";
        }
        else if(zxzt.equals("1")){
            result="在线";
        }
        else if(zxzt.equals("0")){
            result="离线";
        }
        return  result;
    }
}
