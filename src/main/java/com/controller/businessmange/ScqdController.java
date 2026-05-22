package com.controller.businessmange;

import com.commons.base.BaseController;
import com.commons.utils.StringEscapeEditor;
import com.service.base.PublicService;
import com.service.businessmanage.PfbzService;
import com.service.configmanage.SysOptionService;
import com.service.sysmanage.SysOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description：ai识别结果查看
 */
@Controller
@RequestMapping("/scqdapi")
public class ScqdController extends BaseController {

    @Autowired
    public PublicService publicService;

    @Autowired
    public SysOrganizationService sysOrganizationService;

    @Autowired
    public SysOptionService sysOptionService;
    @Autowired
    public PfbzService pfbzService;

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

    @RequestMapping(value = "/vin", method = RequestMethod.GET) // 任务统计
    @ResponseBody
    public String getScqd(@RequestParam("token") String token,
                          @RequestParam("vin") String vin) throws Exception {
        String srt="";
        SimpleDateFormat f = new SimpleDateFormat("HH");
        Date now = new Date();
        String time = f.format(now);
        int itime=Integer.parseInt(time);
        if((itime>7)&&(itime<20)){
            srt="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><vehicles><result>false</result>" +
                    "<vehicle>非调用时间段，不可调用</vehicle></vehicles>";
        }
        if (!token.equals("3b57eeeee9303e1c164edf72a2f61a25")){
            srt="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><vehicles><result>false</result>" +
                    "<vehicle>非法授权，不可调用</vehicle></vehicles>";
        }
        else {
            srt=pfbzService.getScqd(vin);
            if(srt.equals("")){
                srt="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><vehicles><result>false</result>" +
                        "<vehicle>调取失败</vehicle></vehicles>";
            }
        }
        return  srt;
    }

}
