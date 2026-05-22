//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.task;

import com.commons.annotation.support.ValidateService;
import com.commons.utils.StringEscapeEditor;
import com.model.businessmange.MjDataBase;
import com.model.vehmanage.MjVehicle;
import com.service.base.PublicService;
import com.service.businessmanage.PfbzService;
import com.service.configmanage.SysOptionService;
import com.service.configmanage.SysOptionsService;
import com.util.ConverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component("ClbjTask")
public class ClbjTask {
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

    private final Logger logger = LoggerFactory.getLogger(ClbjTask.class);
    static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String DATE_FORMAT = "yyyy-MM-dd";
    private DateFormat format;
    private DateFormat formatDate;
    @Autowired
    private PublicService publicService;
    @Autowired
    private SysOptionsService sysOptionsService;
    @Autowired
    private PfbzService pfbzService;
    @Autowired
    public SysOptionService sysOptionService;


    public void ClbjTask() {
        SimpleDateFormat f = new SimpleDateFormat("HH");
        Date now = new Date();
        String time = f.format(now);
        int itime = Integer.parseInt(time);
        ConverUtil converUtil = new ConverUtil();
        if ((itime > 7) && (itime < 19)) {
            try {
                List<MjVehicle> mjVehicles = publicService.selectObjs("select top 100 * from mj_vehicle where (isnull(clbj,'0')='0' or clbj='3') and sjly is null", MjVehicle.class);
                if(mjVehicles.size()==0) {
                    mjVehicles = publicService.selectObjs("select top 100 * from mj_vehicle where (isnull(clbj,'0')='0' or clbj='3')", MjVehicle.class);
                }
                if(mjVehicles.size()>0){
                    for (MjVehicle mjVehicle : mjVehicles) {
                        if ((itime > 7) && (itime < 19)) {
                            String sresult = "";
                            logger.debug("开始过滤:" + mjVehicle.getCphm());
                            String pfbz = mjVehicle.getPfbz();
                            String pfpdyjzl = mjVehicle.getPfpdyjzl();
                            String pfpdyj = mjVehicle.getPfpdyj();
                            if (mjVehicle.getClsbdh() == null || mjVehicle.getClsbdh().equals("")) {
                                sresult = "车架号为空";
                            }
                            if (mjVehicle.getCcdjrq() == null) {
                                if (sresult.equals("")) {
                                    sresult = "初次登记日期为空";
                                } else {
                                    sresult = sresult + ",初次登记日期为空";
                                }
                            }
                            if (mjVehicle.getClppxh() == null || mjVehicle.getClppxh().equals("")) {
                                if (sresult.equals("")) {
                                    sresult = "车辆品牌型号为空";
                                } else {
                                    sresult = sresult + ",车辆品牌型号为空";
                                }
                            }

                            if (mjVehicle.getXszazp() == null || mjVehicle.getXszazp().equals("")) {
                                if (sresult.equals("")) {
                                    sresult = "行驶证照片为空";
                                } else {
                                    sresult = sresult + ",行驶证照片为空";
                                }
                            }
                            if (sresult.equals("")) {
                                mjVehicle = pfbzService.getPfbz(mjVehicle);
                                if (mjVehicle.getPfpdyjzl() != null) {
                                    if (mjVehicle.getPfpdyjzl().equals("2") || mjVehicle.getPfpdyjzl().equals("3")) {

                                    } else {
                                        mjVehicle.setPfbz(pfbz);
                                        mjVehicle.setPfpdyjzl(pfpdyjzl);
                                        mjVehicle.setPfpdyj(pfpdyj);
                                    }
                                } else {
                                    mjVehicle.setPfbz(pfbz);
                                    mjVehicle.setPfpdyjzl(pfpdyjzl);
                                    mjVehicle.setPfpdyj(pfpdyj);
                                }
                                mjVehicle.setRlzl(converUtil.convertrlzl(converUtil.convertrlzlex(mjVehicle.getRlzl())));
                                String syxz = mjVehicle.getSyxz();
                                String cllx = mjVehicle.getCllx();
                                if (syxz == null || syxz.equals("")) {
                                    if (cllx != null && !cllx.equals("")) {
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
                                    if (cllx != null && !cllx.equals("")) {
                                        if (cllx.indexOf("K") >= 0) {
                                            clzl = "4";
                                        } else if (cllx.indexOf("Z") >= 0) {
                                            clzl = "2";
                                        } else {
                                            clzl = "0";
                                        }
                                    }
                                    if (syxz != null && !syxz.equals("")) {
                                        if (syxz.equals("R")) {
                                            clzl = "1";
                                        }
                                    }
                                    mjVehicle.setClzl(clzl);
                                }
                                String lwzt = mjVehicle.getLwzt();
                                if (lwzt == null || lwzt.equals("")) {
                                    mjVehicle.setLwzt("0");
                                }
                                String syr = mjVehicle.getSyr();
                                if (syr == null || syr.equals("")) {
                                    mjVehicle.setSyr("-");
                                }

                                String fdjh = mjVehicle.getFdjh();
                                if (fdjh == null || fdjh.equals("")) {
                                    mjVehicle.setFdjh("-");
                                }
                                String sdate=new SimpleDateFormat("yyyy-MM-dd").format(mjVehicle.getCjsj());
                                String vehiclezp = mjVehicle.getVehiclezp();
                                String id=mjVehicle.getId();
                                if (vehiclezp == null || vehiclezp.equals("")) {
                                    MjDataBase mjDataBase = publicService.selectObj("select top 1 * from mj_data_base where cphm='" + mjVehicle.getCphm() + "' and cpys='" + mjVehicle.getCpys() + "' and ctzp is not null", MjDataBase.class);
                                    if (mjDataBase != null) {
                                        mjVehicle.setVehiclezp(mjDataBase.getCtzp());
                                    }
                                }
                                else{
                                    if(vehiclezp.indexOf("http")>=0){
                                        logger.debug("aaa");
                                        vehiclezp=downloadImage(vehiclezp,"vehicle",sdate,mjVehicle.getId(),"vehicle");
                                        logger.debug("bbb");
                                        mjVehicle.setVehiclezp(vehiclezp);
                                    }
                                }
                                logger.debug("444");
                                String scqdzp = mjVehicle.getScqdzp();
                                if (scqdzp != null &&!scqdzp.equals("")) {
                                    if(scqdzp.indexOf("http")>=0){
                                        scqdzp=downloadImage(scqdzp,"scqd",sdate,mjVehicle.getId(),"scqd");
                                        mjVehicle.setScqdzp(scqdzp);
                                    }
                                }
                                logger.debug("55");
                                String xszazp = mjVehicle.getXszazp();
                                if (xszazp != null &&!xszazp.equals("")) {
                                    if(xszazp.indexOf("http")>=0){
                                        xszazp=downloadImage(xszazp,"xsza",sdate,mjVehicle.getId(),"xsz");
                                        mjVehicle.setXszazp(xszazp);
                                    }
                                }
                                logger.debug("666");
                                String xszbzp = mjVehicle.getXszbzp();
                                if (xszbzp != null &&!xszbzp.equals("")) {
                                    if(xszbzp.indexOf("http")>=0){
                                        xszbzp=downloadImage(xszbzp,"xszb",sdate,mjVehicle.getId(),"xsz");
                                        mjVehicle.setXszbzp(xszbzp);
                                    }
                                }
                                logger.debug("777");
                                List<String> bindResult = new ArrayList<String>();
                                bindResult = ValidateService.valid(mjVehicle);
                                if (bindResult.size() > 0) {
                                    sresult = bindResult.toString();
                                }
                            }
                            if (sresult.equals("")) {
                                mjVehicle.setCljg("通过");
                                mjVehicle.setClbj("1");
                            } else {
                                mjVehicle.setCljg(sresult);
                                mjVehicle.setClbj("2");
                            }
                            mjVehicle.setScjg("");
                            mjVehicle.setSczt("0");
                            List<MjVehicle> mjVehicleList = publicService.selectObjs("mj_vehicle", "", "cphm,cpys", mjVehicle);
                            if (mjVehicleList.size() > 1) {
                                publicService.update("delete from mj_vehicle where cphm='" + mjVehicle.getCphm() + "' and cpys='" + mjVehicle.getCpys() + "'");
                                logger.debug("多条信息删掉后插入");
                                publicService.insert("mj_vehicle", "", "", mjVehicle);
                            } else {
                                publicService.update("mj_vehicle", "id", "id", mjVehicle);
                            }
                            logger.debug("过滤完毕");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("过滤车辆信息失败:" + e.getMessage());
            }
        }
    }
    public String  downloadImage(String imageUrl,String zpqz,String sdate,String baseid,String zplb) {
        String save_filename="";
        String cclj=sysOptionService.getConfigValue("系统设置", "照片存储路径");
        String syear=sdate.substring(0,4);
        String smonth=sdate.substring(5,7);
        String sday=sdate.substring(8,10);
        String path="";
        if(!zplb.equals("")){
            path = cclj+ "/" + zplb + "/" + syear + "/" + smonth + "/" + sday;
            save_filename = "/" + zplb+"/" + syear + "/" + smonth + "/" + sday;
        }
        else {
            path = cclj + "/" + syear + "/" + smonth + "/" + sday;
            save_filename = "/" + syear + "/" + smonth + "/" + sday;
        }
        File directory  = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String suid= UUID.randomUUID().toString().replace("-","");
        String filename = path + "/"+baseid+"_"+zpqz+".jpg";
        save_filename=save_filename+"/"+baseid+"_"+zpqz+".jpg";
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            // 检查响应码
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 获取输入流
                InputStream inputStream = httpURLConnection.getInputStream();
                // 创建文件输出流
                FileOutputStream fileOutputStream = new FileOutputStream(filename);

                // 创建一个缓冲区
                byte[] buffer = new byte[4096];
                int bytesRead;

                // 读取数据并写入文件
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                // 关闭流
                fileOutputStream.close();
                inputStream.close();

                System.out.println("图片下载成功：" + filename);
            } else {
                System.out.println("图片下载失败，HTTP错误码：" + responseCode);
                save_filename="";
            }

            httpURLConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  save_filename;
    }
}
