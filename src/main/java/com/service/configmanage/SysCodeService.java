package com.service.configmanage;

import com.commons.result.Tree;
import com.commons.result.TreeType;
import com.model.configmange.SysCode;

import java.util.List;

public interface SysCodeService {
    /**
     * 根据id基础代码
     *
     * @param id
     * @return
     */
    SysCode findSyscodeById(String id);

    /**
     * 根据syscode查询记录和syscode.id不同的
     * @param sysCode
     * @return
     */
    SysCode findNotSyscode(SysCode sysCode);
    /**
     * 查询基础代码树
     *
     * @return
     */
    List<Tree> findTree();
    /**
     * 根据oi_name获取代码Combox列表json
     * @param oi_name
     * @return
     */
    String findSyscodeCombox(String oi_name);
    /**
     * 根据获取所有Combox列表json
     * @param oi_name
     * @return
     */
    String findSyscodeComboxAll();

    List<TreeType> findTreeType();
    String getCodeByValue(String oi_name, String oi_value);
    public String getValueByCode(String oi_name, String oi_code);
    public String getJclxbyCode(String jclx);
    public String getBgztbyCode(String bgzt);
}
