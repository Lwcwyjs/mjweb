package com.service.webservice;

import com.alibaba.fastjson.JSONObject;
import com.commons.annotation.support.ValidateService;
import com.commons.utils.JaxbUtil;
import com.commons.utils.XmlHelper;
import com.commons.utils.XmlWriteResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.businessmange.MjDataBase;
import com.model.businessmange.MjDataDztz;
import com.model.businessmange.MjDataVideo;
import com.model.configmange.SysCode;
import com.model.sysmanage.MjOnlineDz;
import com.model.sysmanage.SysOrganization;
import com.model.vehmanage.MjVehicle;
import com.service.base.PublicService;
import com.service.businessmanage.PfbzService;
import com.service.configmanage.SysOptionService;
import com.util.ConverUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WriteServiceImpl implements WriteService {
    private final Logger logger = LoggerFactory.getLogger(WriteServiceImpl.class);

    private static Logger LOGGER = LoggerFactory.getLogger(WriteServiceImpl.class);
    @Autowired
    public SysOptionService sysOptionService;

    static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private PublicService publicService;
    @Autowired
    private PfbzService pfbzService;
    private static String tokenTimeYth = "";
    // JSON解析器（Jackson）
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // 字符编码常量
    private static final String CHARSET = "UTF-8";
    private static String buildPostData(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&"); // 多参数用&分隔
            }
            // 对参数名和值进行URL编码，避免特殊字符问题
            sb.append(URLEncoder.encode(entry.getKey(), CHARSET))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), CHARSET));
        }
        return sb.toString();
    }
    public XmlWriteResult dealCommand(String qybh, String jkxlh, String jkid, String sXmlDoc) throws UnsupportedEncodingException, ParseException {
        //logger.info("进入dealCommand");
        boolean isrt = false;
        ConverUtil converUtil = new ConverUtil();
        XmlWriteResult xmlWriteResult = new XmlWriteResult();
        SysOrganization sysOrganization = new SysOrganization();
        sysOrganization.setOrgan(qybh);
        String cojkxlh = DigestUtils.md5Hex(DigestUtils.md5Hex(qybh + "Bdmj"));
        if (!cojkxlh.equals(jkxlh)) {
            xmlWriteResult.setCodeMessage("0", "授权序列号非法");
            isrt = true;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!isrt) {
            sysOrganization = publicService.selectObj("ai_sys_organization", "", "organ", sysOrganization);
            if (sysOrganization == null) {
                xmlWriteResult.setCodeMessage("0", "该企业未授权");
                isrt = true;
            }
        }
        if (!isrt) {
            XmlHelper xmlHelper = new XmlHelper();
            XmlHelper xmlHelperHead = new XmlHelper();
            xmlHelper.loadFromStr(sXmlDoc);
            String xmlVehcrpara = xmlHelper.getInnerText("/root", "vehcrpara");
            xmlVehcrpara = URLDecoder.decode(xmlVehcrpara, "UTF-8");
            if (jkid.equals("01W01")) {
                MjDataBase mjDataBase = JaxbUtil.converToJavaBean(xmlVehcrpara, MjDataBase.class);
                if (mjDataBase != null) {
                    //logger.info("反序列化正常");
                } else {
                    logger.info("反序列化失败为空");
                }
                String ctzp = mjDataBase.getCtzp();
                String cszp = mjDataBase.getCszp();
                String cpzp = mjDataBase.getCpzp();
                logger.info(new SimpleDateFormat("yyyy-MM-dd").format(mjDataBase.getCjsj()));
                String sdate = new SimpleDateFormat("yyyy-MM-dd").format(mjDataBase.getCjsj());
                if ((ctzp != null) && (!ctzp.equals(""))) {
                    ctzp = savePic(ctzp, "head", sdate, mjDataBase.getId(), "", qybh);
                }
                if ((cpzp != null) && (!cpzp.equals(""))) {
                    cpzp = savePic(cpzp, "cut", sdate, mjDataBase.getId(), "", qybh);
                }
                if ((cszp != null) && (!cszp.equals(""))) {
                    cszp = savePic(cszp, "vehicle", sdate, mjDataBase.getId(), "", qybh);
                }
                MjVehicle mjVehicle = publicService.selectObj("select * from mj_vehicle where cphm='" + mjDataBase.getCphm() + "' and cpys='" + mjDataBase.getCpys() + "'", MjVehicle.class);
                if (mjVehicle != null) {
                    if (mjVehicle.getVehiclezp() == null || mjVehicle.getVehiclezp().equals("")) {
                        mjVehicle.setVehiclezp(ctzp);
                        mjVehicle.setSczt("0");
                        mjVehicle.setScjg("");
                        publicService.update("mj_vehicle", "id", "id", mjVehicle);
                    }
                }
                mjDataBase.setCtzp(ctzp);
                mjDataBase.setCszp(cszp);
                mjDataBase.setCpzp(cpzp);
                mjDataBase.setCjsj(new Date());
                String jclx = mjDataBase.getJclx();
                Date tgjssj = mjDataBase.getTgjssj();
                String stgjssj = sdf2.format(tgjssj);
                if(mjDataBase.getGkjg().contains("地磅")) {
                    mjDataBase.setQybh("1306020010");
                }
                MjDataBase mjDataBase1 = publicService.selectObj("mj_data_base", "", "id", mjDataBase);
                if (mjDataBase1 != null) {
                    publicService.update("mj_data_base", "sczt,scjg", "id", mjDataBase);
                } else {
                    publicService.insert("mj_data_base", "sczt,scjg", "id", mjDataBase);
                }
                if(!mjDataBase.getGkjg().contains("地磅")) {
                    MjDataDztz mjDataDztz = new MjDataDztz();
                    String id = mjDataBase.getId();
                    UUID uuid = UUID.randomUUID();
                    String uuids = uuid.toString().replaceAll("-", "");
                    if (jclx.equals("1")) {
                        MjDataDztz mjDataDztz1 = new MjDataDztz();
                        mjDataDztz1.setId(uuids);
                        mjDataDztz1.setCphm(mjDataBase.getCphm());
                        mjDataDztz1.setCpys(mjDataBase.getCpys());
                        if (mjVehicle != null) {
                            mjDataDztz1.setClsbdh(mjVehicle.getClsbdh());
                            mjDataDztz1.setClppxh(mjVehicle.getClppxh());
                            mjDataDztz1.setCllx(mjVehicle.getCllx());
                            mjDataDztz1.setCcdjrq(mjVehicle.getCcdjrq());
                            mjDataDztz1.setPfbz(mjVehicle.getPfbz());
                            mjDataDztz1.setRlzl(mjVehicle.getRlzl());
                            mjDataDztz1.setLwzt(mjVehicle.getLwzt());
                            mjDataDztz1.setSyxz(mjVehicle.getSyxz());
                            mjDataDztz1.setXszzp(mjVehicle.getXszazp());
                            mjDataDztz1.setScqdzp(mjVehicle.getScqdzp());
                        }
                        mjDataDztz1.setQybh(mjDataBase.getQybh());
                        mjDataDztz1.setJcid(mjDataBase.getId());
                        mjDataDztz1.setJccrkbh(mjDataBase.getCrkbh());
                        mjDataDztz1.setJcdzbh(mjDataBase.getDzbh());
                        mjDataDztz1.setJcyshwmc(mjDataBase.getYshwmc());
                        mjDataDztz1.setJcysdw(mjDataBase.getYsdw());
                        mjDataDztz1.setJcysl(mjDataBase.getYsl());
                        mjDataDztz1.setSyr(mjDataBase.getSyr());
                        mjDataDztz1.setJczp(mjDataBase.getCtzp());
                        mjDataDztz1.setJcsj(mjDataBase.getTgkssj());
                        mjDataDztz1.setCjsj(new Date());
                        mjDataDztz = publicService.selectObj("select * from mj_data_dztz where jcid='" + id + "'", MjDataDztz.class);
                        if (mjDataDztz == null) {
                            publicService.insert("mj_data_dztz", "", "", mjDataDztz1);
                        } else {
                            publicService.update("mj_data_dztz", "", "jcid", mjDataDztz1);
                        }
                    } else if (jclx.equals("2")) {
                        String ccsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mjDataBase.getTgkssj());
                        String sql = "select top 1 * from mj_data_dztz where cphm='" + mjDataBase.getCphm() + "' and cpys='" + mjDataBase.getCpys()
                                + "' and qybh='" + mjDataBase.getQybh() + "' and ccid is null and jcsj<'" + ccsj + "'";
                        logger.info(sql);
                        mjDataDztz = publicService.selectObj("select top 1 * from mj_data_dztz where cphm='" + mjDataBase.getCphm() + "' and cpys='" + mjDataBase.getCpys()
                                + "' and qybh='" + mjDataBase.getQybh() + "' and  jcsj<'" + ccsj + "' order by jcsj desc", MjDataDztz.class);
                        if (mjDataDztz != null) {
                            logger.info("匹配到进场记录:" + mjDataBase.getCphm());
                            mjDataDztz.setCcid(mjDataBase.getId());
                            mjDataDztz.setCccrkbh(mjDataBase.getCrkbh());
                            mjDataDztz.setCcdzbh(mjDataBase.getDzbh());
                            mjDataDztz.setCcyshwmc(mjDataBase.getYshwmc());
                            mjDataDztz.setCcysdw(mjDataBase.getYsdw());
                            mjDataDztz.setCcysl(mjDataBase.getYsl());
                            mjDataDztz.setCczp(mjDataBase.getCtzp());
                            mjDataDztz.setCcsj(mjDataBase.getTgkssj());
                            String syr = mjDataBase.getSyr();
                            if (syr != null) {
                                if (!syr.equals("未知") && !syr.equals("")) {
                                    mjDataDztz.setSyr(mjDataBase.getSyr());
                                }
                            }
                            publicService.update("mj_data_dztz", "id", "id", mjDataDztz);
                        } else {
                            logger.info("未匹配到进场记录:" + mjDataBase.getCphm());
                        }
                    }
                }
                if(qybh.equals("1306020001")|qybh.equals("1733227357")){
                    mjDataBase.setQybh("1306020010");
                    mjDataBase.setId(DigestUtils.md5Hex(mjDataBase.getId()));
                    if(!mjDataBase.getGkjg().contains("地磅")) {
                        MjDataDztz mjDataDztz = new MjDataDztz();
                        String id = mjDataBase.getId();
                        UUID uuid = UUID.randomUUID();
                        String uuids = uuid.toString().replaceAll("-", "");
                        if (jclx.equals("1")) {
                            MjDataDztz mjDataDztz1 = new MjDataDztz();
                            mjDataDztz1.setId(uuids);
                            mjDataDztz1.setCphm(mjDataBase.getCphm());
                            mjDataDztz1.setCpys(mjDataBase.getCpys());
                            if (mjVehicle != null) {
                                mjDataDztz1.setClsbdh(mjVehicle.getClsbdh());
                                mjDataDztz1.setClppxh(mjVehicle.getClppxh());
                                mjDataDztz1.setCllx(mjVehicle.getCllx());
                                mjDataDztz1.setCcdjrq(mjVehicle.getCcdjrq());
                                mjDataDztz1.setPfbz(mjVehicle.getPfbz());
                                mjDataDztz1.setRlzl(mjVehicle.getRlzl());
                                mjDataDztz1.setLwzt(mjVehicle.getLwzt());
                                mjDataDztz1.setSyxz(mjVehicle.getSyxz());
                                mjDataDztz1.setXszzp(mjVehicle.getXszazp());
                                mjDataDztz1.setScqdzp(mjVehicle.getScqdzp());
                            }
                            mjDataDztz1.setQybh(mjDataBase.getQybh());
                            mjDataDztz1.setJcid(mjDataBase.getId());
                            mjDataDztz1.setJccrkbh(mjDataBase.getCrkbh());
                            mjDataDztz1.setJcdzbh(mjDataBase.getDzbh());
                            mjDataDztz1.setJcyshwmc(mjDataBase.getYshwmc());
                            mjDataDztz1.setJcysdw(mjDataBase.getYsdw());
                            mjDataDztz1.setJcysl(mjDataBase.getYsl());
                            mjDataDztz1.setSyr(mjDataBase.getSyr());
                            mjDataDztz1.setJczp(mjDataBase.getCtzp());
                            mjDataDztz1.setJcsj(mjDataBase.getTgkssj());
                            mjDataDztz1.setCjsj(new Date());
                            mjDataDztz = publicService.selectObj("select * from mj_data_dztz where jcid='" + id + "' and qybh='"+mjDataBase.getQybh()+"'", MjDataDztz.class);
                            if (mjDataDztz == null) {
                                publicService.insert("mj_data_dztz", "", "", mjDataDztz1);
                            } else {
                                publicService.update("mj_data_dztz", "", "jcid", mjDataDztz1);
                            }
                        } else if (jclx.equals("2")) {
                            String ccsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mjDataBase.getTgkssj());
                            String sql = "select top 1 * from mj_data_dztz where cphm='" + mjDataBase.getCphm() + "' and cpys='" + mjDataBase.getCpys()
                                    + "' and qybh='" + mjDataBase.getQybh() + "'  and ccid is null and jcsj<'" + ccsj + "'";
                            logger.info(sql);
                            mjDataDztz = publicService.selectObj("select top 1 * from mj_data_dztz where cphm='" + mjDataBase.getCphm() + "' and cpys='" + mjDataBase.getCpys()
                                    + "' and qybh='" + mjDataBase.getQybh() + "' and  jcsj<'" + ccsj + "' order by jcsj desc", MjDataDztz.class);
                            if (mjDataDztz != null) {
                                //logger.info("匹配到进场记录:" + mjDataBase.getCphm());
                                mjDataDztz.setCcid(mjDataBase.getId());
                                mjDataDztz.setCccrkbh(mjDataBase.getCrkbh());
                                mjDataDztz.setCcdzbh(mjDataBase.getDzbh());
                                mjDataDztz.setCcyshwmc(mjDataBase.getYshwmc());
                                mjDataDztz.setCcysdw(mjDataBase.getYsdw());
                                mjDataDztz.setCcysl(mjDataBase.getYsl());
                                mjDataDztz.setCczp(mjDataBase.getCtzp());
                                mjDataDztz.setCcsj(mjDataBase.getTgkssj());
                                String syr = mjDataBase.getSyr();
                                if (syr != null) {
                                    if (!syr.equals("未知") && !syr.equals("")) {
                                        mjDataDztz.setSyr(mjDataBase.getSyr());
                                    }
                                }
                                publicService.update("mj_data_dztz", "id", "id", mjDataDztz);
                            } else {
                                logger.info("未匹配到进场记录:" + mjDataBase.getCphm());
                            }
                        }
                    }
                }
                if(qybh.equals("6857061522")){
                    String tokenYth="";
                    HttpURLConnection conn = null;
                    OutputStream os = null;
                    BufferedReader br = null;
                    String photourl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                    try {
                        // 1. 构造请求URL
                        URL url = new URL("http://27.129.129.106:18888/api/api-auth/oauth/user/passEncToken");
                        conn = (HttpURLConnection) url.openConnection();

                        // 2. 配置连接参数
                        conn.setRequestMethod("POST"); // 设置POST请求
                        conn.setDoOutput(true); // 允许写入请求体
                        conn.setDoInput(true); // 允许读取响应体
                        conn.setUseCaches(false); // 禁用缓存
                        conn.setConnectTimeout(2000); // 连接超时30秒
                        conn.setReadTimeout(2000); // 读取超时30秒

                        // 3. 设置请求头（对应C#的Headers.Add）
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn.setRequestProperty("Client_id", "200010");
                        conn.setRequestProperty("Client_secret", "379082");
                        conn.setRequestProperty("Charset", CHARSET); // 设置编码

                        // 4. 构造表单参数（模拟C#的CreatePostData方法）
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("username", "slcj001");
                        parameters.put("password", "04150233f7e45d0119e1fa4e3e9931305230ab1018e0f6edf08d134cab7c3577507036686ddf72554563627ec3c38acf6f67672724fb0aaf03ef194907af8d4eeb584a0b4b8844c8433bc832427400526f04be4cc46aead5d68398f5a75194e39996e8d6c7865abc924a82fc");
                        String postData = buildPostData(parameters);

                        // 5. 写入请求体参数
                        os = conn.getOutputStream();
                        os.write(postData.getBytes(CHARSET));
                        os.flush();

                        // 6. 检查HTTP响应状态
                        int responseCode = conn.getResponseCode();
                        logger.debug("获取token返回responseCode:"+String.valueOf(responseCode));
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // 7. 读取响应体
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
                            StringBuilder responseBody = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                responseBody.append(line);
                            }
                            String sRtzb = responseBody.toString();
                            logger.debug( sRtzb);

                            // 8. 解析JSON响应（和之前逻辑一致）
                            JsonNode jRtzb = objectMapper.readTree(sRtzb);
                            String rcode = jRtzb.get("code").asText();

                            if ("10000".equals(rcode)) {
                                // 获取token成功
                                tokenTimeYth = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                                JsonNode dataNode = jRtzb.get("data");
                                tokenYth = dataNode.get("access_token").asText();
                                logger.debug("获取token成功:" + tokenYth);
                            } else {
                                // 获取token失败
                                String rmsg = jRtzb.get("msg").asText();
                                tokenYth = "";
                                logger.debug("获取token失败:" + rmsg);
                            }
                        }
                        else{
                            logger.debug("请求失败，HTTP状态码: " + responseCode);
                        }


                    } catch (Exception e) {
                        // 捕获所有异常（网络、IO、JSON解析等）
                        logger.debug("请求/解析异常: " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        // 9. 关闭资源（必须释放，避免内存泄漏）
                        try {
                            if (br != null) br.close();
                            if (os != null) os.close();
                            if (conn != null) conn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!tokenYth.equals("")){
                        try {
                            // 1. 构造请求URL
                            URL url = new URL("http://27.129.129.106:18888/api/api-low-emission/access/upload-control-vehicles");
                            conn = (HttpURLConnection) url.openConnection();

                            // 2. 配置连接参数
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            conn.setUseCaches(false);
                            conn.setConnectTimeout(2000);
                            conn.setReadTimeout(2000);

                            // 3. 设置请求头（核心：Bearer Token + JSON类型）
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setRequestProperty("Authorization", "Bearer " + tokenYth);
                            conn.setRequestProperty("Charset", CHARSET);
                            MjDataDztz mjDataDztz = new MjDataDztz();
                            JSONObject jotxjl = new JSONObject();
                            String throughId="";
                            if(mjDataBase1!=null){
                                if(mjDataBase1.getTxid()!=null){
                                    throughId=mjDataBase1.getTxid();
                                }
                            }
                            if(mjDataBase.getJclx().equals("1")){
                                jotxjl.put("entranceNumber",mjDataBase.getCrkbh());
                                jotxjl.put("entranceGateNumber",mjDataBase.getDzbh());
                                jotxjl.put("carInTime",mjDataBase.getTgkssj());
                                jotxjl.put("entryHeadPic",photourl+mjDataBase.getCtzp());
                                jotxjl.put("entryBodyPic",photourl+mjDataBase.getCszp());
                                if(mjDataBase.getYshwmc()!=null&&!mjDataBase.getYshwmc().equals("")) {
                                    jotxjl.put("entryTransportedName", mjDataBase.getYshwmc());
                                    jotxjl.put("entryTransportedValue", mjDataBase.getYsl());
                                }
                                if(throughId.equals("")) {
                                    mjDataDztz = publicService.selectObj("select * from mj_data_dztz where jcid='" + mjDataBase.getId() + "'", MjDataDztz.class);
                                    if(mjDataDztz!=null){
                                        if(mjDataDztz.getTxid()!=null){
                                            throughId=mjDataDztz.getTxid();
                                        }
                                    }
                                }
                            }
                            else{
                                jotxjl.put("exportNumber",mjDataBase.getCrkbh());
                                jotxjl.put("exportGateNumber",mjDataBase.getDzbh());
                                jotxjl.put("carOutTime",mjDataBase.getTgkssj());
                                jotxjl.put("factoryHeadPic",photourl+mjDataBase.getCtzp());
                                jotxjl.put("factoryBodyPic",photourl+mjDataBase.getCszp());
                                if(mjDataBase.getYshwmc()!=null&&!mjDataBase.getYshwmc().equals("")) {
                                    jotxjl.put("factoryTransportedName", mjDataBase.getYshwmc());
                                    jotxjl.put("factoryTransportedValue", mjDataBase.getYsl());
                                }
                                if(throughId.equals("")) {
                                    mjDataDztz = publicService.selectObj("select * from mj_data_dztz where ccid='" + mjDataBase.getId() + "'", MjDataDztz.class);
                                    if(mjDataDztz!=null){
                                        if(mjDataDztz.getTxid()!=null){
                                            throughId=mjDataDztz.getTxid();
                                        }
                                    }
                                }
                            }
                            if (throughId.equals("")){
                                jotxjl.put("throughId",throughId);
                            }
                            jotxjl.put("carPlate",mjDataBase.getCphm());
                            jotxjl.put("carPalteColor",mjDataBase.getCpys());
                            jotxjl.put("dischargeStage",mjDataBase.getPfbz());
                            if (mjVehicle != null) {
                                jotxjl.put("carType",mjVehicle.getCllx());
                                jotxjl.put("carIdCode",mjVehicle.getClsbdh());
                                jotxjl.put("registrationDate",mjVehicle.getCcdjrq());
                                jotxjl.put("brand",mjVehicle.getClppxh());
                                jotxjl.put("carFuel",mjVehicle.getRlzl());
                                jotxjl.put("networkingStatus",mjVehicle.getLwzt());
                                jotxjl.put("carUseNature",mjVehicle.getSyxz());
                                if(mjVehicle.getScqdzp()!=null&&!mjVehicle.getScqdzp().equals("")) {
                                    jotxjl.put("accompanyPicture", photourl + mjVehicle.getScqdzp());
                                }
                                if(mjVehicle.getXszazp()!=null&&!mjVehicle.getXszazp().equals("")) {
                                    jotxjl.put("drivingPicture", photourl + mjVehicle.getXszazp());
                                }
                                jotxjl.put("motorcadeName",mjVehicle.getSyr());
                            }
                            String sjson= jotxjl.toJSONString();
                            // 4. 写入JSON请求体
                            os = conn.getOutputStream();
                            os.write(sjson.getBytes(CHARSET));
                            os.flush();
                            logger.debug(sjson);
                            // 5. 检查HTTP响应状态
                            int responseCode = conn.getResponseCode();
                            logger.debug("上传通行记录返回responseCode:"+String.valueOf(responseCode));
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                // 6. 读取响应体
                                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
                                StringBuilder responseBody = new StringBuilder();
                                String line;
                                while ((line = br.readLine()) != null) {
                                    responseBody.append(line);
                                }
                                String sRtzb = responseBody.toString();
                                logger.debug(sRtzb);

                                // 7. 解析JSON响应
                                JsonNode jRtzb = objectMapper.readTree(sRtzb);
                                String rcode = jRtzb.get("code").asText();

                                if ("10000".equals(rcode)) {
                                    // 上传成功
                                    logger.debug("上传一体化平台成功");
                                    JsonNode dataNode = jRtzb.get("data");
                                    String dataStr = dataNode.toString();

                                    // 判断txid为空且data包含throughId
                                    if (throughId.equals("") && dataStr.contains("throughId")) {
                                        throughId = dataNode.get("throughId").asText();
                                        mjDataBase.setTxid(throughId);
                                        publicService.update("mj_data_base","id","id",mjDataBase);
                                        if (mjDataDztz!=null) {
                                            mjDataDztz.setTxid(throughId);
                                            publicService.update("mj_data_dztz","id","id",mjDataDztz);
                                        }
                                    }
                                } else {
                                    // 上传失败：清空Token，记录错误信息
                                    String rmsg = jRtzb.get("msg").asText();
                                    tokenYth = "";
                                    logger.debug("上传一体化平台失败:" + rmsg);
                                }
                            }
                            else{
                                logger.debug("请求失败，HTTP状态码: " + responseCode);
                            }
                        } catch (Exception e) {
                            logger.debug("上传一体化平台异常（数据ID：" + mjDataBase.getId() + "）：" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                xmlWriteResult.setCodeMessage("1", "处理成功");
            } else if (jkid.equals("01W02")) {
                try {
                    MjVehicle mjVehicle = JaxbUtil.converToJavaBean(xmlVehcrpara, MjVehicle.class);
                    //logger.debug("开始接收车辆信息:" + mjVehicle.getCphm());
                    String ccdjrq = xmlHelper.getInnerText("/root/vehcrpara", "ccdjrq");
                    String fzrq = xmlHelper.getInnerText("/root/vehcrpara", "fzrq");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String cllx = mjVehicle.getCllx();
                    if (cllx.length() > 3) {
                        SysCode sysCode = new SysCode();
                        sysCode.setOi_name("车辆类型");
                        sysCode.setOi_value(cllx);
                        sysCode = publicService.selectObj("ai_sys_code", "", "oi_name,oi_value", sysCode);
                        if (sysCode != null) {
                            mjVehicle.setCllx(sysCode.getOi_code());
                        } else {
                            sysCode = publicService.selectObj("select * from ai_sys_code where oi_name='车辆类型' and oi_value like '%" + cllx + "%'", SysCode.class);
                            if (sysCode != null) {
                                mjVehicle.setCllx(sysCode.getOi_code());
                            } else {
                                mjVehicle.setCllx("X99");
                            }
                        }
                    }

                    if (!ccdjrq.equals("")) {
                        try {
                            mjVehicle.setCcdjrq(sdf.parse(ccdjrq));
                        } catch (Exception e) {
                            xmlWriteResult.setCodeMessage("0", "注册日期不规范");
                        }
                    }
                    if (!fzrq.equals("")) {
                        try {
                            mjVehicle.setFzrq(sdf.parse(fzrq));
                        } catch (Exception e) {
                            xmlWriteResult.setCodeMessage("0", "发证日期不规范");
                        }
                    }
                    if (mjVehicle != null) {
                        //logger.info("反序列化正常");
                    } else {
                        logger.info("反序列化失败为空");
                    }
                    if (mjVehicle.getCcdjrq() == null) {
                        xmlWriteResult.setCodeMessage("0", "注册日期为空");
                    } else if (mjVehicle.getClsbdh() == null || mjVehicle.getClsbdh().equals("")) {
                        xmlWriteResult.setCodeMessage("0", "车架号为空");
                    } else {
                        //logger.debug("重新判定排放:" + mjVehicle.getCphm());
                        String xszazp = mjVehicle.getXszazp();
                        String scqdzp = mjVehicle.getScqdzp();
                        String vehiclezp = mjVehicle.getVehiclezp();
                        String pfbz = mjVehicle.getPfbz();
                        String clsbdh = mjVehicle.getClsbdh();
                        if (mjVehicle.getClsbdh() == null || mjVehicle.getClsbdh().equals("")) {
                            xmlWriteResult.setCodeMessage("0", "车架号为空");
                        } else {
                            mjVehicle = pfbzService.getPfbz(mjVehicle);
                            if (mjVehicle.getPfpdyjzl() != null) {
                                if (mjVehicle.getPfpdyjzl().equals("2") || mjVehicle.getPfpdyjzl().equals("3")) {

                                } else {
                                    if (!pfbz.equals("") && !pfbz.equals("X")) {
                                        mjVehicle.setPfbz(pfbz);
                                        mjVehicle.setPfpdyj("老系统导入");
                                    }
                                }
                            }
                            if (mjVehicle.getPfbz() == null || mjVehicle.getPfbz().equals("")) {
                                mjVehicle.setPfbz(pfbz);
                                mjVehicle.setPfpdyj("老系统导入");
                            }
                            mjVehicle.setRlzl(converUtil.convertrlzl(converUtil.convertrlzlex(mjVehicle.getRlzl())));
                            String syxz = mjVehicle.getSyxz();
                            cllx = mjVehicle.getCllx();
                            if (syxz == null || syxz.equals("")) {
                                if (cllx != null || !cllx.equals("")) {
                                    if ((cllx.indexOf("K3") >= 0) || (cllx.indexOf("K4") >= 0)) {
                                        syxz = "A";
                                    } else if ((cllx.indexOf("K1") >= 0) || (cllx.indexOf("K2") >= 0)) {
                                        syxz = "B";
                                    } else {
                                        syxz = "F";
                                    }
                                    mjVehicle.setSyxz(syxz);
                                }
                            }
                            String clzl = mjVehicle.getClzl();
                            if (clzl == null || clzl.equals("")) {
                                if (cllx != null || !cllx.equals("")) {
                                    if (cllx.indexOf("K") >= 0) {
                                        clzl = "4";
                                    } else if (cllx.indexOf("Z") >= 0) {
                                        clzl = "2";
                                    } else {
                                        clzl = "0";
                                    }
                                    if (syxz.equals("R")) {
                                        clzl = "1";
                                    }
                                    mjVehicle.setClzl(clzl);
                                }
                            }
                            String lwzt = mjVehicle.getLwzt();
                            if (lwzt == null || lwzt.equals("")) {
                                mjVehicle.setLwzt("0");
                            }
                            String fdjh = mjVehicle.getFdjh();
                            if (fdjh == null || fdjh.equals("")) {
                                mjVehicle.setFdjh("-");
                            }
                            logger.debug("存照片:" + mjVehicle.getCphm());
                            String sdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            if ((xszazp != null) && (!xszazp.equals(""))) {
                                xszazp = savePic(xszazp, "head", sdate, mjVehicle.getId(), "xsz", qybh);
                            }
                            if ((scqdzp != null) && (!scqdzp.equals(""))) {
                                scqdzp = savePic(scqdzp, "cut", sdate, mjVehicle.getId(), "scqd", qybh);
                            }
                            if ((vehiclezp != null) && (!vehiclezp.equals(""))) {
                                vehiclezp = savePic(vehiclezp, "vehicle", sdate, mjVehicle.getId(), "vehicle", qybh);
                            }
                            //logger.debug("存照片完毕:" + mjVehicle.getCphm());
                            mjVehicle.setXszazp(xszazp);
                            mjVehicle.setScqdzp(scqdzp);
                            mjVehicle.setVehiclezp(vehiclezp);
                            mjVehicle.setCjsj(new Date());
                            mjVehicle.setSczt("0");
                            mjVehicle.setScjg("");
                            SimpleDateFormat f = new SimpleDateFormat("HH");
                            String time = f.format(new Date());
                            int itime = Integer.parseInt(time);
                            if ((itime > 7) && (itime < 19)) {
                                mjVehicle.setCljg("1");
                                mjVehicle.setCljg("通过");
                            } else {
                                mjVehicle.setCljg("3");
                                mjVehicle.setCljg("初次接收");
                            }
                            xmlWriteResult.setCodeMessage("1", "处理成功");
//                            if (!xszazp.equals("")) {
                                List<String> bindResult = new ArrayList<String>();
                                bindResult = ValidateService.valid(mjVehicle);
                                if (bindResult.size() > 0) {
                                    xmlWriteResult.setCodeMessage("0", bindResult.toString());
                                } else {
                                    List<MjVehicle> mjVehicleList = publicService.selectObjs("mj_vehicle", "", "cphm,cpys", mjVehicle);
                                    if (mjVehicleList.size() > 1) {
                                        publicService.update("delete from mj_vehicle where cphm='" + mjVehicle.getCphm() + "' and cpys='" + mjVehicle.getCpys() + "'");
                                        logger.debug("多条信息删掉后插入");
                                        publicService.insert("mj_vehicle", "", "", mjVehicle);
                                    } else if (mjVehicleList.size() == 0) {
                                        publicService.insert("mj_vehicle", "", "cphm,cpys", mjVehicle);
                                    }
                                    xmlWriteResult.setCodeMessage("1", "处理成功");
                                }
//                            } else {
//                                xmlWriteResult.setCodeMessage("0", "行驶证照片不能为空");
//                            }
                        }
                    }
                    //logger.debug("接收车辆信息:" + mjVehicle.getCphm() + "完毕");
                } catch (Exception e) {
                    xmlWriteResult.setCodeMessage("0", e.getMessage());
                }
            } else if (jkid.equals("01W03")) {
                xmlWriteResult.setCodeMessage("1", "处理成功");
            } else if (jkid.equals("01W04")) {
                //logger.info("重置车辆信息状态:" + qybh);
                if (qybh != null && (!qybh.equals(""))) {
                    publicService.update("update mj_vehicle set sczt='0',scjg='' where qybh='" + qybh + "'");
                    xmlWriteResult.setCodeMessage("1", "重置同步状态成功");
                    //logger.info("重置同步状态成功:" + qybh);
                } else {
                    xmlWriteResult.setCodeMessage("0", "企业编号不能为空");
                }
            } else if (jkid.equals("01W05")) {
                //logger.info("接收到视频记录:" + qybh);
                MjDataVideo mjDataVideo = JaxbUtil.converToJavaBean(xmlVehcrpara, MjDataVideo.class);
                if (mjDataVideo!=null){
                    String id=mjDataVideo.getId();
                    String baseid=mjDataVideo.getBaseid();
                    String lxbh=mjDataVideo.getLxbh();
                    mjDataVideo.setCjsj(new Date());
                    MjDataVideo mjDataVideo1=publicService.selectObj("mj_data_video","","baseid,lxbh",mjDataVideo);
                    if(mjDataVideo1==null){
                        publicService.insert("mj_data_video","","id",mjDataVideo);
                        xmlWriteResult.setCodeMessage("1", "处理成功");
                    }
                    else{
                        xmlWriteResult.setCodeMessage("1", "已有此记录");
                    }
                }
            }else if (jkid.equals("01W06")) {
                MjOnlineDz mjOnlineDz = JaxbUtil.converToJavaBean(xmlVehcrpara, MjOnlineDz.class);
                if (mjOnlineDz!=null){
                    publicService.update("update mj_online_dz set isonline='"+mjOnlineDz.getIsonline()+"',onlinetime=getdate() where organ='"+mjOnlineDz.getOrgan()+"' and dzbh='"+mjOnlineDz.getDzbh()+"'");
                    publicService.update("update mj_online_qy set isonline='1',onlinetime=getdate() where organ='"+mjOnlineDz.getOrgan()+"'");
                    xmlWriteResult.setCodeMessage("1", "处理成功");
                }
            }
        }
        return xmlWriteResult;
    }

    public String savePic(String base64string, String zpqz, String sdate, String baseid, String zplb, String qybh) {
        String save_filename = "";
        String cclj = sysOptionService.getConfigValue("系统设置", "照片存储路径");
        String syear = sdate.substring(0, 4);
        String smonth = sdate.substring(5, 7);
        String sday = sdate.substring(8, 10);
        String path = "";
        if (!zplb.equals("")) {
            path = cclj + "/" + zplb + "/" + syear + "/" + smonth + "/" + sday + "/" + qybh;
            save_filename = "/" + zplb + "/" + syear + "/" + smonth + "/" + sday + "/" + qybh;
        } else {
            path = cclj + "/" + syear + "/" + smonth + "/" + sday + "/" + qybh;
            save_filename = "/" + syear + "/" + smonth + "/" + sday + "/" + qybh;
        }
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String suid = UUID.randomUUID().toString().replace("-", "");
        String filename = path + "/" + baseid + "_" + zpqz + ".jpg";
        save_filename = save_filename + "/" + baseid + "_" + zpqz + ".jpg";
        FileOutputStream fos = null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = null;
        try {
            if (base64string == null || base64string.equals("")) {
                return "";
            } else {
                bytes = decoder.decodeBuffer(base64string);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            return "";
        }
        try {
            fos = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        try {
            fos.write(bytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        // fos.flush();//这个清空内存，但不释放本地照片，造成照片不可读
        try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

        return save_filename;
    }

}
