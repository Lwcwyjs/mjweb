package com.service.configmanage;

import com.alibaba.fastjson.JSON;
import com.commons.result.Tree;
import com.commons.result.TreeType;
import com.model.configmange.SysCode;
import com.service.base.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysCodeServiceImpl implements SysCodeService {
	@Autowired
    @Resource
    private PublicService publicService;
	/**
     * 根据id获取Syscode给数据字典修改页面赋值
     * @param id
     * @return Syscode
     */
	@Override
	public SysCode findSyscodeById(String id) {
		// TODO Auto-generated method stub
		SysCode sysCode =new SysCode();
		sysCode.setId(id);
		return sysCode(sysCode);
	}
	/**
     * 修改页面保存时，对比输入的Syscode与数据库中的syscode是否相同
     * @param sysCode
     * @return Syscode
     */
	@Override
	public SysCode findNotSyscode(SysCode sysCode){
		return sysCodeOnly(sysCode);
	};
	/**
     * 加载页面左侧基础数据数
     * @param 
     * @return 
     */
	@Override
	public List<Tree> findTree() {
		List<Tree> trees = new ArrayList<Tree>();
		List<Tree> roots = new ArrayList<Tree>();
		Tree roottree = new Tree();
		roottree.setText("类别");
		roottree.setCode("类别");
		roottree.setIconCls("icon-company");
        SysCode sysCode =new SysCode();
        sysCode.setOi_name("类别");
		@SuppressWarnings("unchecked")
		List<SysCode> syscodeFather = findSyscodeAll(sysCode);
		if (syscodeFather != null) {
			for (SysCode syscode2 : syscodeFather) {
				Tree tree = new Tree();
				tree.setPcode(syscode2.getOi_value());
				tree.setText(syscode2.getOi_value());
				tree.setCode(syscode2.getOi_value());
				tree.setIconCls("icon-folder");
				roots.add(tree);
			}
		}
		roottree.setChildren(roots);
		trees.add(roottree);
		return trees;
	}

	/**
     * 加载页面左侧基础数据数
     * @param
     * @return
     */
	@Override
	public List<TreeType> findTreeType() {
		List<TreeType> trees = new ArrayList<TreeType>();
		List<TreeType> roots = new ArrayList<TreeType>();
		TreeType roottree = new TreeType();
		roottree.setName("类别");
		roottree.setCode("类别");
		roottree.setIconCls("icon-company");
        SysCode sysCode =new SysCode();
        sysCode.setOi_name("类别");
		@SuppressWarnings("unchecked")
		List<SysCode> syscodeFather = findSyscodeAll(sysCode);
		if (syscodeFather != null) {
			for (SysCode syscode2 : syscodeFather) {
				TreeType tree = new TreeType();
				tree.setPcode(syscode2.getOi_value());
				tree.setName(syscode2.getOi_value());
				tree.setCode(syscode2.getOi_value());
				tree.setIconCls("icon-folder");
				roots.add(tree);
			}
		}
		roottree.setChildren(roots);
		trees.add(roottree);
		return trees;
	}
	@Override
	/**
     * 根据获取所有Combox列表json
     * @param oi_name
     * @return
     */
	public String findSyscodeCombox(String oi_name){
		SysCode sysCode = new SysCode();
		sysCode.setOi_name(oi_name);
		List<SysCode> syscodelist =findSyscodeAll(sysCode);
		return JSON.toJSONString(syscodelist);
	}
	@Override
	/**
     * 根据获取所有Combox列表json
     * @param oi_name
     * @return
     */
	public String findSyscodeComboxAll(){
		SysCode sysCode = new SysCode();
		sysCode.setOi_code("0");
		sysCode.setOi_name("类别");
		sysCode.setOi_value("类别");
		List<SysCode> syscodelist =  findSyscodeAll(sysCode);
		syscodelist.add(0, sysCode);
		return JSON.toJSONString(syscodelist);
	};
	/**
     * 根据id查询数据库中的数据
     * @param id
     * @return Syscode
     */
	public SysCode sysCode(SysCode sysCode)
	{
		sysCode =(SysCode)publicService.selectObject("ai_sys_code", "", "id", sysCode);
		return sysCode;
	}
	/**
     * 根据oi_name,oi_value,oi_code查询数据库中是否存在相同数据
     * @param id
     * @return Syscode
     */
	public SysCode sysCodeOnly(SysCode sysCode)
	{
		sysCode =(SysCode)publicService.selectObject("ai_sys_code", "", "oi_name,oi_value,oi_code", sysCode);
		return sysCode;
	}
	@SuppressWarnings("unchecked")
	public List<SysCode> findSyscodeAll(SysCode sysCode) {
		// TODO Auto-generated method stub
		return (List<SysCode>)(List)publicService.selectObjects("ai_sys_code", "", "OI_NAME","seq,oi_code", sysCode);
	}
	@Override
	public String getCodeByValue(String oi_name, String oi_value) {
		// TODO Auto-generated method stub
		String oi_code = oi_value;
		String sql = "select * from AI_SYS_CODE t "
				+ "where oi_name='"+oi_name+"' and oi_value = '"+oi_value+"'";

		SysCode code1 = publicService.selectObj(sql, SysCode.class);
		if(null!=code1){
			oi_code = code1.getOi_code();
		}

		return oi_code;
	}
	@Override
	public String getValueByCode(String oi_name, String oi_code) {
		// TODO Auto-generated method stub
		String oi_value = oi_code;
		String sql = "select * from AI_SYS_CODE t "
				+ "where oi_name='"+oi_name+"' and oi_code = '"+oi_code+"'";

		SysCode code1 = publicService.selectObj(sql, SysCode.class);
		if(null!=code1){
			oi_value = code1.getOi_value();
		}

		return oi_value;
	}
	@Override
	public String getJclxbyCode(String jclx){
		String svalue=jclx;
		if(jclx.equals("1")){
			svalue="进";
		}
		else if(jclx.equals("2")){
			svalue="出";
		}
		return svalue;
	}
	@Override
	public String getBgztbyCode(String bgzt){
		String svalue=bgzt;
		if(bgzt.equals("0")){
			svalue="未摆杆";
		} else if(bgzt.equals("1")){
			svalue="手动抬杆";
		}
		else if(bgzt.equals("2")){
			svalue="自动抬杆";
		}
		return svalue;
	}
}
