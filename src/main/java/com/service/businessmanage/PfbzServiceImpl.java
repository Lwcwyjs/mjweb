package com.service.businessmanage;

import com.alibaba.fastjson.JSONObject;
import com.model.vehmanage.MjVehicle;
import com.model.vehmanage.TVehicles;
import com.model.vehmanage.Vehicle;
import com.service.base.PublicService;
import com.service.configmanage.SysOptionService;
import com.util.ConverUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PfbzServiceImpl implements PfbzService {

    private final Logger logger = LoggerFactory.getLogger(PfbzServiceImpl.class);
    @Autowired
    private PublicService publicService;
    @Autowired
    private SysOptionService sysOptionService;

    @Override
    /**
     * 获取排放标准-目录库
     * 20190424，因为云登录无法获取登录用户，合肥特殊的需求要用编号区分，参数添加检测站编号
     * 20191206，吉林省长春市-按照注册登记日期来判定排放标准
     * 20200408，广东省珠海市-按照注册登记日期来判定排放标准
     * @return
     */
    public MjVehicle getPfbz(MjVehicle mjVehicle) {
        String pfbz = "";
        String pfpdyj = "";
        String pfpdyjzl = "";
        String clsbdh = mjVehicle.getClsbdh();

        TVehicles tvehicle = new TVehicles();
        tvehicle.setClxh(mjVehicle.getClppxh());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ccrq = sdf.format(mjVehicle.getCcdjrq());
        String datanow=sdf.format(new Date());

        String cphm = mjVehicle.getCphm();
        if (cphm.length() > 7) {
            mjVehicle.setRlzl("C");
            pfbz = "D";
            mjVehicle.setPfbz("D");
            pfpdyjzl = "3";
            pfpdyj = "新能源修正";
        } else {

            SimpleDateFormat f = new SimpleDateFormat("HH");
            Date now = new Date();
            String time = f.format(now);
            int itime = Integer.parseInt(time);
//            if ((itime > 7) && (itime < 20)) {
//                logger.debug("调取随车清单接口");
//                String srt = getScqd(clsbdh);
//                logger.debug("随车清单接口返回:" + srt);
//                XmlHelper xmlHelper = new XmlHelper();
//                xmlHelper.loadFromStr(srt);
//                String status = xmlHelper.getInnerText("/vehicles", "result");
//                if (status.equals("true")) {
//                    pfbz = xmlHelper.getInnerText("/vehicles/vehicle", "pf");
//                    String fdjh = xmlHelper.getInnerText("/vehicles/vehicle", "fdjh");
//                    String clppxh = xmlHelper.getInnerText("/vehicles/vehicle", "clxh");
//                    if((mjVehicle.getFdjh()==null)||(mjVehicle.getFdjh().equals(""))){
//                        mjVehicle.setFdjh(fdjh);
//                    }
//                    if((mjVehicle.getClppxh()==null)||(mjVehicle.getClppxh().equals(""))){
//                        mjVehicle.setClppxh(clppxh);
//                    }
//                    pfpdyjzl = "3";
//                    pfpdyj = "随车清单调取";
//                }
//            }

            if (pfbz.equals("")) {
                String clxh = mjVehicle.getClppxh();
                clxh = clxh.replaceAll("[\\u4e00-\\u9fa5]", "");
                if (ccrq.compareTo(datanow)<=0) {
                    String sql="select top 1 * from t_vehicles where clxh='" + clxh + "' and isnull(cast(filename as date),'')<='" + ccrq + "' order by filename desc,pf asc";
                    logger.debug(sql);
                    List<TVehicles> tvehiclelist = publicService.selectObjs(sql, TVehicles.class);
                    if (tvehiclelist.size() > 0) {
                        pfbz = tvehiclelist.get(0).getPf();
                        pfpdyj = "车辆型号为：" + clxh + "," + ccrq + "注册的车辆。查询车型库，自动匹配ID为" +
                                tvehiclelist.get(0).getId() + ",排放为" + tvehiclelist.get(0).getPf() + ",审批日期为" + tvehiclelist.get(0).getFilename();
                        logger.debug(pfpdyj);
                        pfpdyjzl = "2";
                    } else {
                        logger.debug("车型库无对应记录");
                    }

                    if (pfbz.equals("")) {
                        logger.debug("查询vehicle");
                        Vehicle vehicle = new Vehicle();
                        vehicle.setClsbdh(mjVehicle.getClsbdh());
                        vehicle = publicService.selectObj("select * from vehicle where clsbdh='" + clsbdh + "' and  pfbzid is not null", Vehicle.class);
                        if (vehicle != null) {
                            logger.debug("vehicle有对应记录:" + vehicle.getPfbz());
                            pfbz = vehicle.getPfbz();
                            mjVehicle.setPfbz(vehicle.getPfbz());
                            mjVehicle.setHdzzl(vehicle.getHdzzl());
                            mjVehicle.setRlzl(vehicle.getRlzlid());
                            mjVehicle.setPfpdyj("查询数据库车辆信息表获取");
                            mjVehicle.setPfpdyjzl("0");
                        } else {
                            logger.debug("vehicle无对应记录");

                            logger.debug("查询车辆信息数据库");
                            Vehicle vehicle1 = publicService.selectObj("select top 1 * from vehicle where clxh='" + clxh + "' and ccrq<='" + ccrq + "' and pfbzid is not null order by ccrq desc,pfbzid asc", Vehicle.class);
                            if (vehicle1 != null) {
                                pfbz = vehicle1.getPfbzid();
                                pfpdyjzl = "1";
                                pfpdyj = "匹配同样车型的车辆记录id为" + vehicle1.getVehiclebh() + " 车牌号为" + vehicle1.getHphm();
                            } else {
                                logger.debug("查询车型库");

                            }
                        }
                    }
                }
            }
        }
        ConverUtil converUtil = new ConverUtil();
        pfbz = converUtil.pfhztopfsz(pfbz);
        mjVehicle.setPfbz(pfbz);
        mjVehicle.setPfpdyjzl(pfpdyjzl);
        mjVehicle.setPfpdyj(pfpdyj);
        logger.debug("判定完毕:" + pfbz);
        return mjVehicle;
    }

    @Override
    public String getScqd(String clsbdh) {
        logger.debug("获取随车清单");
        String srt = "{\"body\":[{\"result\":false,\"message\":\"查询失败\"}],\"code\":\"200\",\"message\":\"（成功）服务器已成功处理了请求。\",\"rows\":1}";
        String urlStr = "http://81.70.45.19:18090/tVecc/accessControl/get_data"; // 替换为你的URL
        String username = "tvehiclemlistuser";
        String pwd = "4BVcV8nv89stzXCh";
        JSONObject dataJson = new JSONObject();
        dataJson.put("username", username);
        dataJson.put("password", pwd);
        dataJson.put("reqtype","getVehiclemountedList");
        dataJson.put("vin", clsbdh);

        String jsonString = dataJson.toJSONString();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("User-Agent", "PostmanRuntime/7.37.3");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // 设置请求的超时时间（毫秒）
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // 执行请求并获取响应码
            int responseCode = connection.getResponseCode();

            // 检查响应码是否为200（成功）
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应内容
                InputStream inputStreamdata = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStreamdata = new ByteArrayOutputStream();
                byte[] bufferdata = new byte[4096]; // 你可以根据需要调整缓冲区的大小
                int bytesRead;
                // 读取数据到缓冲区，然后写入ByteArrayOutputStream
                while ((bytesRead = inputStreamdata.read(bufferdata)) != -1) {
                    byteArrayOutputStreamdata.write(bufferdata, 0, bytesRead);
                }
                // 从ByteArrayOutputStream获取byte[]
                byte[] byteArraydata = byteArrayOutputStreamdata.toByteArray();
                srt = new String(byteArraydata, "UTF-8");
                logger.debug("返回:" + srt);

            } else {
                logger.debug("调用随车清单接口失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return srt;
    }

}
