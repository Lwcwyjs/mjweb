package com.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.commons.base.BaseController;
import com.model.businessmange.MjDataBase;
import com.model.local.YuYue;
import com.service.base.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class apiController extends BaseController {


    // 约定的固定token
    private static final String VALID_TOKEN = "C5E770D6EBD8564E5487719EB273349A";
    @Autowired
    private PublicService publicService;

    /**
     * 预约记录新增接口（不使用拦截器，直接在方法内验证token）
     */
    @RequestMapping(value = "/addVisitorInfo", method = RequestMethod.POST)
    public String addVisitorInfo(
            // 注入HttpServletRequest用于获取Header
            HttpServletRequest request,
            // 接收Form表单参数
            @RequestParam("dataId") String dataId,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("licensePlateColor") String licensePlateColor,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("geteCode") String geteCode) {

        JSONObject dataJson = new JSONObject();
        dataJson.put("code", "1");
        dataJson.put("msg", "成功");

        // 1. 验证Header中的token（核心步骤：替代拦截器的逻辑）
        String token = request.getHeader("token");
        if (token == null || !token.equals(VALID_TOKEN)) {
            dataJson.put("code", "401");
            dataJson.put("msg", "token无效或缺失");
            return dataJson.toJSONString();
        }

        // 2. 参数校验
        try {
            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                dataJson.put("code", "400");
                dataJson.put("msg", "车牌号码不能为空");
                return dataJson.toJSONString();
            }

            if (beginTime == null || endTime == null || beginTime.trim().isEmpty() || endTime.trim().isEmpty()) {
                dataJson.put("code", "400");
                dataJson.put("msg", "开始时间和结束时间不能为空");
                return dataJson.toJSONString();
            }

            YuYue yuYue=new YuYue();
            yuYue.setOrgan("5869040498");
            yuYue.setCjsj(new Date());
            yuYue.setDataid(dataId);
            yuYue.setLicenseplate(licensePlate);
            yuYue.setLicenseplatecolor(licensePlateColor);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dbeginTime = sdf.parse(beginTime);
            Date dendTime = sdf.parse(endTime);
            yuYue.setBegintime(dbeginTime);
            yuYue.setEndtime(dendTime);
            yuYue.setGetecode(geteCode);
            YuYue yuYue1=publicService.selectObj("yuyue","","licenseplate,begintime,endtime",yuYue);
            if(yuYue1!=null){
                publicService.update("yuyue","","licenseplate,begintime,endtime",yuYue);
            }
            else{
                publicService.insert("yuyue","","",yuYue);
            }
            // 4. 处理成功
            dataJson.put("code", "200");
            dataJson.put("msg", "操作成功");

        } catch (Exception e) {
            dataJson.put("code", "500");
            dataJson.put("msg", "服务器错误：" + e.getMessage());
        }
        logger.info("返回:"+dataJson.toJSONString());
        return dataJson.toJSONString();
    }
    /**
     * 预约记录新增接口（不使用拦截器，直接在方法内验证token）
     */
    @RequestMapping(value = "/visitorHistoryInfo", method = RequestMethod.POST)
    public String visitorHistoryInfo(
            // 注入HttpServletRequest用于获取Header
            HttpServletRequest request,
            // 接收Form表单参数
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("licensePlateColor") String licensePlateColor,
            @RequestParam("beginTime") String beginTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("geteCode") String geteCode) {

        JSONObject dataJson = new JSONObject();
        dataJson.put("code", "1");
        dataJson.put("msg", "成功");
        dataJson.put("data", "");

        // 1. 验证Header中的token（核心步骤：替代拦截器的逻辑）
        String token = request.getHeader("token");
        if (token == null || !token.equals(VALID_TOKEN)) {
            dataJson.put("code", "401");
            dataJson.put("msg", "token无效或缺失");
            return dataJson.toJSONString();
        }

        // 2. 参数校验
        try {
            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                dataJson.put("code", "400");
                dataJson.put("msg", "车牌号码不能为空");
                return dataJson.toJSONString();
            }
            if (beginTime == null || endTime == null || beginTime.trim().isEmpty() || endTime.trim().isEmpty()) {
                dataJson.put("code", "400");
                dataJson.put("msg", "开始时间和结束时间不能为空");
                return dataJson.toJSONString();
            }

            // 3. 执行业务逻辑（示例：打印参数，实际可保存到数据库）
            logger.info("查询历史数据：");
            logger.info("车牌: " + licensePlate + "（颜色：" + licensePlateColor + "）");
            logger.info("时间范围: " + beginTime + " 至 " + endTime);
            logger.info("出入口: " + geteCode);
            String organ="5869040498";
            List< MjDataBase> mjDataBaseList=publicService.selectObjs("select * from mj_data_base where qybh='"+organ+"' and "+
                    "cpys='"+licensePlateColor+"' and tgkssj>='"+beginTime+"' and tgkssj<='"+endTime+"'",MjDataBase.class);
            if(mjDataBaseList.size()>0){
                JSONArray dataArray = new JSONArray();
                for (MjDataBase mjData : mjDataBaseList) {
                    // 每个实体对应一个JSONObject
                    JSONObject dataObj = new JSONObject();
                    // 映射实体属性到JSON字段（根据实际实体类的getter方法填写）
                    dataObj.put("id", mjData.getId()); // 假设实体有getId()方法
                    dataObj.put("licensePlate", mjData.getCphm()); // 车牌号码
                    dataObj.put("licensePlateColor", mjData.getCpys()); // 车牌颜色
                    dataObj.put("passTime", mjData.getTgkssj()); // 假设通行时间对应实体的tgkssj字段（getTgkssj()）
                    dataObj.put("geteCode", mjData.getDzbh()); // 出入口编号
                    String bgzt=mjData.getBgzt();
                    if(bgzt.equals("1")){
                        bgzt="2";
                    }
                    else{
                        bgzt="1";
                    }
                    dataObj.put("status", bgzt); // 状态（根据业务逻辑填写，也可从实体获取）
                    dataObj.put("msg", mjData.getGkjg()); // 描述（根据业务逻辑填写）

                    // 添加到数组
                    dataArray.add(dataObj);
                }
                dataJson.put("data", dataArray);
            }


            // 4. 处理成功
            dataJson.put("code", "200");
            dataJson.put("msg", "操作成功");

        } catch (Exception e) {
            dataJson.put("code", "500");
            dataJson.put("msg", "服务器错误：" + e.getMessage());
        }
        logger.info("返回:"+dataJson.toJSONString());
        return dataJson.toJSONString();
    }

}