package com.controller.login;

import com.commons.base.BaseController;
import com.model.login.BarVo;
import com.model.login.PieVo;
import com.service.base.PublicService;
import com.service.sysmanage.SysOrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description：ai任务数据展示操作控制器
 * @author：Songdh @date：2018/10/23 09:19
 */
@Controller
@RequestMapping("/admin/count")
public class CountController extends BaseController {
	@Autowired
	@Resource
	private PublicService publicService;
	@Autowired
	private SysOrganizationService organService;
	static final String DATE_FORMAT = "yyyy-MM-dd";
	private DateFormat formatDate;

	/**
	 * ai任务识别数据查询转换页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/taskDataCountMain", method = RequestMethod.GET)
	public String turnMain() {

		return "ai/taskDataCountMain";
	}


	@RequestMapping(value = "/txjlweek", method = RequestMethod.POST)
	@ResponseBody
	public Object txjlweekBar(@RequestParam Map<String, Object> params) {
		Map<String, Object> reData = new HashMap<String, Object>();
		List<BarVo> barVos = new ArrayList<BarVo>();

		String strWhere = "1=1";
		if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
			strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
		} else {
			String strCurrentOrgan = getCurrentUserOrganCode();
			if(strCurrentOrgan.equals("1306020010")){
				strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
			}
			else {
				String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
				if (StringUtils.isNoneBlank(strOrgans)) {
					strOrgans = strCurrentOrgan + "," + strOrgans;
					strOrgans = strOrgans.replace(",", "','");
					strWhere += String.format(" and qybh in ('%s')", strOrgans);
				} else {
					strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
				}
			}

		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		String stime = df.format(calendar.getTime());
		strWhere=strWhere+" and tgkssj>='"+stime+" 00:00:00'";
		String sql = "select distinct FORMAT(tgkssj, 'yyyy-MM-dd') as item1,jclx as item2,count(tgkssj) as inum from mj_data_base where "+strWhere+" group by FORMAT(tgkssj, 'yyyy-MM-dd'),jclx order by FORMAT(tgkssj, 'yyyy-MM-dd'),jclx";
		try {
			barVos = publicService.selectObjs(sql, BarVo.class);
			List<Integer> jarr = new ArrayList<Integer>();
			List<Integer> carr = new ArrayList<Integer>();
			List<String> rqarr = new ArrayList<String>();
			Calendar calendar1= Calendar.getInstance();
			calendar1.add(Calendar.DATE, -7);
			String s1 = df.format(calendar1.getTime());
			Calendar calendar2= Calendar.getInstance();
			calendar2.add(Calendar.DATE, -6);
			String s2 = df.format(calendar2.getTime());
			Calendar calendar3= Calendar.getInstance();
			calendar3.add(Calendar.DATE, -5);
			String s3 = df.format(calendar3.getTime());
			Calendar calendar4= Calendar.getInstance();
			calendar4.add(Calendar.DATE, -4);
			String s4= df.format(calendar4.getTime());
			Calendar calendar5= Calendar.getInstance();
			calendar5.add(Calendar.DATE, -3);
			String s5 = df.format(calendar5.getTime());
			Calendar calendar6= Calendar.getInstance();
			calendar6.add(Calendar.DATE, -2);
			String s6 = df.format(calendar6.getTime());
			Calendar calendar7= Calendar.getInstance();
			calendar7.add(Calendar.DATE, -1);
			String s7 = df.format(calendar7.getTime());
			for(int i=1;i<=7;i++) {
				if(i==1) {
					rqarr.add(s1);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s1)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
				else if(i==2) {
					rqarr.add(s2);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s2)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
				else if(i==3) {
					rqarr.add(s3);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s3)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
				else if(i==4) {
					rqarr.add(s4);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s4)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
				else if(i==5) {
					rqarr.add(s5);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s5)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
				else if(i==6) {
					rqarr.add(s6);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s6)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
				else if(i==7) {
					rqarr.add(s7);
					boolean b1=false;
					boolean b2=false;
					for (BarVo o : barVos) {
						if (o.getItem1().equals(s7)){
							if(o.getItem2().equals("1")){
								b1=true;
								jarr.add(o.getInum());
							}
							else{
								b2=true;
								carr.add(o.getInum());
							}
						}
					}
					if(!b1){
						jarr.add(0);
					}
					if(!b2){
						carr.add(0);
					}
				}
			}
			reData.put("jarr", jarr);
			reData.put("carr", carr);
			reData.put("rqarr", rqarr);
			reData.put("code", "1");
			reData.put("message", "查询成功");

		} catch (Exception e) {
			logger.error(e.getMessage());
			reData.put("code", "0");
			reData.put("message", "失败");
		}
		return reData;
	}

	@RequestMapping(value = "/rlzlzb", method = RequestMethod.POST)
	@ResponseBody
	public Object rlzlzbCount(@RequestParam Map<String, Object> params) {
		Map<String, Object> reData = new HashMap<String, Object>();
		List<PieVo> rlzlVos = new ArrayList<PieVo>();

		String strWhere = "1=1";
		if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
			strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
		} else {
			String strCurrentOrgan = getCurrentUserOrganCode();
			if(strCurrentOrgan.equals("1306020010")){
				strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
			}
			else {
				String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
				if (StringUtils.isNoneBlank(strOrgans)) {
					strOrgans = strCurrentOrgan + "," + strOrgans;
					strOrgans = strOrgans.replace(",", "','");
					strWhere += String.format(" and qybh in ('%s')", strOrgans);
				} else {
					strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
				}
			}

		}
		String sql = "select distinct rlzl as item,count(*) as inum from mj_vehicle where "+strWhere+" group by rlzl";
		try {
			rlzlVos = publicService.selectObjs(sql, PieVo.class);
			List<Integer> qyarr = new ArrayList<Integer>();
			List<Integer> cyarr = new ArrayList<Integer>();
			List<Integer> trqarr = new ArrayList<Integer>();
			List<Integer> darr = new ArrayList<Integer>();
			List<Integer> qtarr = new ArrayList<Integer>();
			for (PieVo o : rlzlVos) {
				if(o.getItem().equals("A")){
					qyarr.add(o.getInum());
				}
				else if(o.getItem().equals("B")){
					cyarr.add(o.getInum());
				}
				else if(o.getItem().equals("E")){
					trqarr.add(o.getInum());
				}
				else if(o.getItem().equals("C")){
					darr.add(o.getInum());
				}
				else{
					qtarr.add(o.getInum());
				}
			}
			if(qyarr.size()==0){
				qyarr.add(0);
			}
			if(cyarr.size()==0){
				cyarr.add(0);
			}
			if(trqarr.size()==0){
				trqarr.add(0);
			}
			if(darr.size()==0){
				darr.add(0);
			}
			if(qtarr.size()==0){
				qtarr.add(0);
			}
			reData.put("qyarr", qyarr);
			reData.put("cyarr", cyarr);
			reData.put("trqarr", trqarr);
			reData.put("darr", darr);
			reData.put("qtarr", qtarr);
			reData.put("code", "1");
			reData.put("message", "查询成功");

		} catch (Exception e) {
			logger.error(e.getMessage());
			reData.put("code", "0");
			reData.put("message", "失败");
		}
		return reData;
	}
	@RequestMapping(value = "/pfbzzb", method = RequestMethod.POST)
	@ResponseBody
	public Object pfbzzbCount(@RequestParam Map<String, Object> params) {
		Map<String, Object> reData = new HashMap<String, Object>();
		List<PieVo> pfbzVos = new ArrayList<PieVo>();

		String strWhere = "1=1";
		if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
			strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
		} else {
			String strCurrentOrgan = getCurrentUserOrganCode();
			if(strCurrentOrgan.equals("1306020010")){
				strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
			}
			else {
				String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
				if (StringUtils.isNoneBlank(strOrgans)) {
					strOrgans = strCurrentOrgan + "," + strOrgans;
					strOrgans = strOrgans.replace(",", "','");
					strWhere += String.format(" and qybh in ('%s')", strOrgans);
				} else {
					strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
				}
			}

		}
		String sql = "select distinct pfbz as item,count(*) as inum from mj_vehicle  where "+strWhere+" group by pfbz";
		try {
			pfbzVos = publicService.selectObjs(sql, PieVo.class);
			List<Integer> g0arr = new ArrayList<Integer>();
			List<Integer> g1arr = new ArrayList<Integer>();
			List<Integer> g2arr = new ArrayList<Integer>();
			List<Integer> g3arr = new ArrayList<Integer>();
			List<Integer> g4arr = new ArrayList<Integer>();
			List<Integer> g5arr = new ArrayList<Integer>();
			List<Integer> g6arr = new ArrayList<Integer>();
			List<Integer> darr = new ArrayList<Integer>();
			List<Integer> xarr = new ArrayList<Integer>();
			for (PieVo o : pfbzVos) {
				if(o.getItem().equals("0")){
					g0arr.add(o.getInum());
				}
				else if(o.getItem().equals("1")){
					g1arr.add(o.getInum());
				}
				else if(o.getItem().equals("2")){
					g2arr.add(o.getInum());
				}
				else if(o.getItem().equals("3")){
					g3arr.add(o.getInum());
				}
				else if(o.getItem().equals("4")){
					g4arr.add(o.getInum());
				}
				else if(o.getItem().equals("5")){
					g5arr.add(o.getInum());
				}
				else if(o.getItem().equals("6")){
					g6arr.add(o.getInum());
				}
				else if(o.getItem().equals("D")){
					darr.add(o.getInum());
				}
				else{
					xarr.add(o.getInum());
				}
			}
			if(g0arr.size()==0){
				g0arr.add(0);
			}
			if(g1arr.size()==0){
				g1arr.add(0);
			}
			if(g2arr.size()==0){
				g2arr.add(0);
			}
			if(g3arr.size()==0){
				g3arr.add(0);
			}
			if(g4arr.size()==0){
				g4arr.add(0);
			}
			if(g5arr.size()==0){
				g5arr.add(0);
			}
			if(g6arr.size()==0){
				g6arr.add(0);
			}
			if(darr.size()==0){
				darr.add(0);
			}
			if(xarr.size()==0){
				xarr.add(0);
			}
			reData.put("g0arr", g0arr);
			reData.put("g1arr", g1arr);
			reData.put("g2arr", g2arr);
			reData.put("g3arr", g3arr);
			reData.put("g4arr", g4arr);
			reData.put("g5arr", g5arr);
			reData.put("g6arr", g6arr);
			reData.put("darr", darr);
			reData.put("xarr", xarr);
			reData.put("code", "1");
			reData.put("message", "查询成功");

		} catch (Exception e) {
			logger.error(e.getMessage());
			reData.put("code", "0");
			reData.put("message", "失败");
		}
		return reData;
	}
	@RequestMapping(value = "/sssl", method = RequestMethod.POST)
	@ResponseBody
	public Object ssslCount(@RequestParam Map<String, Object> params) {
		Map<String, Object> reData = new HashMap<String, Object>();
		List<PieVo> rlzlVos = new ArrayList<PieVo>();
		List<Integer> ysclarr = new ArrayList<Integer>();
		List<Integer> jslarr = new ArrayList<Integer>();
		List<Integer> cslarr = new ArrayList<Integer>();

		String strWhere = "1=1";
		if (params.containsKey("qybh") && StringUtils.isNoneBlank(params.get("qybh").toString())) {
			strWhere += String.format(" and qybh='%s'", params.get("qybh").toString());
		} else {
			try {
				String strCurrentOrgan = getCurrentUserOrganCode();
				if (strCurrentOrgan != null) {
					if(strCurrentOrgan.equals("1306020010")){
						strWhere += " and (qybh='1306020010' or qybh='1306020001' or qybh='1733227357')";
					}
					else {
						String strOrgans = organService.findChildrenCodes(strCurrentOrgan);
						if (StringUtils.isNoneBlank(strOrgans)) {
							strOrgans = strCurrentOrgan + "," + strOrgans;
							strOrgans = strOrgans.replace(",", "','");
							strWhere += String.format(" and qybh in ('%s')", strOrgans);
						} else {
							strWhere += String.format(" and qybh='%s'", strCurrentOrgan);
						}
					}
				} else {
					return null;
				}
			}
			catch (Exception e){
				return  null;
			}

		}
		String sql = "select count(*) as inum from mj_vehicle where "+strWhere;
		try {
			rlzlVos = publicService.selectObjs(sql, PieVo.class);

			for (PieVo o : rlzlVos) {
				ysclarr.add(o.getInum());
			}
			if(ysclarr.size()==0){
				ysclarr.add(0);
			}
			reData.put("ysclarr", ysclarr);
			formatDate = new SimpleDateFormat(DATE_FORMAT);
			String sdatenow = formatDate.format(new Date());
			strWhere=strWhere+" and tgkssj>='"+sdatenow+" 00:00:00' and tgkssj<'"+sdatenow+" 23:59:59'";
			sql = "select distinct jclx as item,count(*) as inum from mj_data_base where "+strWhere+" group by jclx order by jclx";
			rlzlVos = publicService.selectObjs(sql, PieVo.class);
			for (PieVo o : rlzlVos) {
				if(o.getItem().equals("1")) {
					jslarr.add(o.getInum());
				}
				else{
					cslarr.add(o.getInum());
				}
			}
			if(jslarr.size()==0){
				jslarr.add(0);
			}
			if(cslarr.size()==0){
				cslarr.add(0);
			}
			reData.put("cslarr", cslarr);
			reData.put("jslarr", jslarr);
			reData.put("code", "1");
			reData.put("message", "查询成功");

		} catch (Exception e) {
			logger.error(e.getMessage());
			reData.put("code", "0");
			reData.put("message", "失败");
		}
		return reData;
	}
}
