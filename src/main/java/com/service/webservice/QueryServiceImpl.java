package com.service.webservice;

import com.commons.result.XmlQueryResult;
import com.commons.utils.JaxbUtil;
import com.commons.utils.XmlHelper;
import com.model.businessmange.MjYshw;
import com.model.local.MjBmd;
import com.model.local.YuYue;
import com.model.sysmanage.MjDzxx;
import com.model.sysmanage.SysOrganization;
import com.model.vehmanage.MjFdlVehicle;
import com.model.vehmanage.MjInsideVehicle;
import com.model.vehmanage.MjVehicle;
import com.model.videomanage.MjSpxx;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QueryServiceImpl implements QueryService {
    private final Logger logger = LoggerFactory.getLogger(QueryServiceImpl.class);
    @Autowired
    private PublicService publicService;
    @Autowired
    public SysOptionService sysOptionService;

    public XmlQueryResult dealCommand(String qybh, String jkxlh, String jkid, String sXmlDoc) throws UnsupportedEncodingException {

        boolean isrt = false;
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        SysOrganization sysOrganization = new SysOrganization();
        sysOrganization.setOrgan(qybh);
        String cojkxlh = DigestUtils.md5Hex(DigestUtils.md5Hex(qybh + "Bdmj"));
        if (!cojkxlh.equals(jkxlh)) {
            xmlQueryResult.setCodeMessage(0, "授权序列号非法", "0", "");
            isrt = true;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        if (!isrt) {
            sysOrganization = publicService.selectObj("ai_sys_organization", "", "organ", sysOrganization);
            if (sysOrganization == null) {
                xmlQueryResult.setCodeMessage(0, "该企业未授权", "0", "");
                isrt = true;
            }
        }
        XmlHelper xmlHelper = new XmlHelper();
        XmlHelper xmlHelperHead = new XmlHelper();
        sXmlDoc = URLDecoder.decode(sXmlDoc, "UTF-8");
        xmlHelper.loadFromStr(sXmlDoc);
        String xmlDoc = xmlHelper.getInnerText("/root", "vehcrpara");
        String cphm="";
        String cpys="";
        String dzbhs = xmlHelper.getInnerText("/root/vehcrpara", "dzbhs");
        if(dzbhs==null){
            dzbhs="";
        }
        if (!isrt) {
            try {
                switch (jkid) {
                    case "01Q00":
                        publicService.update("update mj_online_qy set isonline='1',onlinetime=GETDATE() where organ='"+qybh+"'");
                    // 获取车辆信息
                    case "01Q01":
                        //logger.debug("18Q01获取车辆信息");
                        cphm = xmlHelper.getInnerText("/root/vehcrpara", "cphm");
                        cpys = xmlHelper.getInnerText("/root/vehcrpara", "cpys");
                        xmlQueryResult = queryMjVehicle(cphm, cpys, qybh);
                        break;
                    //获取下载信息
                    case "01Q02":
                        xmlQueryResult = queryDownload(qybh,dzbhs);
                        break;
                    //获取下载信息
                    case "01Q03":
                        xmlQueryResult = queryDownloadQyxx(qybh);
                        break;
                    //获取下载信息
                    case "01Q04":
                        cphm = xmlHelper.getInnerText("/root/vehcrpara", "cphm");
                        cpys = xmlHelper.getInnerText("/root/vehcrpara", "cpys");
                        String dzbh = xmlHelper.getInnerText("/root/vehcrpara", "dzbh");
                        xmlQueryResult = queryDownloadBmd(cphm, cpys, qybh,dzbh);
                        break;
                    //获取下载信息
                    case "01Q05":
                        cphm = xmlHelper.getInnerText("/root/vehcrpara", "cphm");
                        xmlQueryResult = queryDownloadYuyue(cphm,qybh);
                        break;
                    default:
                        xmlQueryResult.setCodeMessage(-1, "无当前接口ID的处理功能，请确认调用的方法是否正确:"
                                + jkid, "0", "");
                        break;
                }
            } catch (Exception e) {
                xmlQueryResult.setCodeMessage(-1, jkid + "处理异常:" + e.getMessage(),
                        "0", "");
                logger.error(xmlQueryResult.getMessage());
            }

        }
        return xmlQueryResult;
    }

    private XmlQueryResult queryDownload(String qybh,String dzbhs) {
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        xmlQueryResult.setCodeMessage(0, "无需要下载信息", "0","");
        boolean isrt = false;
        try {
            MjDzxx mjDzxx = new MjDzxx();
            mjDzxx.setQybh(qybh);
            mjDzxx.setSczt("0");
            mjDzxx = publicService.selectObj("mj_dzxx", "", "qybh,sczt", mjDzxx);
            if (mjDzxx != null) {
                String sdzxx = JaxbUtil.convertToXml(mjDzxx, "UTF-8");
                sdzxx = sdzxx.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                sdzxx = sdzxx.replace("<vehcrpara>", "");
                sdzxx = sdzxx.replace("</vehcrpara>", "");
                mjDzxx.setSczt("1");
                mjDzxx.setScjg("成功");
                publicService.update("mj_dzxx", "id", "id", mjDzxx);
                xmlQueryResult.setCodeMessage(1, "数据下载成功", "1", sdzxx);
                //logger.debug("获取道闸信息成功" + xmlQueryResult.getXml());
                isrt = true;
            }
            if (!isrt) {
                MjSpxx mjSpxx = new MjSpxx();
                mjSpxx.setQybh(qybh);
                mjSpxx.setSczt("0");
                mjSpxx = publicService.selectObj("mj_spxx", "", "qybh,sczt", mjSpxx);
                if (mjSpxx != null) {
                    String sspxx = JaxbUtil.convertToXml(mjSpxx, "UTF-8");
                    sspxx = sspxx.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                    sspxx = sspxx.replace("<vehcrpara>", "");
                    sspxx = sspxx.replace("</vehcrpara>", "");
                    mjSpxx.setSczt("1");
                    mjSpxx.setScjg("成功");
                    publicService.update("mj_spxx", "id", "id", mjSpxx);
                    xmlQueryResult.setCodeMessage(2, "数据下载成功", "1", sspxx);
                    //logger.debug("获取视频信息成功" + xmlQueryResult.getXml());
                    isrt = true;
                }
            }

            if (!isrt) {
                MjVehicle mjVehicle = new MjVehicle();
                mjVehicle.setQybh(qybh);
                mjVehicle.setSczt("0");
                String strWhere=" where 1=1";
                strWhere=strWhere+" and  sczt='0' and qybh='"+qybh+"'";
                if(!dzbhs.equals("")) {
                    String dzWhere = "";
                    String[] dzarray = dzbhs.split(",");
                    if (dzarray.length > 0) {
                        dzWhere = " and (";
                        for (int i = 0; i < dzarray.length; i++) {
                            if (dzWhere.equals(" and (")) {
                                dzWhere = dzWhere + " dzbhs like '%" + dzarray[i] + "%'";
                            } else {
                                dzWhere = dzWhere + " or dzbhs like '%" + dzarray[i] + "%'";
                            }
                        }
                        dzWhere = dzWhere + ")";
                    }
                    if (!dzWhere.equals("")) {
                        strWhere = strWhere + dzWhere;
                    }
                }
                mjVehicle = publicService.selectObj("select * from mj_vehicle "+strWhere,MjVehicle.class);
                if (mjVehicle != null) {
                    String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                    if (mjVehicle.getXszazp() != null && !mjVehicle.getXszazp().equals("")) {
                        mjVehicle.setXszazp(photourl + mjVehicle.getXszazp());
                    }
                    if (mjVehicle.getXszbzp() != null && !mjVehicle.getXszbzp().equals("")) {
                        mjVehicle.setXszbzp(photourl + mjVehicle.getXszbzp());
                    }
                    if (mjVehicle.getScqdzp() != null && !mjVehicle.getScqdzp().equals("")) {
                        mjVehicle.setScqdzp(photourl + mjVehicle.getScqdzp());
                    }
                    if (mjVehicle.getVehiclezp() != null && !mjVehicle.getVehiclezp().equals("")) {
                        mjVehicle.setVehiclezp(photourl + mjVehicle.getVehiclezp());
                    }
                    String svehicle = JaxbUtil.convertToXml(mjVehicle, "UTF-8");
                    svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                    svehicle = svehicle.replace("<vehcrpara>", "");
                    svehicle = svehicle.replace("</vehcrpara>", "");
                    mjVehicle.setSczt("1");
                    mjVehicle.setScjg("成功");
                    String hxdzbhs=mjVehicle.getDzbhs();
                    String tbdzbhs="已同步";
                    if(mjVehicle.getTbdzbhs() != null && !mjVehicle.getTbdzbhs().equals("")) {
                        tbdzbhs=mjVehicle.getTbdzbhs();
                    }

                    if(!dzbhs.equals("")) {
                        tbdzbhs = mjVehicle.getTbdzbhs();
                        hxdzbhs = mjVehicle.getDzbhs();
                        String[] dzarray = dzbhs.split(",");
                        if (dzarray.length > 0) {
                            for (int i = 0; i < dzarray.length; i++) {
                                hxdzbhs = hxdzbhs.replace(dzarray[i], "");
                                if(tbdzbhs.indexOf(dzarray[i])<0) {
                                    tbdzbhs = tbdzbhs + dzarray[i];
                                }
                            }
                        }
                    }
                    String sczt="1";
                    if(!dzbhs.equals("")) {
                        sczt="0";
                    }
                    if(hxdzbhs==null||hxdzbhs.equals("")) {
                        sczt = "1";
                    }
                    publicService.update("update mj_vehicle set sczt='"+sczt+"',scjg='成功',dzbhs='"+hxdzbhs+"',tbdzbhs='"+tbdzbhs+"' where id='" + mjVehicle.getId() + "'");
                    xmlQueryResult.setCodeMessage(3, "数据下载成功", "1", svehicle);
                    //logger.debug("获取车辆信息成功" + xmlQueryResult.getXml());
                    isrt = true;
                }
            }
            if (!isrt) {
                MjInsideVehicle mjInsideVehicle = new MjInsideVehicle();
                mjInsideVehicle.setQybh(qybh);
                mjInsideVehicle.setSczt("0");
                mjInsideVehicle = publicService.selectObj("mj_inside_vehicle", "", "qybh,sczt", mjInsideVehicle);
                if (mjInsideVehicle != null) {
                    String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                    if (mjInsideVehicle.getXszzp() != null && !mjInsideVehicle.getXszzp().equals("")) {
                        mjInsideVehicle.setXszzp(photourl + mjInsideVehicle.getXszzp());
                    }
                    if (mjInsideVehicle.getScqdzp() != null && !mjInsideVehicle.getScqdzp().equals("")) {
                        mjInsideVehicle.setScqdzp(photourl + mjInsideVehicle.getScqdzp());
                    }
                    String svehicle = JaxbUtil.convertToXml(mjInsideVehicle, "UTF-8");
                    svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                    svehicle = svehicle.replace("<vehcrpara>", "");
                    svehicle = svehicle.replace("</vehcrpara>", "");
                    mjInsideVehicle.setSczt("1");
                    mjInsideVehicle.setScjg("成功");
                    publicService.update("update mj_inside_vehicle set sczt='1',scjg='成功' where id='" + mjInsideVehicle.getId() + "'");
                    xmlQueryResult.setCodeMessage(4, "数据下载成功", "1", svehicle);
                    //logger.debug("获取场内车辆信息成功" + xmlQueryResult.getXml());
                    isrt = true;
                }
            }
            if (!isrt) {
                MjFdlVehicle mjFdlVehicle = new MjFdlVehicle();
                mjFdlVehicle.setQybh(qybh);
                mjFdlVehicle.setSczt("0");
                mjFdlVehicle = publicService.selectObj("mj_fdl_vehicle", "", "qybh,sczt", mjFdlVehicle);
                if (mjFdlVehicle != null) {
                    String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                    if (mjFdlVehicle.getZcmpzp() != null && !mjFdlVehicle.getZcmpzp().equals("")) {
                        mjFdlVehicle.setZcmpzp(photourl + mjFdlVehicle.getZcmpzp());
                    }
                    if (mjFdlVehicle.getHbbqzp() != null && !mjFdlVehicle.getHbbqzp().equals("")) {
                        mjFdlVehicle.setHbbqzp(photourl + mjFdlVehicle.getHbbqzp());
                    }
                    if (mjFdlVehicle.getFdjmpzp() != null && !mjFdlVehicle.getFdjmpzp().equals("")) {
                        mjFdlVehicle.setFdjmpzp(photourl + mjFdlVehicle.getFdjmpzp());
                    }
                    String svehicle = JaxbUtil.convertToXml(mjFdlVehicle, "UTF-8");
                    svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                    svehicle = svehicle.replace("<vehcrpara>", "");
                    svehicle = svehicle.replace("</vehcrpara>", "");
                    mjFdlVehicle.setSczt("1");
                    mjFdlVehicle.setScjg("成功");
                    publicService.update("update mj_fdl_vehicle set sczt='1',scjg='成功' where id='" + mjFdlVehicle.getId() + "'");
                    xmlQueryResult.setCodeMessage(5, "数据下载成功", "1", svehicle);
                    //logger.debug("获取非道路车辆信息成功" + xmlQueryResult.getXml());
                    isrt = true;
                }
            }
            if (!isrt) {
                MjYshw mjYshw = new MjYshw();
                mjYshw.setQybh(qybh);
                mjYshw.setSczt("0");
                mjYshw = publicService.selectObj("select top 1 * from mj_yshw where sczt='0' and qybh='"+qybh+"'",MjYshw.class);
                if (mjYshw != null) {
                    String svehicle = JaxbUtil.convertToXml(mjYshw, "UTF-8");
                    svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                    svehicle = svehicle.replace("<vehcrpara>", "");
                    svehicle = svehicle.replace("</vehcrpara>", "");
                    mjYshw.setSczt("1");
                    mjYshw.setScjg("成功");
                    publicService.update("update mj_yshw set sczt='1',scjg='成功' where id='" + mjYshw.getId() + "'");
                    xmlQueryResult.setCodeMessage(6, "数据下载成功", "1", svehicle);
                    //logger.debug("获取车辆信息成功" + xmlQueryResult.getXml());
                    isrt = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            xmlQueryResult.setCodeMessage(0, e.toString(), "0", "");
        }
        return xmlQueryResult;
    }
    private XmlQueryResult queryDownloadQyxx(String qybh) {
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        xmlQueryResult.setCodeMessage(0, "无需要下载信息", "0","");
        boolean isrt = false;
        try {
            SysOrganization sysOrganization = new SysOrganization();
            sysOrganization.setOrgan(qybh);
            sysOrganization = publicService.selectObj("ai_sys_organization", "", "organ", sysOrganization);
            if (sysOrganization != null) {
                String ssysOrganization = JaxbUtil.convertToXml(sysOrganization, "UTF-8");
                ssysOrganization = ssysOrganization.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                ssysOrganization = ssysOrganization.replace("<vehcrpara>", "");
                ssysOrganization = ssysOrganization.replace("</vehcrpara>", "");
                xmlQueryResult.setCodeMessage(1, "数据下载成功", "1", ssysOrganization);
                //logger.debug("获取企业信息成功" + xmlQueryResult.getXml());
            }
        } catch (Exception e) {
            e.printStackTrace();
            xmlQueryResult.setCodeMessage(0, e.toString(), "0", "");
        }
        return xmlQueryResult;
    }

    private XmlQueryResult queryMjVehicle(String cphm, String cpys, String qybh) {
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        try {
            MjVehicle mjVehicle = new MjVehicle();
            mjVehicle.setCphm(cphm);
            mjVehicle.setCpys(cpys);
            mjVehicle = publicService.selectObj("mj_vehicle", "", "cphm,cpys", mjVehicle);
            if (mjVehicle != null) {
                String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                if (mjVehicle.getXszazp() != null && !mjVehicle.getXszazp().equals("")) {
                    mjVehicle.setXszazp(photourl + mjVehicle.getXszazp());
                }
                if (mjVehicle.getXszbzp() != null && !mjVehicle.getXszbzp().equals("")) {
                    mjVehicle.setXszbzp(photourl + mjVehicle.getXszbzp());
                }
                if (mjVehicle.getScqdzp() != null && !mjVehicle.getScqdzp().equals("")) {
                    mjVehicle.setScqdzp(photourl + mjVehicle.getScqdzp());
                }
                if (mjVehicle.getVehiclezp() != null && !mjVehicle.getVehiclezp().equals("")) {
                    mjVehicle.setVehiclezp(photourl + mjVehicle.getVehiclezp());
                }
                String svehicle = JaxbUtil.convertToXml(mjVehicle, "UTF-8");
                svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                svehicle = svehicle.replace("<vehcrpara>", "");
                svehicle = svehicle.replace("</vehcrpara>", "");
                xmlQueryResult.setCodeMessage(1, "数据下载成功", "1", svehicle);
                publicService.update("update mj_vehicle set qybh='"+qybh+"' where cphm='"+cphm+"' and cpys='"+cpys+"' and qybh is null");
                //logger.debug("获取车辆信息成功" + xmlQueryResult.getXml());
            } else {
                xmlQueryResult.setCodeMessage(0, "数据下载失败", "0", "");
                //logger.debug("获取车辆信息失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            xmlQueryResult.setCodeMessage(0, e.toString(), "0", "");
        }
        return xmlQueryResult;
    }
    private XmlQueryResult queryDownloadBmd(String cphm, String cpys, String qybh,String dzbh) {
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        try {
            MjDzxx mjDzxx = new MjDzxx();
            mjDzxx.setQybh(qybh);
            mjDzxx.setDzbh(dzbh);
            mjDzxx=publicService.selectObj("mj_dzxx", "", "qybh,dzbh", mjDzxx);
            if (mjDzxx != null) {
                String dzmc=mjDzxx.getDzmc();
                Integer number = extractNumberBeforeHao(dzmc);
                if(number!=null){
                    String crkh=number.toString();
                    MjBmd mjBmd = new MjBmd();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date now = new Date();
                    String nowdate=sdf.format(new Date());
                    mjBmd = publicService.selectObj("select * from mj_bmd where cphm='"+cphm+"' and cpys='"+cpys+"' and organ='"+qybh+"' and sxsj<='"
                            +nowdate+"' and zzsj>='"+nowdate+"'", MjBmd.class);
                    if (mjBmd != null) {
                        String svehicle = JaxbUtil.convertToXml(mjBmd, "UTF-8");
                        svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                        svehicle = svehicle.replace("<vehcrpara>", "");
                        svehicle = svehicle.replace("</vehcrpara>", "");
                        String crkbh=mjBmd.getCrkbh();
                        if(crkbh!=null&&crkbh.contains(crkh)) {
                            xmlQueryResult.setCodeMessage(1, "白名单车辆", "1", svehicle);
                        }
                        else {
                            xmlQueryResult.setCodeMessage(0, "非此门白名单车辆", "1", svehicle);
                        }
                    } else {
                        xmlQueryResult.setCodeMessage(0, "无白名单信息", "0", "");
                    }
                }
                else{
                    xmlQueryResult.setCodeMessage(0, "无白名单信息", "0", "");
                }
            }
            else{
                xmlQueryResult.setCodeMessage(0, "道闸无对应信息", "0", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            xmlQueryResult.setCodeMessage(0, e.toString(), "0", "");
        }
        return xmlQueryResult;
    }
    private XmlQueryResult queryDownloadYuyue(String cphm,  String qybh) {
        XmlQueryResult xmlQueryResult = new XmlQueryResult();
        try {
            MjDzxx mjDzxx = new MjDzxx();
            mjDzxx.setQybh(qybh);

                    YuYue yuYue = new YuYue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowdate=sdf.format(new Date());
                    yuYue = publicService.selectObj("select * from yuyue where licenseplate='"+cphm+"' and organ='"+qybh+"' and begintime<='"
                            +nowdate+"' and endtime>='"+nowdate+"'", YuYue.class);
                    if (yuYue != null) {
                        String svehicle = JaxbUtil.convertToXml(yuYue, "UTF-8");
                        svehicle = svehicle.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
                        svehicle = svehicle.replace("<vehcrpara>", "");
                        svehicle = svehicle.replace("</vehcrpara>", "");
                        xmlQueryResult.setCodeMessage(1, "车辆已预约", "1", svehicle);
                    } else {
                        xmlQueryResult.setCodeMessage(0, "未预约", "0", "");
                    }
        } catch (Exception e) {
            e.printStackTrace();
            xmlQueryResult.setCodeMessage(0, e.toString(), "0", "");
        }
        return xmlQueryResult;
    }
    /**
     * 提取字符串中"号"字前面的数字
     * @param str 待处理的字符串
     * @return 提取到的数字，若未找到则返回null
     */
    public static Integer extractNumberBeforeHao(String str) {
        // 正则表达式：匹配"号"字前面的数字
        Pattern pattern = Pattern.compile("(\\d+)号");
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            // 提取"号"字前面的数字
            return Integer.parseInt(matcher.group(1));
        }

        // 未找到匹配的模式
        return null;
    }
}
