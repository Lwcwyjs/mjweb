package com.service.sysmanage;

import com.model.sysmanage.SysUser;
import com.model.securitymange.SysTerminalInfo;

import java.util.Map;

/**
 * @description：用户管理
 * @author：zhixuan.wang
 * @date：2015/10/1 14:51
 */
public interface SysUserService {
    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    SysUser findUserByLoginName(String username);
    
    /**
     * 根据IP查询终端记录
     *
     * @return
     */
    SysTerminalInfo findTerminalByIp();

    /**
     * 根据用户id查询用户
     *
     * @param id
     * @return
     */
    SysUser findUserById(Long id);

    /**
     * 添加用户
     *
     * @param sysUser
     */
    void addUser(SysUser sysUser);

    /**
     * 修改用户
     *
     * @param sysUser
     */
    void updateUser(SysUser sysUser);
    /**
     * 修改用户
     *
     * @param sysUser
     */
    void updateUserex(SysUser sysUser);
    /**
     * 删除用户
     *
     * @param id
     */
    void deleteUserById(Long id);
    
    Map<String,Object> findBaseData(String param);

}
