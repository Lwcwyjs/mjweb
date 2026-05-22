package com.controller.login;

import com.commons.base.BaseController;
import com.commons.publicTool.PublicMethordUtil;
import com.commons.result.Result;
import com.commons.shiro.ShiroUser;
import com.commons.utils.DESHelper;
import com.model.login.CurrentUserInfo;
import com.model.securitymange.SysTerminalInfo;
import com.model.sysmanage.SysOrganization;
import com.model.sysmanage.SysUser;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.service.configmanage.SysResourceService;
import com.service.login.LoginService;
import com.service.sysmanage.SysUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * @description：登录退出
 * @author：zhixuan.wang @date：2015/10/1 14:51
 */
@Controller
public class LoginController extends BaseController {
	@Autowired
	private SysOptionService optionService;
	@Autowired
	@Resource
	private PublicService publicService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private CacheManager shiroEhcacheManager;
	@Autowired
	private HttpSession session;

	@Autowired
	private LoginService loginservice;

	@Autowired
	private SysResourceService resourceService;
	private String ai_sys_terminalinfo;

	static final String DATE_FORMAT = "yyyy-MM-dd";
	private DateFormat formatDate;


	/**
	 * 首页
	 *
	 * @return
	 */
	@RequestMapping(value = "/admin")
	public String index() {
//		return "redirect:/login/index";
		return "redirect:/template/template";
	}

	@RequestMapping(value = "/admin/console")
	public String indexConsole() {
		return "/login/console";
	}

	/**
	 * 首页 ß
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/admin/index")
	public String index(Model model, HttpServletRequest request, HttpSession session) {
		SysOrganization organization = getCurrentUserOrganization();
		SysUser sysUser = getCurrentUser();
		CurrentUserInfo userinfo = new CurrentUserInfo();
		userinfo.setOrganization(organization);
		userinfo.setUser(sysUser);
		request.setAttribute("userinfo", userinfo);
		request.setAttribute("username", userinfo.getUser().getName());
		InetAddress ia = null;
		try {
			ia = ia.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// publicService.insert("H_LOAD_LOG", "", "", loadLog);
		String title = optionService.getConfigValue("服务配置", "系统标题");
		title = StringUtils.isBlank(title) ? "机动车检验智能审核系统" : title;
		request.setAttribute("title", title);
		String pwd="";
		try {
			pwd = session.getAttribute("password").toString();
		}
		catch (Exception e) {
			pwd="BFbl@123";
		}

		SysUser currentUser = getCurrentUser();
		model.addAttribute("menus", resourceService.findTree(currentUser));
//		return checkPwdStrong(pwd) ? "login/index" : "sysmanage/updatePwd";
		return checkPwdStrong(pwd) ? "template/template" : "sysmanage/updatePwd";
	}


	/**
	 * 首页 交警支队名
	 *
	 * @return
	 */
	@RequestMapping(value = "/admin/init")
	public String init(HttpServletRequest request) {
		String glbmmc = optionService.getConfigValue("系统参数", "glbmmc");
		glbmmc = StringUtils.isBlank(glbmmc) ? "XX交警" : glbmmc;
		request.setAttribute("glbmmc", glbmmc);
		return glbmmc;
	}

	/**
	 * GET 登录
	 * 
	 * @return {String}
	 */
	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		logger.info("GET请求登录");
		String title = optionService.getConfigValue("服务配置", "系统标题");
		String glbm = optionService.getConfigValue("服务配置", "管理部门");
		String lxdh = optionService.getConfigValue("服务配置", "联系电话");
		title = StringUtils.isBlank(title) ? "机动车检验智能审核系统" : title;
		glbm = StringUtils.isBlank(glbm) ? "机动车排气污染管理中心" : glbm;
		lxdh = StringUtils.isBlank(lxdh) ? "400-7777-266" : lxdh;
		request.setAttribute("title", title);
		request.setAttribute("glbm", glbm);
		request.setAttribute("lxdh", lxdh);
		if (SecurityUtils.getSubject().isAuthenticated()) {
//			return "redirect:/login/index";
			return "redirect:/template/template";
		}
		return "login/login";
	}

	/**
	 * GET 未授权
	 * 
	 * @return {String}
	 */
	@RequestMapping(value = "/admin/unauth", method = RequestMethod.GET)
	public String unauth(HttpServletRequest request) {
		logger.info("GET请求登录");
		String title = optionService.getConfigValue("服务配置", "系统标题");
		String glbm = optionService.getConfigValue("服务配置", "管理部门");
		String lxdh = optionService.getConfigValue("服务配置", "联系电话");
		title = StringUtils.isBlank(title) ? "机动车检验智能审核系统" : title;
		glbm = StringUtils.isBlank(glbm) ? "机动车排气污染管理中心" : glbm;
		lxdh = StringUtils.isBlank(lxdh) ? "400-7777-266" : lxdh;
		request.setAttribute("title", title);
		request.setAttribute("glbm", glbm);
		request.setAttribute("lxdh", lxdh);

		return "login/login";
	}

	@RequestMapping(value = "/admin/updatePwd")
	public String updatePwd(Model model, HttpServletRequest request) {
		SysOrganization organization = getCurrentUserOrganization();
		SysUser sysUser = getCurrentUser();
		CurrentUserInfo userinfo = new CurrentUserInfo();
		userinfo.setOrganization(organization);
		userinfo.setUser(sysUser);
		request.setAttribute("userinfo", userinfo);
		String title = optionService.getConfigValue("服务配置", "系统标题");
		title = StringUtils.isBlank(title) ? "机动车检验智能审核系统" : title;
		request.setAttribute("title", title);
		return "sysmanage/updatePwd";
	}

	@RequestMapping(value = "/admin/register")
	public String register(Model model, HttpServletRequest request) {
		logger.info("GET申请注册");
		return "admin/register";
	}

	/**
	 * POST 登录 shiro 写法
	 *
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	@ResponseBody
	public Object loginPost(String username, String password, String verifycode, HttpSession session,
			HttpServletRequest request) {
		logger.info("POST请求登录");

		// 最大并发数限制
		Set currUser = shiroEhcacheManager.getCache("shiro-kickout-session").keys();
		String maxUserCount = "20";
		maxUserCount = maxUserCount == "" ? "5" : maxUserCount;
		if (currUser.size() >= Integer.valueOf(maxUserCount)) {
			logger.error("已达用户最大并发限制，不允许登录");
			return renderError("已达用户最大并发限制，不允许登录");
		}
		formatDate = new SimpleDateFormat(DATE_FORMAT);
		String sdatenow = formatDate.format(new Date());
//		if (sdatenow.compareTo("2023-12-01")>0){
//			logger.error("授权已过期");
//			return renderError("授权已过期");
//		}
		for (Object object : currUser) {
			System.out.println(object);
		}
		if (StringUtils.isBlank(username)) {
			logger.error("用户名为空,无法登陆!");
			return renderError("用户名不能为空");
		} else {
			logger.info("用户名" + username + "待验证");
		}
		if (StringUtils.isBlank(password)) {
			logger.error("密码为空无法登陆!");
			return renderError("密码不能为空");
		} else {
			logger.info("密码不为空,待验证");
		}
		username = DESHelper.base64Decrypt(username, "12345678");
		password = DESHelper.base64Decrypt(password, "12345678");
		verifycode = DESHelper.base64Decrypt(verifycode, "12345678");
		String code = session.getAttribute("rand").toString();
		if (code==null) {
			logger.error("验证码为空!");
			return renderError("请重新获取验证码");
		}
		if (!code.equals(verifycode)) {
			logger.error("验证码不匹配!");
			return renderError("验证码不正确");
		} else {
			logger.info("验证码正确");
		}
		// 校验密码强度
		session.setAttribute("password", password);

		SysUser sysUser = sysUserService.findUserByLoginName(username);

		if (sysUser == null) {
			return renderError("账号不存在");
		}

		SysTerminalInfo sysTerminalInfo = sysUserService.findTerminalByIp();

		String organ = sysUser.getOrgan();

		Object organization = publicService.selectObj("ai_sys_organization", "", "organ", organ);
		int eTimes = 0;
		int ipTimes = 0;
		Date dateNow = new Date();
		String ip = sysTerminalInfo.getTerminal_id();

		if (sysUser != null && sysUser.getErrortimes() != null) {
			eTimes = sysUser.getErrortimes();
		}
		if (sysTerminalInfo.getNum_id() != null && sysTerminalInfo.getFail_times() != null) {
			ipTimes = sysTerminalInfo.getFail_times();
		} else if (sysTerminalInfo.getNum_id() == null) {
			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			sysTerminalInfo.setNum_id(uuid);
			sysTerminalInfo.setCreate_time(dateNow);
			sysTerminalInfo.setIslocked("0");
			sysTerminalInfo.setFail_times(ipTimes);
			sysTerminalInfo.setUpdate_time(dateNow);
			sysTerminalInfo.setJyw(PublicMethordUtil.md5("" + sysTerminalInfo.getTerminal_id() + sysTerminalInfo.getIslocked()));
			publicService.insert("ai_sys_terminalinfo", "", "terminal_id", sysTerminalInfo);
		}

		Subject user = SecurityUtils.getSubject();
		String s1 = DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid() + username + password));
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid() + username + password)).toCharArray());
		token.setRememberMe(true);

		session.setAttribute("orgn", organization);

		Result result = new Result();
		// IP黑名单校验
		result = loginservice.checkBlackIP(ip);
		if (!result.isSuccess()) {
			publicService.insertLogInfo("终端锁定", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			String unlocktime = result.getObj().toString();
			return renderError(result.getMsg() + ":" + unlocktime);
		}
		// 用户状态校验
		result = loginservice.chekUserState(sysUser);
		if (!result.isSuccess()) {
			publicService.insertLogInfo("用户状态不正常", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError(result.getMsg());
		}

		// 用户有效期校验
		result = loginservice.checkValid(sysUser);
		if (!result.isSuccess()) {
			publicService.insertLogInfo("过期登录", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError(result.getMsg());
		}

		try {
			user.login(token);
			if (sysUser == null) {// 等保测评优化2018/3/8
				return renderError("该用户不存在");
			}
			eTimes = 0;
			ipTimes = 0;
			LockMsg(eTimes, sysUser);
			IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("用户名为" + username + "的用户验证通过,登陆成功!", "登陆", "1", "登录页面");
			logger.info("用户名为" + username + "的用户验证通过,登陆成功!");
		} catch (UnknownAccountException e) {
			logger.error("账号不存在：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("账号不存在", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError("账号或密码不正确。" + ms);
		} catch (DisabledAccountException e) {
			logger.error("账号未启用：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("账号未启用", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError("账号或密码不正确。" + ms);
		} catch (IncorrectCredentialsException e) {
			logger.error("密码错误：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("密码错误", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError("账号或密码不正确。" + ms);
		} catch (RuntimeException e) {
			logger.error("未知错误,请联系管理员：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("未知错误,请联系管理员", "登陆", "0", sysUser.getId(), sysUser.getName(),
					sysUser.getOrgan(), "登录页面");
			return renderError("账号或密码不正确。" + ms);
		}
		organization = getCurrentUserOrganization();

		sysUser.setScdlip(sysUser.getZjdlip());
		sysUser.setScdlsj(sysUser.getZjdlsj());
		sysUser.setZjdlip(request.getRemoteAddr());
		sysUser.setZjdlsj(new Date());
		sysUserService.updateUserex(sysUser);
		return renderSuccess();
	}
	@RequestMapping(value = "/admin/loginex", method = RequestMethod.POST)
	@ResponseBody
	public Object loginex(String username, String password, HttpSession session,
						  HttpServletRequest request) {
		logger.info("POST请求登录");
		// 最大并发数限制
		Set currUser = shiroEhcacheManager.getCache("shiro-kickout-session").keys();
		String maxUserCount = "20";
		maxUserCount = maxUserCount == "" ? "5" : maxUserCount;
		if (currUser.size() >= Integer.valueOf(maxUserCount)) {
			logger.error("已达用户最大并发限制，不允许登录");
			return renderError("已达用户最大并发限制，不允许登录");
		}
		formatDate = new SimpleDateFormat(DATE_FORMAT);
		for (Object object : currUser) {
			System.out.println(object);
		}
		if (StringUtils.isBlank(username)) {
			logger.error("用户名为空,无法登陆!");
			return renderError("用户名不能为空");
		} else {
			logger.info("用户名" + username + "待验证");
		}
		if (StringUtils.isBlank(password)) {
			logger.error("密码为空无法登陆!");
			return renderError("密码不能为空");
		} else {
			logger.info("密码不为空,待验证");
		}
		// 校验密码强度
		session.setAttribute("password", password);

		SysUser sysUser = sysUserService.findUserByLoginName(username);

		if (sysUser == null) {
			return renderError("账号不存在");
		}

		SysTerminalInfo sysTerminalInfo = sysUserService.findTerminalByIp();

		String organ = sysUser.getOrgan();

		Object organization = publicService.selectObj("ai_sys_organization", "", "organ", organ);
		int eTimes = 0;
		int ipTimes = 0;
		Date dateNow = new Date();
		String ip = sysTerminalInfo.getTerminal_id();

		if (sysUser != null && sysUser.getErrortimes() != null) {
			eTimes = sysUser.getErrortimes();
		}
		if (sysTerminalInfo.getNum_id() != null && sysTerminalInfo.getFail_times() != null) {
			ipTimes = sysTerminalInfo.getFail_times();
		} else if (sysTerminalInfo.getNum_id() == null) {
			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			sysTerminalInfo.setNum_id(uuid);
			sysTerminalInfo.setCreate_time(dateNow);
			sysTerminalInfo.setIslocked("0");
			sysTerminalInfo.setFail_times(ipTimes);
			sysTerminalInfo.setUpdate_time(dateNow);
			sysTerminalInfo.setJyw(PublicMethordUtil.md5("" + sysTerminalInfo.getTerminal_id() + sysTerminalInfo.getIslocked()));
			publicService.insert("ai_sys_terminalinfo", "", "terminal_id", sysTerminalInfo);
		}

		Subject user = SecurityUtils.getSubject();
		String s1 = DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid() + username + password));
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				DigestUtils.md5Hex(DigestUtils.md5Hex(sysUser.getUserid() + username + password)).toCharArray());
		token.setRememberMe(true);

		session.setAttribute("orgn", organization);

		Result result = new Result();
		// IP黑名单校验
		result = loginservice.checkBlackIP(ip);
		if (!result.isSuccess()) {
			publicService.insertLogInfo("终端锁定", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			String unlocktime = result.getObj().toString();
			return renderError(result.getMsg() + ":" + unlocktime);
		}
		// 用户状态校验
		result = loginservice.chekUserState(sysUser);
		if (!result.isSuccess()) {
			publicService.insertLogInfo("用户状态不正常", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError(result.getMsg());
		}

		// 用户有效期校验
		result = loginservice.checkValid(sysUser);
		if (!result.isSuccess()) {
			publicService.insertLogInfo("过期登录", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError(result.getMsg());
		}

		try {
			user.login(token);
			if (sysUser == null) {// 等保测评优化2018/3/8
				return renderError("该用户不存在");
			}
			eTimes = 0;
			ipTimes = 0;
			LockMsg(eTimes, sysUser);
			IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("用户名为" + username + "的用户验证通过,登陆成功!", "登陆", "1", "登录页面");
			logger.info("用户名为" + username + "的用户验证通过,登陆成功!");
		} catch (UnknownAccountException e) {
			logger.error("账号不存在：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("账号不存在", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError("账号或密码不正确。" + ms);
		} catch (DisabledAccountException e) {
			logger.error("账号未启用：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("账号未启用", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError("账号或密码不正确。" + ms);
		} catch (IncorrectCredentialsException e) {
			logger.error("密码错误：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("密码错误", "登陆", "0", sysUser.getId(), sysUser.getName(), sysUser.getOrgan(),
					"登录页面");
			return renderError("账号或密码不正确。" + ms);
		} catch (RuntimeException e) {
			logger.error("未知错误,请联系管理员：{}", e);
			eTimes = eTimes + 1;
			ipTimes = ipTimes + 1;
			String ms = LockMsg(eTimes, sysUser);
			ms = ms + IPLockMsg(ipTimes, sysTerminalInfo);
			publicService.insertLogInfo("未知错误,请联系管理员", "登陆", "0", sysUser.getId(), sysUser.getName(),
					sysUser.getOrgan(), "登录页面");
			return renderError("账号或密码不正确。" + ms);
		}
		organization = getCurrentUserOrganization();

		sysUser.setScdlip(sysUser.getZjdlip());
		sysUser.setScdlsj(sysUser.getZjdlsj());
		sysUser.setZjdlip(request.getRemoteAddr());
		sysUser.setZjdlsj(new Date());
		sysUserService.updateUserex(sysUser);
		Subject subject = SecurityUtils.getSubject();
		System.out.println("登录成功-Session ID：" + subject.getSession().getId());
		System.out.println("登录成功-用户信息：" + subject.getPrincipal());
		return renderSuccess();
	}

	private String LockMsg(Integer errorTimes, SysUser sysUser) {
		String ret = "";
		String times = "5";
		times = times == "" ? "5" : times;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String sdsj = formatter.format(new Date());// 获得锁定时间,以便写入日志里
		if (errorTimes >= Integer.valueOf(times)) {
			if (sysUser != null) {
				// 0:正常 1:停用
				sysUser.setStatus(1);
			}
		}
		if (sysUser != null) {
			sysUser.setErrortimes(errorTimes);
			String jyw = PublicMethordUtil
					.md5(sysUser.getLoginname() + sysUser.getName()  + sysUser.getStatus());
			sysUser.setJyw(jyw);
			publicService.update("ai_sys_user", "id,createdateend,role_id,loginname", "id", "loginname,name,organ", "",
					sysUser);
		}

		if (errorTimes > 0 && errorTimes < Integer.valueOf(times)) {
			ret = String.format("登录失败%s次，再失败%s次此账号将会被锁定。   ", errorTimes.toString(),
					String.valueOf(Integer.valueOf(times) - errorTimes));
		} else if (errorTimes >= Integer.valueOf(times)) {
			ret = String.format("登录失败%s次，帐号被锁定。   ", errorTimes.toString());
		}
		return ret;
	}

	private String IPLockMsg(int ipTimes, SysTerminalInfo sysTerminalInfo) {
		String ret = "";
		String IPTimes = "10";
		String time = "30";
		IPTimes = IPTimes == "" ? "7" : IPTimes;
		time = time == "" ? "30" : time;

		Date date = new Date();

		if (ipTimes == Integer.valueOf(IPTimes)) {
			if (sysTerminalInfo != null) {
				// 0:未锁定 1:已锁定
				sysTerminalInfo.setIslocked("1");
				long currentTime = System.currentTimeMillis();
				currentTime += 30 * 60 * 1000;
				Date lockdate = new Date(currentTime);
				sysTerminalInfo.setLockedtime(lockdate);
			}
		}
		if (sysTerminalInfo != null) {
			sysTerminalInfo.setFail_times(ipTimes);
			sysTerminalInfo.setUpdate_time(date);
			String jyw = PublicMethordUtil.md5("" + sysTerminalInfo.getTerminal_id() + sysTerminalInfo.getIslocked());
			sysTerminalInfo.setJyw(jyw);
			publicService.update("AI_SYS_TERMINALINFO", "create_time,num_id,terminal_id,", "terminal_id", "",
					sysTerminalInfo);
		}

		if (ipTimes > 0 && ipTimes < Integer.valueOf(IPTimes)) {
			ret = String.format("  IP登陆失败%s次,再失败%s次IP将会被锁定%s分钟", ipTimes,
					String.valueOf(Integer.valueOf(IPTimes) - ipTimes), time);
		} else if (ipTimes >= Integer.valueOf(IPTimes)) {
			ret = String.format("  IP登录失败%s次，IP被锁定，请%s分钟后再试", ipTimes,
					(sysTerminalInfo.getLockedtime().getTime() - new Date().getTime()) / 1000 / 60);
		}
		return ret;
	}

	/**
	 * 未授权
	 * 
	 * @return {String}
	 */
	@RequestMapping(value = "/unauth")
	public String unauth() {
		if (SecurityUtils.getSubject().isAuthenticated() == false) {
			return "redirect:login/login";
		}
		return "unauth";
	}

	/**
	 * 退出
	 * 
	 * @return {Result}
	 */
	@RequestMapping(value = "/admin/logout")
	@ResponseBody
	public Object logout() {
		logger.info("登出");
		Subject subject = SecurityUtils.getSubject();
		ShiroUser user = (ShiroUser) subject.getPrincipal();
		SysUser currentUser = sysUserService.findUserById(user.id);
		publicService.insertLogInfo("用户" + currentUser.getName() + "退出登录", "登陆", "1", "登录页面");
		shiroEhcacheManager.getCache("shiro-kickout-session").remove(user.name);
		subject.logout();
		return renderSuccess();
	}

	/**
	 * GET 登录
	 * 
	 * @return {String}
	 */
	@RequestMapping(value = "/admin/kickout", method = RequestMethod.GET)
	public String kickout(HttpServletRequest request) {
		String kickout = "您的账号在其它地方登录,本次已退出";
		logger.info("GET请求登录");
		String title = optionService.getConfigValue("服务配置", "系统标题");
		String glbm = optionService.getConfigValue("服务配置", "管理部门");
		String lxdh = optionService.getConfigValue("服务配置", "联系电话");
		title = StringUtils.isBlank(title) ? "机动车检验智能审核系统" : title;
		glbm = StringUtils.isBlank(glbm) ? "管理中心" : glbm;
		lxdh = StringUtils.isBlank(lxdh) ? "400-7777-266" : lxdh;
		request.setAttribute("title", title);
		request.setAttribute("glbm", glbm);
		request.setAttribute("lxdh", lxdh);
		request.setAttribute("kickout", kickout);
		return "login/login";
	}

	/**
	 * GET 登录
	 * 
	 * @return {String}
	 */
	@RequestMapping(value = "/timeout", method = RequestMethod.GET)
	public String timeout(HttpServletRequest request) {
		String kickout = "您的账号在其它地方登录,本次已退出";
		logger.info("GET请求登录");
		String title = optionService.getConfigValue("服务配置", "系统标题");
		String glbm = optionService.getConfigValue("服务配置", "管理部门");
		String lxdh = optionService.getConfigValue("服务配置", "联系电话");
		title = StringUtils.isBlank(title) ? "机动车检验智能审核系统" : title;
		glbm = StringUtils.isBlank(glbm) ? "管理中心" : glbm;
		lxdh = StringUtils.isBlank(lxdh) ? "400-7777-266" : lxdh;
		request.setAttribute("title", title);
		request.setAttribute("glbm", glbm);
		request.setAttribute("lxdh", lxdh);
		request.setAttribute("kickout", kickout);
		return "login/login";
	}

	private boolean checkPwdStrong(String pwd) {
		if (StringUtils.isBlank(pwd)) {
			return false;
		}
		String strong = "^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$";
		return pwd.matches(strong);
	}
}
