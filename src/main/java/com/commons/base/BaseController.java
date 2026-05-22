package com.commons.base;

import com.commons.result.Result;
import com.commons.shiro.ShiroUser;
import com.commons.utils.DealString;
import com.commons.utils.PageInfo;
import com.commons.utils.StringEscapeEditor;
import com.model.sysmanage.SysOrganization;
import com.model.sysmanage.SysRole;
import com.model.sysmanage.SysUser;
import com.model.sysmanage.SysUserRole;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import com.service.sysmanage.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description：基础 controller
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysOrganizationService sysOrganizationService;

	@Autowired
	private PublicService publicService;
    
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

        /**
         * 防止XSS攻击
         */
        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

    /**
     * 获取当前登录用户对象
     * @return
     */
    public SysUser getCurrentUser() {
    	System.out.println(SecurityUtils.getSubject().getPrincipal()+"----------SecurityUtils.getSubject().getPrincipal()");
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        SysUser currentUser=new SysUser();
        if (user==null){
            currentUser.setLoginname("13784068839");
            currentUser=publicService.selectObj("ai_sys_user","","loginname",currentUser);
        }
        else {
            currentUser = sysUserService.findUserById(user.id);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户id
     * @return
     */
    public Long getUserId() {
        return this.getCurrentUser().getId();
    }

    /**
     * 获取当前登录用户名
     * @return
     */
    public String getStaffName() {
        return this.getCurrentUser().getName();
    }

    /**
     * 获取当前用户的部门信息
     * @return
     */
    public SysOrganization getCurrentUserOrganization(){
    	SysUser sysUser= getCurrentUser();
        SysOrganization organ=new SysOrganization();
        if(sysUser!=null) {
            organ.setOrgan(sysUser.getOrgan());
            organ=publicService.selectObj("ai_sys_organization", "", "cyqxh", organ);
        }
        return organ;
    }
    
    /**
     * 获取当前用户的部门ID
     * @return
     */
    public long getCurrentUserOrganId(){
    	SysUser sysUser= getCurrentUser();
    	if(sysUser==null) {return 0;}
    	SysOrganization organ=new SysOrganization();
    	organ.setOrgan(sysUser.getOrgan());
    	organ=publicService.selectObj("ai_sys_organization", "", "code", organ);
    	if (organ==null) {return 0;}
    	return 1;//organ.getId();
    }
    
    /**
     * 获取当前用户的部门编码
     * @return
     */
    public String getCurrentUserOrganCode(){
    	SysUser sysUser= getCurrentUser();
    	if(sysUser==null) {return null;}
    	return sysUser.getOrgan();
    }
    /**
     * 获取当前用户的部门名称
     * @return
     */
    public String getCurrentUserOrganName(){
    	SysUser sysUser= getCurrentUser();
    	if(sysUser==null) {return null;}
    	SysOrganization organ=new SysOrganization();
    	organ.setOrgan(sysUser.getOrgan());
    	organ=publicService.selectObj("ai_sys_organization", "", "code", organ);
    	if (organ==null) {return null;}
    	return organ.getName();
    }
    
    public Long getCurrentUserRoleId(){
    	SysUser sysUser= getCurrentUser();
    	if(sysUser==null) {return null;}
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUser_id(sysUser.getId());
		sysUserRole = publicService.selectObj("ai_sys_user_role", "", "user_id", sysUserRole);
		
    	if (sysUserRole ==null) {return null;}
    	return sysUserRole.getRole_id();
    }
    
    public String getCurrentUserRoleName(){
    	SysRole sysRole= new SysRole();
    	Long roleId=getCurrentUserRoleId();
    	if(roleId==null) {return null;}
    	sysRole.setId(roleId);
    	sysRole=publicService.selectObj("ai_sys_role","","id",sysRole);
    	if (sysRole==null) {return null;}
    	return sysRole.getName();
    }
    /**
     * ajax失败
     * @param msg 失败的消息
     * @return {Object}
     */
    public Object renderError(String msg) {
        Result result = new Result();
        result.setMsg(msg);
        return result;
    }
    /**
     * PageInfo分页获取异常
     * @param msg
     * @return
     */
    public PageInfo renderErrorForDataGrid(String msg) {
    	PageInfo pageInfo = new PageInfo();
    	pageInfo.setTotal(-1);
        List<String> msgs = new ArrayList<String>();
        msgs.add(msg);
        pageInfo.setRows(msgs);
        return pageInfo;
    }
    /**
     * 返回string类型的异常
     * @param msg
     * @return
     */
    public String renderErrorForString(String msg) {
        return msg;
    }
    
    /**
     * ajax失败
     * @param msg ajax失败时需要返回的消息
     * @param obj ajax失败是需要返回的对象
     * @return
     */
    public Object renderError(String msg,Object obj) {
        Result result = new Result();
        result.setMsg(msg);
        result.setObj(obj);
        return result;
    }
    

    /**
     * ajax成功
     * @return {Object}
     */
    public Object renderSuccess() {
        Result result = new Result();
        result.setSuccess(true);
        result.setMsg("执行成功");
        return result;
    }

    /**
     * ajax成功
     * @param msg 消息
     * @return {Object}
     */
    public Object renderSuccess(String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMsg(msg);
        return result;
    }

    /**
     * ajax成功
     * @param obj 成功时的对象
     * @return {Object}
     */
    public Object renderSuccess(Object obj) {
        Result result = new Result();
        result.setSuccess(true);
        result.setObj(obj);
        return result;
    }
    
    public PageInfo renderSuccess(PageInfo obj) {
        //Result result = new Result();
        //result.setSuccess(true);
        //result.setObj(obj);
        return obj;
    }
    
    /**
     * ajax成功，
     * @param msg 成功时要返回的消息
     * @param obj 成功时要返回的对象obj
     * @return
     */
    public Object renderSuccess(String msg,Object obj)
    {
        Result result = new Result();
        result.setSuccess(true);
        result.setMsg(msg);
        result.setObj(obj);
        return result;    	
    }
    
    public Date getCurrentDatetime() throws ParseException
    {
    	SimpleDateFormat sdf = new SimpleDateFormat(" ");
    	Date date = sdf.parse(DealString.GetDateTime());
    	return date;
    }
    /** 
     * SpringMvc下获取request 
     *  
     * @return 
     */  
    public  HttpServletRequest getRequest() {  
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();  
        return request;  
  
    }  
}
