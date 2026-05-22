package com.controller.sysmanage;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.commons.utils.TimeUtil;
import com.model.sysmanage.MjOnlineDz;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/dzxxOnline")
public class DzxxOnlineController extends BaseController {

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
        return "sysmanage/DzxxOnline";
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
            if (params.containsKey("dzbh") && StringUtils.isNoneBlank(params.get("dzbh").toString())) {
                strWhere += String.format(" and dzbh like '%%%s%%'",
                        params.get("dzbh").toString());
            }
            if (params.containsKey("dzzt") && StringUtils.isNoneBlank(params.get("dzzt").toString())) {
                strWhere += String.format(" and isonline='%s'",
                        params.get("dzzt").toString());
            }
            String tblName = "mj_online_dz";
            logger.info(tblName);
            publicService.selectPageObjects(pageInfo, tblName, "*", strWhere, "organ,dzbh");
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
        if (params.containsKey("dzbh") && StringUtils.isNoneBlank(params.get("dzbh").toString())) {
            strWhere += String.format(" and dzbh like '%%%s%%'",
                    params.get("dzbh").toString());
        }
        if (params.containsKey("dzzt") && StringUtils.isNoneBlank(params.get("dzzt").toString())) {
            strWhere += String.format(" and isonline='%s'",
                    params.get("dzzt").toString());
        }
        SimpleDateFormat sdfsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            String sql="select * from mj_online_dz  where "+strWhere+" order by organ";
            List<MjOnlineDz>  mjOnlineDzs= publicService.selectObjs(sql,MjOnlineDz.class);

            int row = 0;
            int col = 0;
            list.add(new ExcelCell(row, col++, "企业编号"));
            list.add(new ExcelCell(row, col++, "企业名称"));
            list.add(new ExcelCell(row, col++, "道闸编号"));
            list.add(new ExcelCell(row, col++, "道闸名称"));
            list.add(new ExcelCell(row, col++, "在线状态"));
            list.add(new ExcelCell(row, col++, "更新时间"));
            ConverUtil converUtil=new ConverUtil();
            if (mjOnlineDzs.size()>0) {
                for (MjOnlineDz mjOnlineDz:mjOnlineDzs) {
                    row++;
                    col = 0;
                    list.add(new ExcelCell(row, col++, mjOnlineDz.getOrgan()));
                    list.add(new ExcelCell(row, col++, mjOnlineDz.getName()));
                    list.add(new ExcelCell(row, col++, mjOnlineDz.getDzbh()));
                    list.add(new ExcelCell(row, col++, mjOnlineDz.getDzmc()));
                    list.add(new ExcelCell(row, col++, getzxzt(mjOnlineDz.getIsonline())));
                    list.add(new ExcelCell(row, col++, sdfsj.format(mjOnlineDz.getOnlinetime())));
                }
            }
            Workbook book = util.export(list);
            response.setContentType("application/vnd.ms-excel");
            TimeUtil tm=new TimeUtil();
            String fileName=tm.getRandomFileName()+"道闸在线状态";
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
            result="开";
        }
        else if(zxzt.equals("0")){
            result="关";
        }
        return  result;
    }
}
