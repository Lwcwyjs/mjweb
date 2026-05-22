package com.service.login;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.commons.publicTool.PublicMethordUtil;
import com.service.configmanage.SysOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commons.result.Result;
import com.model.sysmanage.SysUser;
import com.model.securitymange.SysTerminalInfo;
import com.service.base.PublicService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	@Resource
	private PublicService publicService;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SysOptionService optionService;

	@Override
	public Result chekUserState(SysUser sysuser) {
		// TODO Auto-generated method stub
		Result result = new Result();
		result.setSuccess(true);
		result.setMsg("用户状态正常");
		try {
			// 账户状态
			if (sysuser.getStatus() == 2 && null != sysuser.getStatus()) {
				result.setSuccess(false);
				result.setMsg("帐号锁定中，请联系管理员解锁后再试");
				return result;
			}
			if (null != sysuser.getStatus() && sysuser.getStatus().equals(3)) {
				result.setSuccess(false);
				result.setMsg("帐号处于停用状态，请联系管理员！");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("状态验证有问题，联系管理员msg:" + e.getMessage());
			return result;
		}

	}


	@Override
	public Result checkValid(SysUser sysuser) {
		// TODO Auto-generated method stub
		Result result = new Result();
		result.setSuccess(true);
		try {

			// 账号有效期校验
			if (null != sysuser.getZhyxq()) {
				Date zhyxq = sysuser.getZhyxq();
				Date now = new Date();
				if (now.before(zhyxq)) {
					result.setMsg("在有效期内");
				} else {
					result.setSuccess(false);
					result.setMsg("用户登录时，超出账号有效期");
					return result;
				}

			} else {
				result.setSuccess(false);
				result.setMsg("账号有效期设置有问题，联系管理员");
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("工作时间验证有问题，联系管理员msg:" + e.getMessage());
			return result;
		}

	}


	public String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}
	@Override
	public Result checkBlackIP(String ip) {
		// TODO Auto-generated method stub
		Result result = new Result();
		result.setSuccess(true);
		SysTerminalInfo t = new SysTerminalInfo();
		Date dateNow = new Date();
		try {
			t.setTerminal_id(ip);
			t = (SysTerminalInfo) publicService.selectObject("AI_SYS_TERMINALINFO", "", "terminal_id", "", "", t);
			if (null != t) {
				if (t.getIslocked().equals("1")) {
					Date sockDate = t.getLockedtime();
					// sockDate = sdf.parse(sdf.format(sockDate));
					if (dateNow.after(sockDate)) {
						t.setIslocked("0");
						t.setUpdate_time(dateNow);
						t.setFail_times(0);
						t.setJyw(PublicMethordUtil.md5("" + t.getTerminal_id() + t.getIslocked()));
						publicService.update("AI_SYS_TERMINALINFO", "", "terminal_id", t);
						publicService.update("update AI_SYS_TERMINALINFO set lockedtime = null where terminal_id = '"
								+ t.getTerminal_id() + "'");
						result.setMsg("此IP锁定时间已过，已自动踢出黑名单。");
					} else {
						String mString = String.format("此IP被记入黑名单,请联系管理员解锁或等待%s分钟后自动解锁！",
								(sockDate.getTime() - dateNow.getTime()) / 1000 / 60);
						result.setSuccess(false);
						long unlocktime = (sockDate.getTime() - dateNow.getTime()) / 1000;
						System.out.println(unlocktime + "------------s");
						// SimpleDateFormat formatter = new
						// SimpleDateFormat("HH:mm:ss");
						// formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
						// String hms = formatter.format(unlocktime);
						result.setObj(unlocktime);
						result.setMsg(mString);
					}
				} else {
					t.setIslocked("0");
					t.setUpdate_time(dateNow);
					t.setFail_times(0);
					t.setJyw(PublicMethordUtil.md5("" + t.getTerminal_id() + t.getIslocked()));
					publicService.update("AI_SYS_TERMINALINFO", "", "terminal_id", "", "", t);
					result.setMsg("IP限制正常");
				}
			} else {
				result.setMsg("IP限制正常");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("IP限制检测异常:" + e.getMessage());
			return result;
		}

	}
}
