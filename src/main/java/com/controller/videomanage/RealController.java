package com.controller.videomanage;

import com.commons.base.BaseController;
import com.commons.result.TreeDz;
import com.commons.utils.StringEscapeEditor;
import com.model.sysmanage.SysOrganization;
import com.model.videomanage.MjSpxx;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysRoleService;
import com.service.sysmanage.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：用户管理
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
@RequestMapping("/admin/realplay")
public class RealController extends BaseController {

    private volatile boolean isRunning = true;
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
        String strCurrentOrgan = getCurrentUserOrganCode();
        request.setAttribute("qybh",strCurrentOrgan );
        return "videomanage/RealPlay";
    }
    @RequestMapping(value = "/playvlc", method = RequestMethod.POST)
    @ResponseBody
    public Object playvlc(HttpServletRequest request) throws IOException, InterruptedException {
        Map<String, Object> data = new HashMap<String, Object>();
        String id = request.getParameter("id");
        MjSpxx mjSpxx = new MjSpxx();
        mjSpxx.setId(id);
        mjSpxx =publicService.selectObj("mj_spxx","","id",mjSpxx);
        String rtspUrl = mjSpxx.getRealurl();
        data.put("m3u8Url", rtspUrl);
        return data;
    }
    public void stopTranscoding() {
        isRunning = false; // 修改标志位为 false
    }
    @RequestMapping(value = "/dzTrees/{qybh}", method = RequestMethod.POST)
    @ResponseBody
    public Object dzTrees(@PathVariable String qybh, HttpServletRequest request) {
        List<TreeDz> treeOneList = new ArrayList<TreeDz>();
        TreeDz treeCommon = new TreeDz();
        SysOrganization sysOrganization=new SysOrganization();
        sysOrganization.setOrgan(qybh);
        sysOrganization=publicService.selectObj("ai_sys_organization","","organ",sysOrganization);
        if(sysOrganization!=null) {
            treeCommon.setId(sysOrganization.getOrgan());
            treeCommon.setText(sysOrganization.getJc());
            treeCommon.setTitle(sysOrganization.getJc());
            treeCommon.setSpread(false);
            treeCommon.setIconCls("icon-sz");
            treeCommon.setAttributes(null);
            if(sysOrganization.getOrgantype().equals("0")){
                List<SysOrganization> sysOrganizations=publicService.selectObjs("select * from ai_sys_organization where porgan='"+qybh+"'",SysOrganization.class);
                if(sysOrganizations.size()>0){
                    List<TreeDz> treesOne=new ArrayList<TreeDz>();
                    for(SysOrganization sysOrganizationOne:sysOrganizations) {
                        TreeDz treeOne = new TreeDz();
                        treeOne.setId(sysOrganizationOne.getOrgan());
                        treeOne.setText(sysOrganizationOne.getJc());
                        treeOne.setTitle(sysOrganizationOne.getJc());
                        treeOne.setSpread(false);
                        treeOne.setIconCls("icon-sz");
                        treeOne.setAttributes(null);
                        if(sysOrganizationOne.getOrgantype().equals("1")){
                            List<SysOrganization> sysOrganizationList=publicService.selectObjs("select * from ai_sys_organization where porgan='"+sysOrganizationOne.getOrgan()+"'",SysOrganization.class);
                            if(sysOrganizationList.size()>0){
                                List<TreeDz> treesTwo=new ArrayList<TreeDz>();
                                for(SysOrganization sysOrganizationTwo:sysOrganizationList) {
                                    TreeDz treeTwo = new TreeDz();
                                    treeTwo.setId(sysOrganizationTwo.getOrgan());
                                    treeTwo.setText(sysOrganizationTwo.getJc());
                                    treeTwo.setTitle(sysOrganizationTwo.getJc());
                                    treeTwo.setSpread(false);
                                    treeTwo.setIconCls("icon-sz");
                                    treeTwo.setAttributes(null);
                                    if(sysOrganizationTwo.getOrgantype().equals("2")){
                                        List<SysOrganization> sysOrganizationList1=publicService.selectObjs("select * from ai_sys_organization where porgan='"+sysOrganizationTwo.getOrgan()+"'",SysOrganization.class);
                                        if(sysOrganizationList1.size()>0) {
                                            List<TreeDz> treesThree = new ArrayList<TreeDz>();
                                            for (SysOrganization sysOrganizationThree : sysOrganizationList1) {
                                                TreeDz treeThree = new TreeDz();
                                                treeThree.setId(sysOrganizationThree.getOrgan());
                                                treeThree.setText(sysOrganizationThree.getJc());
                                                treeThree.setTitle(sysOrganizationThree.getJc());
                                                treeThree.setSpread(false);
                                                treeThree.setIconCls("icon-sz");
                                                treeThree.setAttributes(null);
                                                if (sysOrganizationThree.getOrgantype().equals("3")) {
                                                    List<MjSpxx> mjSpxxes = publicService.selectObjs("select * from mj_spxx where qybh='" + sysOrganizationThree.getOrgan() + "'", MjSpxx.class);
                                                    if (mjSpxxes.size() > 0) {
                                                        List<TreeDz> treesFour = new ArrayList<TreeDz>();
                                                        for (MjSpxx mjSpxx : mjSpxxes) {
                                                            TreeDz treeFour = new TreeDz();
                                                            treeFour.setId(mjSpxx.getId());
                                                            treeFour.setText(mjSpxx.getSxtmc());
                                                            treeFour.setTitle(mjSpxx.getSxtmc());
                                                            treeFour.setSpread(false);
                                                            treeFour.setIconCls("ipc");
                                                            treeFour.setAttributes(null);
                                                            treesFour.add(treeFour);
                                                        }
                                                        treeThree.setChildren(treesFour);
                                                    }
                                                }
                                                treesThree.add(treeThree);
                                            }
                                            treeTwo.setChildren(treesThree);
                                        }
                                    }
                                    else if (sysOrganizationTwo.getOrgantype().equals("3")) {
                                        List<MjSpxx> mjSpxxes = publicService.selectObjs("select * from mj_spxx where qybh='" + sysOrganizationTwo.getOrgan() + "'", MjSpxx.class);
                                        if (mjSpxxes.size() > 0) {
                                            List<TreeDz> treesFour = new ArrayList<TreeDz>();
                                            for (MjSpxx mjSpxx : mjSpxxes) {
                                                TreeDz treeFour = new TreeDz();
                                                treeFour.setId(mjSpxx.getId());
                                                treeFour.setText(mjSpxx.getSxtmc());
                                                treeFour.setTitle(mjSpxx.getSxtmc());
                                                treeFour.setSpread(false);
                                                treeFour.setIconCls("ipc");
                                                treeFour.setAttributes(null);
                                                treesFour.add(treeFour);
                                            }
                                            treeTwo.setChildren(treesFour);
                                        }
                                    }
                                    treesTwo.add(treeTwo);
                                }
                                treeOne.setChildren(treesTwo);
                            }
                        }
                        else{
                            List<MjSpxx> mjSpxxes=publicService.selectObjs("select * from mj_spxx where qybh='"+sysOrganizationOne.getOrgan()+"'",MjSpxx.class);
                            if(mjSpxxes.size()>0){
                                List<TreeDz> treesThree=new ArrayList<TreeDz>();
                                for(MjSpxx mjSpxx:mjSpxxes) {
                                    TreeDz treeThree = new TreeDz();
                                    treeThree.setId(mjSpxx.getId());
                                    treeThree.setText(mjSpxx.getSxtmc());
                                    treeThree.setTitle(mjSpxx.getSxtmc());
                                    treeThree.setSpread(false);
                                    treeThree.setIconCls("ipc");
                                    treeThree.setAttributes(null);
                                    treesThree.add(treeThree);
                                }
                                treeOne.setChildren(treesThree);
                            }
                        }
                        treesOne.add(treeOne);
                    }
                    treeCommon.setChildren(treesOne);
                }
            }
            else{
                List<MjSpxx> mjSpxxes=publicService.selectObjs("select * from mj_spxx where qybh='"+sysOrganization.getOrgan()+"'",MjSpxx.class);
                if(mjSpxxes.size()>0){
                    List<TreeDz> treesThree=new ArrayList<TreeDz>();
                    for(MjSpxx mjSpxx:mjSpxxes) {
                        TreeDz treeThree = new TreeDz();
                        treeThree.setId(mjSpxx.getId());
                        treeThree.setText(mjSpxx.getSxtmc());
                        treeThree.setTitle(mjSpxx.getSxtmc());
                        treeThree.setSpread(false);
                        treeThree.setIconCls("ipc");
                        treeThree.setAttributes(null);
                        treesThree.add(treeThree);
                    }
                    treeCommon.setChildren(treesThree);
                }
            }
        }
        treeOneList.add(treeCommon);
        // 根据roleID给定一级树
        return treeOneList;
    }
}
