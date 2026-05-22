package com.controller.vehmanage;

import com.commons.annotation.support.ValidateService;
import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.vehmanage.MjInsideVehicle;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/business/mjinsidevehicle")
public class MjInsideVehicleController extends BaseController {

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
    private final SimpleDateFormat shortDateFormater = new SimpleDateFormat("yyyy-MM-dd");


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
        return "vehmanage/MjInsideVehicle";
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Object add(MjInsideVehicle mjInsideVehicle) {
        List<String> bindResult = new ArrayList<String>();

        try {
            mjInsideVehicle.setCjsj(new Date());
            UUID uuid = UUID.randomUUID();
            String uuids = uuid.toString().replaceAll("-", "");
            mjInsideVehicle.setId(uuids);

            bindResult = ValidateService.valid(mjInsideVehicle);

            if (bindResult.size() == 0) {
                mjInsideVehicle.setSczt("0");
                // 验证用户是否已存在
                MjInsideVehicle mjInsideVehicle1 = publicService.selectObj("mj_inside_vehicle", "", "clsbdh", mjInsideVehicle);
                if (mjInsideVehicle1 != null) {
                    bindResult.add("该车辆识别代号已存在，不允许重复创建");
                }
                if (bindResult.size() == 0) {

                    publicService.insert("mj_inside_vehicle", "", "", mjInsideVehicle);

                    publicService.insertLogInfo("新建场内车辆信息【" +
                                    mjInsideVehicle.getClsbdh() + "】场内车辆信息",
                            "新增", "1", "场内车辆信息");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "新建场内车辆信息失败【" + mjInsideVehicle.getClsbdh() + "】场内车辆信息",
                    "新增", "0", "场内车辆信息");
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("新建成功！") : renderError(bindResult.toString());
    }

    @RequestMapping(value = "/dataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object dataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        String strWhere = "";

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
        if (params.containsKey("pfbz") && StringUtils.isNoneBlank(params.get("pfbz").toString())) {
            strWhere += String.format(" and pfbz='%s'", params.get("pfbz").toString());
        }
        if (params.containsKey("cphm") && StringUtils.isNoneBlank(params.get("cphm").toString())) {
            strWhere += " and cphm like '%" + params.get("cphm").toString() + "%'";
        }
        if (params.containsKey("hbdjhm") && StringUtils.isNoneBlank(params.get("hbdjhm").toString())) {
            strWhere += " and hbdjhm like '%" + params.get("hbdjhm").toString() + "%'";
        }
        if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
            strWhere += String.format(" and cjsj  > '%s 00:00:00'", params.get("kssj").toString());
        }
        if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
            strWhere += String.format(" and cjsj  <= '%s 23:59:59'", params.get("jssj").toString());
        }
        String field = "*";
        publicService.selectPageObjects(pageInfo, "mj_inside_vehicle", field, strWhere, "cjsj");
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
        return "vehmanage/MjInsideVehicleEdit";
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
        MjInsideVehicle mjInsideVehicle = new MjInsideVehicle();
        mjInsideVehicle.setId(id);
        mjInsideVehicle = publicService.selectObj("mj_inside_vehicle", "", "id", mjInsideVehicle);
        String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
        if (mjInsideVehicle.getXszzp()!=null&&!mjInsideVehicle.getXszzp().equals("")) {
            mjInsideVehicle.setXszzp(photourl+mjInsideVehicle.getXszzp().replace(photourl, ""));
        }
        if (mjInsideVehicle.getScqdzp()!=null&&!mjInsideVehicle.getScqdzp().equals("")) {
            mjInsideVehicle.setScqdzp(photourl+mjInsideVehicle.getScqdzp().replace(photourl, ""));
        }
        request.setAttribute("mjInsideVehicle", mjInsideVehicle);
        String organname = sysOrganizationService.getQyjcByQybh(mjInsideVehicle.getQybh());
        request.setAttribute("organname", organname);
        request.setAttribute("oper", "edit");

        return "vehmanage/MjInsideVehicleEdit";
    }
    @RequestMapping(value="/sendscqdzp", method = RequestMethod.POST)
    @ResponseBody
    public Object sendscqdzp(@RequestParam("file") MultipartFile file) {
        List<String> bindResult = new ArrayList<String>();
        try {
            logger.info("sendscqdzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "scqd/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/scqd/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/scqd/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            //上传
            if (upload(file, filename)) {
                renderSuccess(zpurl);
            }
            else{
                bindResult.add("保存失败");
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            bindResult.add("异常：" + e.getMessage());
            logger.info("保存行驶证失败:" + e.getMessage());
        }
        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
    }
    @RequestMapping(value = "/sendxszzp", method = RequestMethod.POST)
    @ResponseBody
    public Object sendxszzp(@RequestParam("file") MultipartFile file) {
        List<String> bindResult = new ArrayList<String>();
        try {
            logger.info("sendxszzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "xsz/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/xsz/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/xsz/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            //上传
            if (upload(file, filename)) {
                renderSuccess(zpurl);
            }
            else{
                bindResult.add("保存失败");
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            bindResult.add("异常：" + e.getMessage());
            logger.info("保存行驶证失败:" + e.getMessage());
        }
        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
    }

    public boolean upload(MultipartFile file, String fileName) throws Exception {
        logger.info("保存照片:" + fileName);
        boolean brt = false;
        try {
            File dest = new File(fileName);
            // 判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            // 保存文件
            file.transferTo(dest);
            logger.info("保存照片成功");
            brt = true;
        } catch (FrameGrabber.Exception e) {
            logger.info("保存文件异常11:" + e.getMessage());
            logger.error("保存文件异常11:" + e.getMessage());
        }
        return true;
    }

    /**
     * 编辑用户
     *
     * @param
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(MjInsideVehicle mjInsideVehicle) {
        List<String> bindResult = new ArrayList<String>();

        try {
            mjInsideVehicle.setSczt("0");
            bindResult = ValidateService.valid(mjInsideVehicle);

            if (bindResult.size() == 0) {
                publicService.update("mj_inside_vehicle", "", "id", mjInsideVehicle);

                publicService.insertLogInfo("编辑，成功修改【" + "车辆识别代号："
                                + mjInsideVehicle.getClsbdh() + "】的场内车辆信息",
                        "编辑", "1", "场内车辆信息管理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo(
                    "编辑，修改失败【" + "车辆识别代号："
                            + mjInsideVehicle.getClsbdh() + "】的场内车辆信息",
                    "编辑", "0", "场内车辆信息管理");;
            bindResult.add("异常：" + e.getMessage());
        }

        return bindResult.size() == 0 ? renderSuccess("成功修改！") : renderError(bindResult.toString());
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
            MjInsideVehicle mjInsideVehicle = new MjInsideVehicle();
            mjInsideVehicle.setId(id);
            publicService.delete("mj_inside_vehicle", "", "id", mjInsideVehicle);
            publicService.insertLogInfo(
                    "编辑，成功修改【" + "id："
                            + mjInsideVehicle.getId() + "】的场内车辆信息",
                    "删除", "1",  "场内车辆信息管理");
            return renderSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            publicService.insertLogInfo("删除失败，【" + "id："
                            + id+ "】的场内车辆信息",
                    "删除", "0",  "场内车辆信息管理");

            return renderError("删除异常：" + e.getMessage());
        }
    }
}
