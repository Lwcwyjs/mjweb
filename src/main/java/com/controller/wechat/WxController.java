package com.controller.wechat;

import com.commons.base.BaseController;
import com.commons.utils.PageInfo;
import com.model.configmange.SysCode;
import com.model.vehmanage.MjAiPhoto;
import com.model.vehmanage.MjVehicle;
import com.service.base.MidPublicService;
import com.service.base.PublicService;
import com.service.businessmanage.PfbzService;
import com.service.configmanage.SysOptionService;
import com.util.ConverUtil;
import com.util.VinWebAutoUtil;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;


@Controller
@RequestMapping("/wechat")
public class WxController extends BaseController {
    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SysOptionService sysOptionService;
    @Autowired
    private PublicService publicService;
    @Autowired
    private MidPublicService midPublicService;
    @Autowired
    private PfbzService pfbzService;

    private final SimpleDateFormat shortDateFormater = new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int zpsize = 500 * 1024;
    private static final int VECC_RENDER_WIDTH = 1600;
    private static final int VECC_RENDER_SCALE = 2;
    private static final int VECC_IMAGE_TARGET_SIZE = 900 * 1024;
    private static final int VECC_IMAGE_MAX_WIDTH = 3000;
    private static final int VECC_IMAGE_MAX_HEIGHT = 3000;


    @RequestMapping("/vehReg")
    public String vehReg(HttpServletRequest request, String code, HttpServletResponse response) {
        return "/wechat/vehRegNew";
    }

    @RequestMapping("/vehRegNew")
    public String vehRegNew(HttpServletRequest request, String code, HttpServletResponse response) {
        return "/wechat/vehReg";
    }


    /**
     * 初始化并获取验证码
     */
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> init(HttpServletRequest request) {
        Map<String, Object> rtMap = new HashMap<>();
        // 使用SessionID区分不同用户
        String sessionId = request.getSession().getId();
        VinWebAutoUtil vinWebAutoUtil = new VinWebAutoUtil();
        String captchaBase64 = vinWebAutoUtil.initAndGetCaptcha(sessionId);

        if (!captchaBase64.isEmpty()) {
            rtMap.put("code", 200);
            rtMap.put("captcha", captchaBase64);
        } else {
            rtMap.put("code", 500);
            rtMap.put("msg", "获取验证码失败");
        }
        return rtMap;
    }

    /**
     * 同步车架号、发动机号到目标网站
     */
    @RequestMapping(value = "/syncParams", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> syncParams(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Map<String, Object> rtMap = new HashMap<>();
        rtMap.put("code", 500);
        rtMap.put("msg", "接口已升级，请使用/vecc/scqd");
        return rtMap;
    }

    /**
     * 提交查询
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submit(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        String captcha = params.get("captcha");
        VinWebAutoUtil vinWebAutoUtil = new VinWebAutoUtil();
        Map<String, Object> rtMap = new HashMap<>();
        rtMap.put("code", 500);
        rtMap.put("msg", "接口已升级，请使用/vecc/scqd");
        return rtMap;
    }

    /**
     * 刷新验证码
     */
    @RequestMapping(value = "/refreshCaptcha", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> refreshCaptcha(HttpServletRequest request) {
        Map<String, Object> rtMap = new HashMap<>();
        String sessionId = request.getSession().getId();
        VinWebAutoUtil vinWebAutoUtil = new VinWebAutoUtil();
        String newCaptcha = vinWebAutoUtil.refreshCaptcha(sessionId);

        if (!newCaptcha.isEmpty()) {
            rtMap.put("code", 200);
            rtMap.put("captcha", newCaptcha);
        } else {
            rtMap.put("code", 500);
            rtMap.put("msg", "获取验证码失败");
        }
        return rtMap;
    }

    @RequestMapping(value = "/vecc/init", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> veccInit(HttpServletRequest request) {
        Map<String, Object> rtMap = new HashMap<>();
        if (!isVeccServiceAvailable()) {
            rtMap.put("code", 0);
            rtMap.put("message", "晚8点至早7点期间不提供数据查询服务");
            return rtMap;
        }
        String sessionId = request.getSession().getId();
        VinWebAutoUtil vinWebAutoUtil = new VinWebAutoUtil();
        String captchaBase64 = vinWebAutoUtil.initAndGetCaptcha(sessionId);
        if (captchaBase64 != null && !captchaBase64.isEmpty()) {
            rtMap.put("code", 1);
            rtMap.put("captcha", captchaBase64);
            return rtMap;
        }
        rtMap.put("code", 0);
        rtMap.put("message", "获取验证码失败");
        return rtMap;
    }

    @RequestMapping(value = "/vecc/refresh", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> veccRefresh(HttpServletRequest request) {
        Map<String, Object> rtMap = new HashMap<>();
        if (!isVeccServiceAvailable()) {
            rtMap.put("code", 0);
            rtMap.put("message", "晚8点至早7点期间不提供数据查询服务");
            return rtMap;
        }
        String sessionId = request.getSession().getId();
        VinWebAutoUtil vinWebAutoUtil = new VinWebAutoUtil();
        String captchaBase64 = vinWebAutoUtil.refreshCaptcha(sessionId);
        if (captchaBase64 != null && !captchaBase64.isEmpty()) {
            rtMap.put("code", 1);
            rtMap.put("captcha", captchaBase64);
            return rtMap;
        }
        rtMap.put("code", 0);
        rtMap.put("message", "获取验证码失败");
        return rtMap;
    }

    @RequestMapping(value = "/vecc/scqd", method = RequestMethod.POST)
    @ResponseBody
    public String veccScqd(@RequestBody Map<String, String> params, HttpServletRequest request) {
        if (!isVeccServiceAvailable()) {
            return "{\"code\":0,\"message\":\"晚8点至早7点期间不提供数据查询服务\"}";
        }
        String sessionId = request.getSession().getId();
        String vin = params.get("vin");
        if (StringUtils.isBlank(vin)) {
            vin = params.get("vin6");
        }
        String engine6 = params.get("engine6");
        String captcha = params.get("captcha");
        String fdjh = params.get("fdjh");
        String clsbdh = params.get("clsbdh");
        String clppxh = params.get("clppxh");

        if (StringUtils.isBlank(vin) || vin.length() != 17) {
            return "{\"code\":0,\"message\":\"车架号不正确\"}";
        }
        if (StringUtils.isBlank(engine6) || engine6.length() != 6) {
            return "{\"code\":0,\"message\":\"发动机号后6位不正确\"}";
        }
        if (StringUtils.isBlank(captcha)) {
            return "{\"code\":0,\"message\":\"验证码不能为空\"}";
        }

        VinWebAutoUtil vinWebAutoUtil = new VinWebAutoUtil();
        VinWebAutoUtil.QueryResult queryResult = vinWebAutoUtil.queryHttp(sessionId, vin, engine6, captcha);
        if (queryResult == null) {
            return "{\"code\":0,\"message\":\"查询失败\"}";
        }
        if (queryResult.code != 200) {
            String msg = queryResult.msg == null ? "查询失败" : queryResult.msg.replace("\"", "\\\"");
            if (queryResult.captchaBase64 != null && !queryResult.captchaBase64.isEmpty()) {
                return "{\"code\":0,\"message\":\"" + msg + "\",\"captcha\":\"" + queryResult.captchaBase64 + "\"}";
            }
            return "{\"code\":0,\"message\":\"" + msg + "\"}";
        }

        try {
            byte[] screenshotPng = queryResult.screenshotPng;
            if (screenshotPng == null || screenshotPng.length == 0) {
                screenshotPng = renderHtmlToPng(queryResult.html, VECC_RENDER_WIDTH, VECC_RENDER_SCALE);
            }
            if ((screenshotPng == null || screenshotPng.length == 0) && queryResult.html != null && !queryResult.html.trim().equals("")) {
                screenshotPng = renderTextToPng(queryResult.html, VECC_RENDER_WIDTH, VECC_RENDER_SCALE);
            }
            if (screenshotPng == null || screenshotPng.length == 0) {
                return "{\"code\":0,\"message\":\"生成随车清单图片失败\"}";
            }

            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String zpdir = uploadDir + "/scqd/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            String remotefilename = "/scqd/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            String fullFileName = zpdir + "/" + filename;
            byte[] imagebytes = compressJpg(new ByteArrayInputStream(screenshotPng), VECC_IMAGE_TARGET_SIZE, VECC_IMAGE_MAX_WIDTH, VECC_IMAGE_MAX_HEIGHT);

            if (!upload(imagebytes, fullFileName)) {
                return "{\"code\":0,\"message\":\"随车清单图片保存失败\"}";
            }

            String fullUrl = zpurl + remotefilename;
            MjAiPhoto mjAiPhoto = new MjAiPhoto();
            String uid = UUID.randomUUID().toString().replace("-", "");
            mjAiPhoto.setId(uid);
            mjAiPhoto.setZpzl("scqd");
            mjAiPhoto.setZp(remotefilename);
            mjAiPhoto.setSbzt("0");
            mjAiPhoto.setSbjg("");
            mjAiPhoto.setCjsj(new Date());
            mjAiPhoto.setSbtype("2");
            midPublicService.insert("mj_ai_photo", "", "", mjAiPhoto);

            String sbz = "";
            boolean bsb = false;
            for (int i = 0; i < 30; i++) {
                MjAiPhoto mjAiPhoto1 = midPublicService.selectObj("mj_ai_photo", "", "id", mjAiPhoto);
                if (mjAiPhoto1 != null) {
                    if (!mjAiPhoto1.getSbzt().equals("0")) {
                        if (mjAiPhoto1.getSbzt().equals("1")) {
                            sbz = mjAiPhoto1.getSbjg();
                            bsb = true;
                            break;
                        } else {
                            sbz = "";
                            logger.debug("识别失败:" + mjAiPhoto1.getSbjg());
                        }
                    }
                }
                Thread.sleep(500);
            }

            String pfbz = "";
            String pfpdyjLocal = "";
            String pfpdyjzlLocal = "";
            if (sbz != null && !sbz.equals("")) {
                if (clppxh != null) {
                    clppxh = clppxh.replaceAll("[\\u4e00-\\u9fa5]", "");
                } else {
                    clppxh = "";
                }
                boolean matched = false;
                if (fdjh != null && !fdjh.equals("") && sbz.contains(fdjh)) {
                    matched = true;
                }
                if (!matched && clsbdh != null && !clsbdh.equals("") && sbz.contains(clsbdh)) {
                    matched = true;
                }
                if (!matched && !clppxh.equals("") && sbz.contains(clppxh)) {
                    matched = true;
                }
                if (matched) {
                    String spfbz = extractTextAroundKeyword(sbz, "排放阶段", 100);
                    String pfbzid = "";
                    if ((spfbz.contains("国零")) || (spfbz.contains("国0"))) {
                        pfbzid = "0";
                    } else if (spfbz.contains("国一")) {
                        pfbzid = "1";
                    } else if (spfbz.contains("国二")) {
                        pfbzid = "2";
                    } else if (spfbz.contains("国三")) {
                        pfbzid = "3";
                    } else if (spfbz.contains("国四")) {
                        pfbzid = "4";
                    } else if (spfbz.contains("国五")) {
                        pfbzid = "5";
                    } else if (spfbz.contains("国六")) {
                        pfbzid = "6";
                    } else if (spfbz.contains("电")) {
                        pfbzid = "D";
                    }
                    ConverUtil converUtil = new ConverUtil();
                    pfbz = converUtil.pfsztopfhz(pfbzid);
                    pfpdyjLocal = "随车清单网页查询获取";
                    pfpdyjzlLocal = "4";
                } else {
                    return "{\"code\":0,\"message\":\"随车清单非本车,请确认\",\"url\":\"" + fullUrl + "\"}";
                }
            } else {
                if (bsb) {
                    return "{\"code\":0,\"message\":\"非有效随车清单，请确认\",\"url\":\"" + fullUrl + "\"}";
                }
            }

            String safeUrl = fullUrl.replace("\"", "\\\"");
            String safePfbz = pfbz == null ? "" : pfbz.replace("\"", "\\\"");
            String safePfpdyj = pfpdyjLocal == null ? "" : pfpdyjLocal.replace("\"", "\\\"");
            String safePfpdyjzl = pfpdyjzlLocal == null ? "" : pfpdyjzlLocal.replace("\"", "\\\"");
            return "{\"code\":1,\"message\":\"成功\",\"url\":\"" + safeUrl + "\",\"pfbz\":\"" + safePfbz + "\",\"pfpdyj\":\"" + safePfpdyj + "\",\"pfbzyjzl\":\"" + safePfpdyjzl + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage() == null ? "处理失败" : e.getMessage().replace("\"", "\\\"");
            return "{\"code\":0,\"message\":\"" + msg + "\"}";
        }
    }

    private boolean isVeccServiceAvailable() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return !(hour >= 20 || hour < 7);
    }

    private static byte[] renderHtmlToPng(final String html, final int width, final int scale) throws Exception {
        if (html == null || html.trim().equals("")) {
            return null;
        }
        final byte[][] holder = new byte[1][];
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    JEditorPane pane = new JEditorPane();
                    pane.setContentType("text/html; charset=UTF-8");
                    pane.setEditable(false);
                    String wrapped = html;
                    if (html.indexOf("<base") < 0) {
                        wrapped = "<html><head><base href=\"https://info.vecc.org.cn/ve/\"></head><body>" + html + "</body></html>";
                    }
                    pane.setText(wrapped);
                    pane.setSize(new Dimension(width, Integer.MAX_VALUE));
                    Dimension pref = pane.getPreferredSize();
                    if (pref.height > 6000) {
                        pref = new Dimension(pref.width, 6000);
                    }
                    pane.setSize(pref);
                    int outW = pref.width * Math.max(1, scale);
                    int outH = pref.height * Math.max(1, scale);
                    BufferedImage image = new BufferedImage(outW, outH, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = image.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, outW, outH);
                    if (scale > 1) {
                        g2d.scale(scale, scale);
                    }
                    pane.paint(g2d);
                    g2d.dispose();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", baos);
                    holder[0] = baos.toByteArray();
                    baos.close();
                } catch (Exception e) {
                    holder[0] = null;
                }
            }
        });
        return holder[0];
    }

    private static byte[] renderTextToPng(String text, int width, int scale) {
        try {
            if (text == null) {
                return null;
            }
            String plain = text.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
            if (plain.equals("")) {
                plain = "随车清单查询结果";
            }
            int s = Math.max(1, scale);
            int fontSize = 18 * s;
            int lineChars = Math.max(20, width / fontSize);
            StringBuilder sb = new StringBuilder();
            int idx = 0;
            while (idx < plain.length()) {
                int end = Math.min(plain.length(), idx + lineChars);
                sb.append(plain.substring(idx, end)).append("\n");
                idx = end;
            }
            String[] lines = sb.toString().split("\\n");
            int lineHeight = 26 * s;
            int height = Math.max(200 * s, lines.length * lineHeight + 40 * s);
            int outW = width * s;
            BufferedImage img = new BufferedImage(outW, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, outW, height);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Microsoft YaHei", Font.PLAIN, fontSize));
            int y = 30 * s;
            for (String line : lines) {
                g.drawString(line, 20 * s, y);
                y += lineHeight;
                if (y > height - 10) {
                    break;
                }
            }
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/queryveh")
    @ResponseBody
    public String queryveh(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        ConverUtil converUtil = new ConverUtil();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "0");
        jsonObject.put("msg", "无此车信息，请录入");
        MjVehicle mjVehicle = new MjVehicle();
        try {
            MjVehicle mjVehicle1 = new MjVehicle();
            if (params.containsKey("cphm")) {
                mjVehicle1.setCphm(params.get("cphm").toString());
            }
            if (params.containsKey("cpys")) {
                mjVehicle1.setCpys(params.get("cpys").toString());
            }
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            mjVehicle = publicService.selectObj("mj_vehicle", "", "cphm,cpys", mjVehicle1);
            if (mjVehicle != null) {
                if (mjVehicle.getScqdzp() != null && !mjVehicle.getScqdzp().equals("")) {
                    mjVehicle.setScqdzp(zpurl + mjVehicle.getScqdzp());
                }
                if (mjVehicle.getXszazp() != null && !mjVehicle.getXszazp().equals("")) {
                    mjVehicle.setXszazp(zpurl + mjVehicle.getXszazp());
                }
                if (mjVehicle.getXszbzp() != null && !mjVehicle.getXszbzp().equals("")) {
                    mjVehicle.setXszbzp(zpurl + mjVehicle.getXszbzp());
                }
                if (mjVehicle.getVehiclezp() != null && !mjVehicle.getVehiclezp().equals("")) {
                    mjVehicle.setVehiclezp(zpurl + mjVehicle.getVehiclezp());
                }
                jsonObject.put("code", "1");
                jsonObject.put("msg", "有此车信息，无需录入");
            }

        } catch (Exception e) {
            jsonObject.put("code", "0");
            jsonObject.put("msg", e.getMessage());
        }
        request.setAttribute("mjVehicle", mjVehicle);
        return jsonObject.toString();
    }

    @RequestMapping("/updateveh")
    @ResponseBody
    public String updateveh(@RequestParam Map<String, Object> params) {
        ConverUtil converUtil = new ConverUtil();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String srt = "\"" + "提交成功" + "\"";
        logger.debug("提交车辆信息:" + params.get("cphm").toString());
        try {
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
            MjVehicle mjVehicle = new MjVehicle();
            if (params.containsKey("lxdh")) {
                mjVehicle.setLxdh(params.get("lxdh").toString());
            }
            if (params.containsKey("zjnjsj")) {
                Date dtZjnjsj = shortDateFormater.parse(params.get("zjnjsj").toString());
                mjVehicle.setZjnjsj(dtZjnjsj);
            }
            if (params.containsKey("cphm")) {
                mjVehicle.setCphm(params.get("cphm").toString());
            }
            if (params.containsKey("clsbdh")) {
                mjVehicle.setClsbdh(params.get("clsbdh").toString());
            }
            if (params.containsKey("fdjh")) {
                mjVehicle.setFdjh(params.get("fdjh").toString());
            }
            if (params.containsKey("ccdjrq")) {
                Date dtccdjrq = shortDateFormater.parse(params.get("ccdjrq").toString());
                mjVehicle.setCcdjrq(dtccdjrq);
            }
            if (params.containsKey("pfbz")) {
                mjVehicle.setPfbz(converUtil.pfhztopfsz(params.get("pfbz").toString()));
            }

            if (params.containsKey("cllx")) {
                String cllx = params.get("cllx").toString();
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
                        if (cllx.indexOf("轻型普通") >= 0) {
                            mjVehicle.setCllx("H31");
                        } else if (cllx.indexOf("中型普通") >= 0) {
                            mjVehicle.setCllx("H21");
                        } else if (cllx.indexOf("重型普通") >= 0) {
                            mjVehicle.setCllx("H11");
                        } else {
                            srt = "\"" + "无此车辆类型" + "\"";
                            return srt;
                        }
                    }
                }
            }
            if (params.containsKey("syxz")) {
                String syxz = params.get("syxz").toString();
                SysCode sysCode = new SysCode();
                sysCode.setOi_name("使用性质");
                sysCode.setOi_value(syxz);
                sysCode = publicService.selectObj("ai_sys_code", "", "oi_name,oi_value", sysCode);
                if (sysCode != null) {
                    mjVehicle.setSyxz(sysCode.getOi_code());
                } else {
                    sysCode = publicService.selectObj("select * from ai_sys_code where oi_name='使用性质' and oi_value like '%" + syxz + "%'", SysCode.class);
                    if (sysCode != null) {
                        mjVehicle.setSyxz(sysCode.getOi_code());
                    } else {
                        srt = "\"" + "无此使用性质" + "\"";
                        return srt;
                    }
                }
            }
            if (params.containsKey("clppxh")) {
                mjVehicle.setClppxh(params.get("clppxh").toString());
            }
            if (params.containsKey("syr")) {
                mjVehicle.setSyr(params.get("syr").toString());
            }
            if (params.containsKey("rlzl")) {
                mjVehicle.setRlzl(params.get("rlzl").toString());
            }
            if (params.containsKey("hdzzl")) {
                String hdzzl = params.get("hdzzl").toString();
                hdzzl = hdzzl.replaceAll("kg", "");
                mjVehicle.setHdzzl(hdzzl);
            }
            if (params.containsKey("zdzzl")) {
                String zdzzl = params.get("zdzzl").toString();
                zdzzl = zdzzl.replaceAll("kg", "");
                mjVehicle.setZdzzl(zdzzl);
            }
            if (params.containsKey("clzl")) {
                mjVehicle.setClzl(params.get("clzl").toString());
            }
            if (params.containsKey("cpys")) {
                mjVehicle.setCpys(params.get("cpys").toString());
            }
            if (params.containsKey("lwzt") && (!params.get("lwzt").toString().equals(""))) {
                mjVehicle.setLwzt(params.get("lwzt").toString());
            }
            if (params.containsKey("pfpdyj") && (!params.get("pfpdyj").toString().equals(""))) {
                mjVehicle.setPfpdyj(params.get("pfpdyj").toString());
            }
            if (params.containsKey("xszazp") && (!params.get("xszazp").toString().equals(""))) {
                mjVehicle.setXszazp(params.get("xszazp").toString().replace(zpurl, ""));
            }
            if (params.containsKey("xszbzp") && (!params.get("xszbzp").toString().equals(""))) {
                mjVehicle.setXszbzp(params.get("xszbzp").toString().replace(zpurl, ""));
            }
            if (params.containsKey("scqdzp") && (!params.get("scqdzp").toString().equals(""))) {
                mjVehicle.setScqdzp(params.get("scqdzp").toString().replace(zpurl, ""));
            }
            if (params.containsKey("vehiclezp") && (!params.get("vehiclezp").toString().equals(""))) {
                mjVehicle.setVehiclezp(params.get("vehiclezp").toString().replace(zpurl, ""));
            }
            if (params.containsKey("pfpdyjzl") && (!params.get("pfpdyjzl").toString().equals(""))) {
                mjVehicle.setPfpdyjzl(params.get("pfpdyjzl").toString());
            }
            List<MjVehicle> mjVehicles = publicService.selectObjs("mj_vehicle", "", "cphm,cpys", mjVehicle);
            mjVehicle.setSczt("0");
            mjVehicle.setScjg("");
            SimpleDateFormat f = new SimpleDateFormat("HH");
            String time = f.format(new Date());
            int itime = Integer.parseInt(time);
            if ((itime > 7) && (itime < 19)) {
                mjVehicle.setClbj("1");
                mjVehicle.setCljg("通过");
            } else {
                mjVehicle.setClbj("3");
                mjVehicle.setCljg("初次接收");
            }
            mjVehicle.setCjsj(new Date());
            if (mjVehicles.size() > 1) {
                publicService.update("delete from mj_vehicle where cphm='" + mjVehicle.getCphm() + "' and cpys='" + mjVehicle.getCpys() + "'");
                UUID uuid = UUID.randomUUID();
                String uuids = uuid.toString().replaceAll("-", "");
                mjVehicle.setId(uuids);
                mjVehicle.setCjsj(new Date());
                logger.debug("多条信息删掉后插入");
                publicService.insert("mj_vehicle", "", "", mjVehicle);
            } else if (mjVehicles.size() == 1) {
                String id = mjVehicles.get(0).getId();
                if ((id != null) && (!id.equals(""))) {
                    mjVehicle.setId(mjVehicles.get(0).getId());
                    logger.debug("已有此车信息,更新");
                    publicService.update("mj_vehicle", "id", "id", mjVehicle);
                } else {
                    UUID uuid = UUID.randomUUID();
                    String uuids = uuid.toString().replaceAll("-", "");
                    mjVehicle.setId(uuids);
                    publicService.update("mj_vehicle", "", "cphm,cpys", mjVehicle);
                }
            } else {
                UUID uuid = UUID.randomUUID();
                String uuids = uuid.toString().replaceAll("-", "");
                mjVehicle.setId(uuids);
                mjVehicle.setCjsj(new Date());
                logger.debug("无有此车信息,插入");
                publicService.insert("mj_vehicle", "", "", mjVehicle);
            }
        } catch (Exception e) {
            srt = "\"" + e.getMessage() + "\"";
        }
        logger.debug(srt);
        return srt;
    }

    @RequestMapping(value = "/DataGrid", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataGrid(@RequestParam Map<String, Object> params, Integer page, Integer rows, String sort, String order) {
        try {
            PageInfo pageInfo = new PageInfo(page, rows, sort, order);
            String strWhere = "";
            if (params.containsKey("sbzt") && StringUtils.isNoneBlank(params.get("sbzt").toString())) {
                strWhere += String.format(" and sbzt='%s'", params.get("sbzt").toString());
            }
            if (params.containsKey("sbpj") && StringUtils.isNoneBlank(params.get("sbpj").toString())) {
                strWhere += String.format(" and sbpj='%s'", params.get("sbpj").toString());
            }
            if (params.containsKey("kssj") && StringUtils.isNoneBlank(params.get("kssj").toString())) {
                strWhere += String.format(" and cjsj  > '%s 00:00:00'", params.get("kssj").toString());
            }
            if (params.containsKey("jssj") && StringUtils.isNoneBlank(params.get("jssj").toString())) {
                strWhere += String.format(" and cjsj  <= '%s 23:59:59'", params.get("jssj").toString());
            }
            String tblName = "ai_photo";
            logger.info(tblName);
            publicService.selectPageObjects(pageInfo, tblName, "*", strWhere, "cjsj desc");
            pageInfo.setCode(0);
            return pageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return renderError(e.getMessage());
        }
    }

    @RequestMapping("/app/auth")
    public String toIndex() {
        String url = sysOptionService.getConfigValue("系统设置", "微信公众平台域名");
        String appid = sysOptionService.getConfigValue("微信配置", "appId");
        try {
            url = URLEncoder.encode(url, "UTF-8");
            logger.debug("微信公众平台域名" + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String redirectUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid + "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        logger.debug("转入url:" + redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @RequestMapping("/sendxszzp")
    @ResponseBody
    public String sendxszzp(@RequestParam("file") MultipartFile file, String zpzl) {
        String srt = "{\"code\":\"0\",\"message\":\"行驶证识别失败，请重试\"}";
        ConverUtil converUtil = new ConverUtil();
        try {
            logger.debug("进入sendxszzp");
            byte[] fileBytes = compressJpg(file.getInputStream(), 500 * 1024, 1920, 1080);
            // 使用Base64对字节数组进行编码
            String encodedString = Base64.getEncoder().encodeToString(fileBytes);

                // 图片路径
                String imgUrl = null;
                String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
                String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");
                String xsztype = sysOptionService.getConfigValue("系统设置", "行驶证识别方式");
                if(xsztype.equals("")){
                    xsztype="1";
                }

                String sdate = shortDateFormater.format(new Date());
                String syear = sdate.substring(0, 4);
                String smonth = sdate.substring(5, 7);
                String sday = sdate.substring(8, 10);
                String remotepath = "xsz/" + syear + "/" + smonth + "/" + sday;
                String zpdir = uploadDir + "/xsz/" + syear + "/" + smonth + "/" + sday;
                File dir = new File(zpdir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String ofilename = file.getOriginalFilename();
                String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
                String remotefilename = "/xsz/" + syear + "/" + smonth + "/" + sday + "/" + filename;
                zpurl = zpurl + remotefilename;
                filename = zpdir + "/" + filename;
                //上传
                if (upload(fileBytes, filename)) {
                    if(xsztype.equals("1")) {
                        String APP_ID = sysOptionService.getConfigValue("微信配置", "bd_appid");
                        String API_KEY = sysOptionService.getConfigValue("微信配置", "bd_apikey");
                        String SECRET_KEY = sysOptionService.getConfigValue("微信配置", "bd_secretkey");
                        logger.debug("API_KEY:" + API_KEY);
                        logger.debug("SECRET_KEY:" + SECRET_KEY);
                        String access_token = getAuth(API_KEY, SECRET_KEY);
                        logger.debug("access_token:" + access_token);
                        String bdapiurl = "https://aip.baidubce.com/rest/2.0/ocr/v1/vehicle_license";
                        logger.debug(bdapiurl);

                        String requestUrl = bdapiurl + "?access_token=" + access_token;
                        URL url = new URL(requestUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // 设置请求方法为POST
                        connection.setRequestMethod("POST");

                        // 设置请求头Content-Type为application/x-www-form-urlencoded
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        String postData = "";
                        // 设置请求头允许写入（如果是POST请求）
                        connection.setDoOutput(true);
                        if (zpzl.equals("front")) {
                            // 发送POST请求体数据
                            postData = "image=" + URLEncoder.encode(encodedString, "UTF-8") + "&vehicle_license_side=front"; // 对image的值进行URL编码
                        } else if (zpzl.equals("back")) {
                            postData = "image=" + URLEncoder.encode(encodedString, "UTF-8") + "&vehicle_license_side=back"; // 对image的值进行URL编码
                        }
                        try (OutputStream os = connection.getOutputStream()) {
                            byte[] input = postData.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }
                        String sret = "";
                        // 获取响应码
                        int responseCode = connection.getResponseCode();
                        System.out.println("Response Code: " + responseCode);
                        if (responseCode == 200) {
                            try (BufferedReader in = new BufferedReader(
                                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                                String inputLine;
                                StringBuffer response = new StringBuffer();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }

                                // 打印响应内容
                                logger.debug("识别结果: " + response.toString());
                                sret = response.toString();

                            } finally {
                                // 关闭连接
                                connection.disconnect();
                            }
                            MjAiPhoto mjAiPhoto = new MjAiPhoto();
                            String uid = UUID.randomUUID().toString().replace("-", "");
                            mjAiPhoto.setId(uid);
                            mjAiPhoto.setZpzl(zpzl);
                            mjAiPhoto.setZp(remotefilename);
                            mjAiPhoto.setSbzt("1");
                            mjAiPhoto.setSbjg(sret);
                            mjAiPhoto.setCjsj(new Date());
                            mjAiPhoto.setSbsj(new Date());
                            mjAiPhoto.setSbtype("1");
                            midPublicService.insert("mj_ai_photo", "", "", mjAiPhoto);
                            if (sret.contains("words_result")) {
                                MjVehicle mjVehicle = new MjVehicle();
                                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(sret);
                                com.alibaba.fastjson.JSONObject wordsResult = jsonObject.getJSONObject("words_result");
                                //识别车辆信息
                                for (String key : wordsResult.keySet()) {
                                    com.alibaba.fastjson.JSONObject innerObject = wordsResult.getJSONObject(key);
                                    String words = innerObject.getString("words");
                                    if (key.equals("号牌号码")) {
                                        mjVehicle.setCphm(words);
                                    } else if (key.equals("车辆识别代号")) {
                                        mjVehicle.setClsbdh(words);
                                    } else if (key.equals("车辆类型")) {
                                        mjVehicle.setCllx(words);
                                    } else if (key.equals("所有人")) {
                                        mjVehicle.setSyr(words);
                                    } else if (key.equals("使用性质")) {
                                        mjVehicle.setSyxz(words);
                                    } else if (key.equals("品牌型号")) {
                                        mjVehicle.setClppxh(words);
                                    } else if (key.equals("发动机号码")) {
                                        mjVehicle.setFdjh(words);
                                    } else if (key.equals("注册日期")) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                        try {
                                            Date date = sdf.parse(words);
                                            mjVehicle.setCcdjrq(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (key.equals("发证日期")) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                                        try {
                                            Date date = sdf.parse(words);
                                            mjVehicle.setFzrq(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (key.equals("核定载质量")) {
                                        mjVehicle.setHdzzl(words);
                                    } else if (key.equals("燃油类型")) {
                                        mjVehicle.setRlzl(converUtil.convertrlzl(words));
                                    } else if (key.equals("总质量")) {
                                        mjVehicle.setZdzzl(words);
                                    } else if (key.equals("住址")) {
                                        mjVehicle.setAddress(words);
                                    } else if (key.equals("核定载人数")) {
                                        mjVehicle.setHdzrs(words);
                                    }
                                }
                                String cphm = mjVehicle.getCphm();
                                String cllx = mjVehicle.getCllx();

                                String clsbdh = mjVehicle.getClsbdh();
                                String scqdjg = "";
                                if (clsbdh != null && !clsbdh.equals("")) {
                                    mjVehicle = pfbzService.getPfbz(mjVehicle);
                                } else {
                                    logger.debug("车架号为空,不予匹配vehicle记录");
                                }
                                //判断是否新能源
                                if (cphm != null && !cphm.equals("")) {
                                    if (cphm.length() > 7) {
                                        mjVehicle.setRlzl("C");
                                        mjVehicle.setPfbz("D");
                                        if (cllx != null && !cllx.equals("")) {
                                            if (cllx.indexOf("大") >= 0 || cllx.indexOf("重") >= 0 || cllx.indexOf("中") >= 0) {
                                                mjVehicle.setCpys("6");
                                            } else if (cllx.indexOf("小") >= 0 || cllx.indexOf("轻") >= 0 || cllx.indexOf("微") >= 0) {
                                                mjVehicle.setCpys("4");
                                            }
                                        }
                                    } else {
                                        if (cllx != null && !cllx.equals("")) {
                                            if (cllx.indexOf("大") >= 0 || cllx.indexOf("重") >= 0 || cllx.indexOf("中") >= 0) {
                                                mjVehicle.setCpys("1");
                                            } else if (cllx.indexOf("小") >= 0 || cllx.indexOf("轻") >= 0 || cllx.indexOf("微") >= 0) {
                                                mjVehicle.setCpys("0");
                                            }
                                        }
                                    }

                                }
                                if (mjVehicle.getCllx() != null && !mjVehicle.getCllx().equals("")) {
                                    mjVehicle.setClzl(cllxtoclzl(mjVehicle.getCllx()));
                                }
                                String syxz = mjVehicle.getSyxz();
                                if (syxz != null && !syxz.equals("")) {
                                    if (syxz.contains("危")) {
                                        mjVehicle.setClzl("1");
                                    }
                                }
                                // 传入可选参数调用接口
                                HashMap<String, String> options = new HashMap<String, String>();
                                options.put("multi_detect", "false");


                                String pfbz = mjVehicle.getPfbz();
                                if ((pfbz != null) && (!pfbz.equals(""))) {
                                    if (pfbz.length() < 2) {
                                        pfbz = converUtil.pfsztopfhz(pfbz);
                                        mjVehicle.setPfbz(pfbz);
                                    }
                                }
                                if (cllx != null && !cllx.equals("")) {
                                    SysCode sysCode = new SysCode();
                                    sysCode.setOi_name("车辆类型");
                                    sysCode.setOi_value(cllx);
                                    sysCode = publicService.selectObj("ai_sys_code", "", "oi_name,oi_value", sysCode);
                                    if (sysCode != null) {
                                        mjVehicle.setCllxid(sysCode.getOi_code());
                                    } else {
                                        sysCode = publicService.selectObj("select * from ai_sys_code where oi_name='车辆类型' and oi_value like '%" + cllx + "%'", SysCode.class);
                                        if (sysCode != null) {
                                            mjVehicle.setCllxid(sysCode.getOi_code());
                                        } else {
                                            mjVehicle.setCllxid("");
                                        }
                                    }
                                }
                                if (syxz != null && !syxz.equals("")) {
                                    SysCode sysCode = new SysCode();
                                    sysCode.setOi_name("使用性质");
                                    sysCode.setOi_value(syxz);
                                    sysCode = publicService.selectObj("ai_sys_code", "", "oi_name,oi_value", sysCode);
                                    if (sysCode != null) {
                                        mjVehicle.setSyxzid(sysCode.getOi_code());
                                    } else {
                                        sysCode = publicService.selectObj("select * from ai_sys_code where oi_name='使用性质' and oi_value like '%" + syxz + "%'", SysCode.class);
                                        if (sysCode != null) {
                                            mjVehicle.setSyxzid(sysCode.getOi_code());
                                        } else {
                                            mjVehicle.setSyxzid("");
                                        }
                                    }
                                }
                                if (mjVehicle.getZdzzl() != null && !mjVehicle.getZdzzl().equals("")) {
                                    mjVehicle.setZdzzl(mjVehicle.getZdzzl().replaceAll("kg", ""));
                                }
                                if (mjVehicle.getHdzzl() != null && !mjVehicle.getHdzzl().equals("")) {
                                    mjVehicle.setHdzzl(mjVehicle.getHdzzl().replaceAll("kg", ""));
                                }
                                String svehicle = com.alibaba.fastjson.JSON.toJSONString(mjVehicle);

                                srt = "{\"code\":\"1\",\"message\":\"" + zpurl + "\",\"vehicle\":" + svehicle + "}";
                            }
                        }
                    }
                    else {
                        MjAiPhoto mjAiPhoto = new MjAiPhoto();
                        String uid = UUID.randomUUID().toString().replace("-", "");
                        mjAiPhoto.setId(uid);
                        mjAiPhoto.setZpzl(zpzl);
                        mjAiPhoto.setZp(remotefilename);
                        mjAiPhoto.setSbzt("0");
                        mjAiPhoto.setSbjg("");
                        mjAiPhoto.setCjsj(new Date());
                        mjAiPhoto.setSbsj(new Date());
                        mjAiPhoto.setSbtype("2");
                        midPublicService.insert("mj_ai_photo", "", "", mjAiPhoto);
                        String sbz = "";
                        boolean bsb = false;
                        for (int i = 0; i < 30; i++) {
                            MjAiPhoto mjAiPhoto1 = midPublicService.selectObj("mj_ai_photo", "", "id", mjAiPhoto);
                            if (mjAiPhoto1 != null) {
                                if (!mjAiPhoto1.getSbzt().equals("0")) {
                                    if (mjAiPhoto1.getSbzt().equals("1")) {
                                        sbz = mjAiPhoto1.getSbjg();
                                        bsb = true;
                                        break;
                                    } else {
                                        sbz = "";
                                        logger.debug("识别失败:" + mjAiPhoto1.getSbjg());
                                    }
                                } else {
                                    srt = "{\"code\":\"0\",\"message\":\"识别失败\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"\",\"pfbz\":\"\"}";
                                }
                            }
                            Thread.sleep(500);
                        }
                        if (!sbz.equals("")) {
                            logger.debug("行驶证识别完成");
                            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(sbz);
                            String code = jsonObject.getString("code");
                            if (code.equals("1")) {
                                MjVehicle mjVehicle = new MjVehicle();
                                if (zpzl.equals("front")) {
                                    mjVehicle.setCphm(jsonObject.getString("plate"));
                                    mjVehicle.setClsbdh(jsonObject.getString("vin"));
                                    mjVehicle.setCllx(jsonObject.getString("vehicleType"));
                                    mjVehicle.setSyr(jsonObject.getString("owner"));
                                    mjVehicle.setSyxz(jsonObject.getString("useCharacter"));
                                    mjVehicle.setClppxh(jsonObject.getString("model"));
                                    mjVehicle.setFdjh(jsonObject.getString("engineNo"));
                                    mjVehicle.setAddress(jsonObject.getString("address"));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        Date date = sdf.parse(jsonObject.getString("issueDate"));
                                        mjVehicle.setFzrq(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        Date date = sdf.parse(jsonObject.getString("registerDate"));
                                        mjVehicle.setCcdjrq(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else if (zpzl.equals("back")) {
                                    String approvedLoad = jsonObject.getString("approvedLoad");
                                    approvedLoad = approvedLoad.replaceAll("[^0-9.]", "");
                                    mjVehicle.setHdzzl(approvedLoad);
                                    mjVehicle.setCphm(jsonObject.getString("plate1"));
                                    mjVehicle.setRlzl(converUtil.convertrlzl(jsonObject.getString("fuelType")));
                                    String Zdzzl = jsonObject.getString("grossMass");
                                    Zdzzl = Zdzzl.replaceAll("[^0-9.]", "");
                                    mjVehicle.setZdzzl(Zdzzl);
                                    String Hdzrs = jsonObject.getString("passengers");
                                    Hdzrs = Hdzrs.replaceAll("[^0-9.]", "");
                                    mjVehicle.setHdzrs(Hdzrs);
                                }
                                String cphm = mjVehicle.getCphm();
                                String cllx = mjVehicle.getCllx();

                                String clsbdh = mjVehicle.getClsbdh();
                                String scqdjg = "";
                                if (clsbdh != null && !clsbdh.equals("")) {
                                    mjVehicle = pfbzService.getPfbz(mjVehicle);
                                } else {
                                    logger.debug("车架号为空,不予匹配vehicle记录");
                                }
                                //判断是否新能源
                                if (cphm != null && !cphm.equals("")) {
                                    if (cphm.length() > 7) {
                                        mjVehicle.setRlzl("C");
                                        mjVehicle.setPfbz("D");
                                        if (cllx != null && !cllx.equals("")) {
                                            if (cllx.indexOf("大") >= 0 || cllx.indexOf("重") >= 0 || cllx.indexOf("中") >= 0) {
                                                mjVehicle.setCpys("6");
                                            } else if (cllx.indexOf("小") >= 0 || cllx.indexOf("轻") >= 0 || cllx.indexOf("微") >= 0) {
                                                mjVehicle.setCpys("4");
                                            }
                                        }
                                    } else {
                                        if (cllx != null && !cllx.equals("")) {
                                            if (cllx.indexOf("大") >= 0 || cllx.indexOf("重") >= 0 || cllx.indexOf("中") >= 0) {
                                                mjVehicle.setCpys("1");
                                            } else if (cllx.indexOf("小") >= 0 || cllx.indexOf("轻") >= 0 || cllx.indexOf("微") >= 0) {
                                                mjVehicle.setCpys("0");
                                            }
                                        }
                                    }

                                }
                                if (mjVehicle.getCllx() != null && !mjVehicle.getCllx().equals("")) {
                                    mjVehicle.setClzl(cllxtoclzl(mjVehicle.getCllx()));
                                }
                                String syxz = mjVehicle.getSyxz();
                                if (syxz != null && !syxz.equals("")) {
                                    if (syxz.contains("危")) {
                                        mjVehicle.setClzl("1");
                                    }
                                }
                                // 传入可选参数调用接口
                                HashMap<String, String> options = new HashMap<String, String>();
                                options.put("multi_detect", "false");


                                String pfbz = mjVehicle.getPfbz();
                                if ((pfbz != null) && (!pfbz.equals(""))) {
                                    if (pfbz.length() < 2) {
                                        pfbz = converUtil.pfsztopfhz(pfbz);
                                        mjVehicle.setPfbz(pfbz);
                                    }
                                }
                                if (cllx != null && !cllx.equals("")) {
                                    SysCode sysCode = new SysCode();
                                    sysCode.setOi_name("车辆类型");
                                    sysCode.setOi_value(cllx);
                                    sysCode = publicService.selectObj("ai_sys_code", "", "oi_name,oi_value", sysCode);
                                    if (sysCode != null) {
                                        mjVehicle.setCllxid(sysCode.getOi_code());
                                    } else {
                                        sysCode = publicService.selectObj("select * from ai_sys_code where oi_name='车辆类型' and oi_value like '%" + cllx + "%'", SysCode.class);
                                        if (sysCode != null) {
                                            mjVehicle.setCllxid(sysCode.getOi_code());
                                        } else {
                                            mjVehicle.setCllxid("");
                                        }
                                    }
                                }
                                if (syxz != null && !syxz.equals("")) {
                                    SysCode sysCode = new SysCode();
                                    sysCode.setOi_name("使用性质");
                                    sysCode.setOi_value(syxz);
                                    sysCode = publicService.selectObj("ai_sys_code", "", "oi_name,oi_value", sysCode);
                                    if (sysCode != null) {
                                        mjVehicle.setSyxzid(sysCode.getOi_code());
                                    } else {
                                        sysCode = publicService.selectObj("select * from ai_sys_code where oi_name='使用性质' and oi_value like '%" + syxz + "%'", SysCode.class);
                                        if (sysCode != null) {
                                            mjVehicle.setSyxzid(sysCode.getOi_code());
                                        } else {
                                            mjVehicle.setSyxzid("");
                                        }
                                    }
                                }
                                if (mjVehicle.getZdzzl() != null && !mjVehicle.getZdzzl().equals("")) {
                                    mjVehicle.setZdzzl(mjVehicle.getZdzzl().replaceAll("kg", ""));
                                }
                                if (mjVehicle.getHdzzl() != null && !mjVehicle.getHdzzl().equals("")) {
                                    mjVehicle.setHdzzl(mjVehicle.getHdzzl().replaceAll("kg", ""));
                                }
                                String svehicle = com.alibaba.fastjson.JSON.toJSONString(mjVehicle);

                                srt = "{\"code\":\"1\",\"message\":\"" + zpurl + "\",\"vehicle\":" + svehicle + "}";
                            }
                            else{
                                srt = "{\"code\":\"0\",\"message\":\"请规范拍摄\",\"vehicle\":\"\"}";
                            }
                        }
                        else{
                            srt = "{\"code\":\"0\",\"message\":\"请规范拍摄\",\"vehicle\":\"\"}";
                        }
                    }
                } else {
                    srt = "{\"code\":\"0\",\"message\":\"照片保存失败\",\"vehicle\":\"\"}";
                }

        } catch (Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\"" + e.getMessage() + "\"}";
            logger.debug("保存行驶证失败:" + e.getMessage());
        }
        return srt;
    }

    public double getSimilarity(String str1, String str2) {
        // 空值处理
        if (str1 == null && str2 == null) {
            return 1.0;
        }
        if (str1 == null || str2 == null) {
            return 0.0;
        }

        // 计算莱文斯坦距离
        int editDistance = levenshteinDistance(str1, str2);

        // 最长长度作为分母
        int maxLength = Math.max(str1.length(), str2.length());

        if (maxLength == 0) {
            return 1.0;
        }

        // 公式：相似度 = 1 - (编辑距离 / 最长字符串长度)
        return 1.0 - (double) editDistance / maxLength;
    }

    /**
     * 莱文斯坦距离：计算从 str1 变成 str2 需要的最少修改次数（增/删/改）
     */
    private int levenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        // 构建二维数组
        int[][] dp = new int[len1 + 1][len2 + 1];

        // 初始化第一行、第一列
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // 动态规划填充
        for (int i = 1; i <= len1; i++) {
            char c1 = str1.charAt(i - 1);
            for (int j = 1; j <= len2; j++) {
                char c2 = str2.charAt(j - 1);

                // 字符相等，代价0
                int cost = c1 == c2 ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,    // 删除
                                dp[i][j - 1] + 1),   // 插入
                        dp[i - 1][j - 1] + cost);     // 替换
            }
        }

        return dp[len1][len2];
    }

    @RequestMapping("/sendscqdzp")
    @ResponseBody
    public String sendscqdzp(@RequestParam("file") MultipartFile file, String fdjh, String clsbdh, String clppxh) {
        String srt = "{\"code\":\"0\",\"message\":\"照片保存失败\"}";
        try {
            logger.debug("sendscqdzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "scqd/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/scqd/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/scqd/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            byte[] imagebytes = compressJpg(file.getInputStream(), 500 * 1024, 1920, 1920);
            //上传
            if (upload(imagebytes, filename)) {
                srt = "{\"code\":\"1\",\"message\":\"成功\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"\",\"pfbz\":\"\"}";
                MjAiPhoto mjAiPhoto = new MjAiPhoto();
                String uid = UUID.randomUUID().toString().replace("-", "");
                mjAiPhoto.setId(uid);
                mjAiPhoto.setZpzl("scqd");
                mjAiPhoto.setZp(remotefilename);
                mjAiPhoto.setSbzt("0");
                mjAiPhoto.setSbjg("");
                mjAiPhoto.setCjsj(new Date());
                mjAiPhoto.setSbtype("2");
                midPublicService.insert("mj_ai_photo", "", "", mjAiPhoto);
                String sbz = "";
                boolean bsb = false;
                for (int i = 0; i < 30; i++) {
                    MjAiPhoto mjAiPhoto1 = midPublicService.selectObj("mj_ai_photo", "", "id", mjAiPhoto);
                    if (mjAiPhoto1 != null) {
                        if (!mjAiPhoto1.getSbzt().equals("0")) {
                            if (mjAiPhoto1.getSbzt().equals("1")) {
                                sbz = mjAiPhoto1.getSbjg();
                                bsb = true;
                                break;
                            } else {
                                sbz = "";
                                logger.debug("识别失败:" + mjAiPhoto1.getSbjg());
                            }
                        } else {
                            srt = "{\"code\":\"0\",\"message\":\"识别失败\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"\",\"pfbz\":\"\"}";
                        }
                    }
                    Thread.sleep(500);
                }
                if (!sbz.equals("")) {
                    logger.debug("随车清单识别完成");
                    clppxh = clppxh.replaceAll("[\\u4e00-\\u9fa5]", "");
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(sbz);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String clppxhex = jsonObject.getString("clxh");
                        clppxhex = clppxhex.replaceAll("[\\u4e00-\\u9fa5]", "");
                        String clsbbhex = jsonObject.getString("clsbdh");
                        String fdjhex = jsonObject.getString("fdjh");
                        String pfbzex = jsonObject.getString("pfbz");
                        boolean sfyz = false;
                        double dxsd = getSimilarity(clsbbhex, clsbdh);
                        if (dxsd >= 0.8) {
                            sfyz = true;
                        }
                        if (!sfyz) {
                            dxsd = getSimilarity(clppxhex, clppxh);
                            if (dxsd >= 0.8) {
                                sfyz = true;
                            }
                        }
                        if (!sfyz) {
                            dxsd = getSimilarity(fdjhex, fdjh);
                            if (dxsd >= 0.8) {
                                sfyz = true;
                            }
                        }
                        if (sfyz) {
                            logger.debug("随车清单发动机号匹配");
                            String spfbz = pfbzex;
                            String pfbz = "";
                            if ((spfbz.contains("国零")) || (spfbz.contains("国0"))) {
                                pfbz = "0";
                            } else if (spfbz.contains("一")) {
                                pfbz = "1";
                            } else if (spfbz.contains("二")) {
                                pfbz = "2";
                            } else if (spfbz.contains("三") || spfbz.contains("III")) {
                                pfbz = "3";
                            } else if (spfbz.contains("四") || spfbz.contains("IV")) {
                                pfbz = "4";
                            } else if (spfbz.contains("五") || spfbz.contains("V")) {
                                pfbz = "5";
                            } else if (spfbz.contains("六") || spfbz.contains("VI")) {
                                pfbz = "6";
                            } else if (spfbz.contains("电")) {
                                pfbz = "D";
                            }
                            logger.debug("随车清单排放为：" + pfbz);
                            ConverUtil converUtil = new ConverUtil();
                            String pfbzid = pfbz;
                            pfbz = converUtil.pfsztopfhz(pfbz);
                            String pfbzyj = "随车清单照片识别获取";
                            String pfbzyjzl = "4";
                            srt = "{\"code\":\"1\",\"message\":\"成功\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"" + pfbzid + "\",\"pfbz\":\"" + pfbz + "\",\"pfpzyj\":\"" + pfbzyj + "\",\"pfbzyjzl\":\"" + pfbzyjzl + "\"}";
                        } else {
                            logger.debug("随车清单非本车");
                            srt = "{\"code\":\"0\",\"message\":\"随车清单非本车或拍摄不规范,请确认\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"\",\"pfbz\":\"\",\"pfpzyj\":\"\",\"pfbzyjzl\":\"\"}";
                        }
                    } else {
                        srt = "{\"code\":\"0\",\"message\":\"非有效随车清单或拍摄不规范，请确认\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"\",\"pfbz\":\"\",\"pfpzyj\":\"\",\"pfbzyjzl\":\"\"}";
                    }
                } else {
                    if (bsb) {
                        srt = "{\"code\":\"0\",\"message\":\"非有效随车清单或拍摄不规范，请确认\",\"url\":\"" + zpurl + "\",\"pfbzid\":\"\",\"pfbz\":\"\",\"pfpzyj\":\"\",\"pfbzyjzl\":\"\"}";
                    } else {
                        logger.debug("随车清单未识别");
                    }
                }
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\"" + e.getMessage() + "\",\"url\":\"\",\"pfbzid\":\"\",\"pfbz\":\"\"}";
            logger.debug("保存随车清单失败:" + e.getMessage());
        }
        logger.debug("随车清单识别返回:" + srt);
        return srt;
    }

    @RequestMapping("/sendvehiclezp")
    @ResponseBody
    public String sendvehiclezp(@RequestParam("file") MultipartFile file) {
        String srt = "{\"code\":\"0\",\"message\":\"照片保存失败\"}";
        try {
            logger.debug("sendscqdzp");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "vehicle/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/vehicle/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/vehicle/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            byte[] imagebytes = compressJpg(file.getInputStream(), 500 * 1024, 1920, 1080);
            //上传
            if (upload(imagebytes, filename)) {
                srt = "{\"code\":\"1\",\"message\":\"成功\",\"url\":\"" + zpurl + "\",\"pfbz\":\"\"}";
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\"" + e.getMessage() + "\",\"url\":\"\",\"pfbz\":\"\"}";
            logger.debug("保存车辆照片失败:" + e.getMessage());
        }
        return srt;
    }
    // 字符偏移量（前后端必须一致！）
    private static final int SHIFT = 3;
    private static final String KEY = "my-secret-key-123";

    /**
     * 编码含中文的sbz字符串
     *
     * @param sbzJsonStr 含中文的JSON字符串
     * @return 编码结果
     */
    public static String encode(String sbzJsonStr) {
        try {
            // 1. 密钥混淆：将JSON字符串与密钥拼接（简单加密）
            String mixed = sbzJsonStr + "|" + KEY;

            // 2. Base64编码（指定UTF-8，确保中文正确处理）
            return Base64.getEncoder().encodeToString(mixed.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException("编码失败：" + e.getMessage());
        }
    }

    @RequestMapping("/sendxsza")
    @ResponseBody
    public String sendxsza(@RequestParam("file") MultipartFile file, String zpzl, HttpServletRequest request) {
        String srt = "{\"code\":\"0\",\"message\":\"行驶证识别失败，请重试\"}";
        ConverUtil converUtil = new ConverUtil();
        try {
            logger.debug("进入sendxsza");
            // 图片路径
            String imgUrl = null;
            String uploadDir = sysOptionService.getConfigValue("系统设置", "照片存储路径");
            String zpurl = sysOptionService.getConfigValue("系统设置", "照片url地址");

            String sdate = shortDateFormater.format(new Date());
            String syear = sdate.substring(0, 4);
            String smonth = sdate.substring(5, 7);
            String sday = sdate.substring(8, 10);
            String remotepath = "xsz/" + syear + "/" + smonth + "/" + sday;
            String zpdir = uploadDir + "/xsz/" + syear + "/" + smonth + "/" + sday;
            File dir = new File(zpdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String ofilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString().replace("-", "") + ofilename.substring(ofilename.lastIndexOf("."));
            String remotefilename = "/xsz/" + syear + "/" + smonth + "/" + sday + "/" + filename;
            zpurl = zpurl + remotefilename;
            filename = zpdir + "/" + filename;
            byte[] fileBytes = compressJpg(file.getInputStream(), 500 * 1024, 1920, 1080);
            //上传
            if (upload(fileBytes, filename)) {

                MjAiPhoto mjAiPhoto = new MjAiPhoto();
                String uid = UUID.randomUUID().toString().replace("-", "");
                mjAiPhoto.setId(uid);
                mjAiPhoto.setZpzl(zpzl);
                mjAiPhoto.setZp(remotefilename);
                mjAiPhoto.setSbzt("0");
                mjAiPhoto.setSbjg("");
                mjAiPhoto.setCjsj(new Date());
                mjAiPhoto.setSbtype("1");
                String svehicle = "";
                publicService.insert("ai_photo", "", "", mjAiPhoto);
                String sbz = "";
                boolean bsb = false;
                for (int i = 0; i < 30; i++) {
                    MjAiPhoto mjAiPhoto1 = publicService.selectObj("ai_photo", "", "id", mjAiPhoto);
                    if (mjAiPhoto1 != null) {
                        if (!mjAiPhoto1.getSbzt().equals("0")) {
                            if (mjAiPhoto1.getSbzt().equals("1")) {
                                sbz = mjAiPhoto1.getSbjg();
                                bsb = true;
                                break;
                            } else {
                                sbz = "";
                                logger.debug("识别失败:" + mjAiPhoto1.getSbjg());
                            }
                        } else {
                            srt = "{\"code\":\"0\",\"message\":\"识别失败\",\"url\":\"" + zpurl + "\",\"pid\":\"" + uid + "\",\"sbz\":\"" + encode(sbz) + "\"}";
                        }
                    }
                    Thread.sleep(500);
                }
                srt = "{\"code\":\"1\",\"message\":\"识别成功\",\"url\":\"" + zpurl + "\",\"pid\":\"" + uid + "\",\"sbz\":\"" + encode(sbz) + "\"}";
            } else {
                srt = "{\"code\":\"0\",\"message\":\"照片保存失败\",\"url\":\"" + zpurl + "\",\"pid\":\"\",\"sbz\":\"照片保存失败\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            srt = "{\"code\":\"0\",\"message\":\"照片保存失败\",\"url\":\"\",\"pid\":\"\",\"sbz\":\"" + e.getMessage() + "\"}";
            logger.debug("保存行驶证失败:" + e.getMessage());
        }
        return srt;
    }

    public String extractTextAroundKeyword(String text, String keyword, int charsBeforeAfter) {
        int index = text.indexOf(keyword);
        if (index == -1) {
            return "未找到关键词"; // 或者返回 "" 表示空字符串
        }

        // 计算开始和结束的索引，确保不超出字符串边界
        int start = Math.max(0, index - charsBeforeAfter);
        int end = Math.min(text.length(), index + keyword.length() + charsBeforeAfter);

        // 提取子字符串
        return text.substring(start, end);
    }

    public boolean upload(byte[] imageBytes, String fileName) throws Exception {
        logger.debug("保存照片:" + fileName);
        boolean brt = false;
        File dest = new File(fileName);
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(imageBytes);
            brt = true;
            logger.debug("保存照片成功");
        } catch (IOException e) {
            logger.debug("保存文件异常11:" + e.getMessage());
            logger.debug("保存文件异常11:" + e.getMessage());
        }
        return brt;
    }

    public boolean upload(MultipartFile file, String fileName) throws Exception {
        logger.debug("保存照片:" + fileName);
        boolean brt = false;
        try {
            File dest = new File(fileName);
            // 判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            // 保存文件
            file.transferTo(dest);
            logger.debug("保存照片成功");
            brt = true;
        } catch (FrameGrabber.Exception e) {
            logger.debug("保存文件异常11:" + e.getMessage());
            logger.debug("保存文件异常11:" + e.getMessage());
        }
        return true;
    }

    private String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        logger.debug("authHost:" + authHost);
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        logger.debug("getAccessTokenUrl:" + getAccessTokenUrl);
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                //  System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            logger.debug("获取token失败！" + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    String hpzltocpys(String hpzl) {
        String sret = "";
        if (hpzl.equals("01") || hpzl.equals("15") || hpzl.equals("16")) {
            sret = "1";
        } else if (hpzl.equals("02")) {
            sret = "0";
        } else if (hpzl.equals("51") || hpzl.equals("52")) {
            sret = "4";
        } else {
            sret = "6";
        }
        return sret;
    }

    String cllxtoclzl(String cllx) {
        String sret = "";
        if (cllx.contains("客") || cllx.contains("旅居") || cllx.contains("轿") || cllx.contains("面包") || cllx.contains("校")) {
            sret = "4";
        } else if (cllx.contains("专项")) {
            sret = "2";
        } else if (cllx.contains("货") || cllx.contains("挂") || cllx.contains("牵引")) {
            sret = "0";
        } else {
            sret = "";
        }
        return sret;
    }

    public static byte[] compressJpg(InputStream inputStream, double targetSizeBytes, int maxWidth, int maxHeight) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        if (bufferedImage.getWidth() > maxWidth || bufferedImage.getHeight() > maxHeight) {
            bufferedImage = scaleImage(bufferedImage, maxWidth, maxHeight);
        }
        float quality = 0.9f;

        byte[] compressedImageBytes = null;
        long currentSize = 0;

        do {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            writeJpgWithQuality(bufferedImage, baos, quality);
            compressedImageBytes = baos.toByteArray();
            currentSize = compressedImageBytes.length;

            if (currentSize > targetSizeBytes) {
                quality -= 0.1f; // 更大的步长可能会更快收敛
                if (quality < 0.1f) {
                    // 如果质量已经低于0.1，则退出循环，避免无限循环（尽管这在实践中不太可能发生）
                    break;
                }
            } else {
                break;
            }

            // 清理资源
            baos.close();

        } while (Math.abs(currentSize - targetSizeBytes) > 1024); // 设置一个阈值来停止迭代，例如1KB

        return compressedImageBytes;
    }

    private static void writeJpgWithQuality(BufferedImage image, OutputStream outputStream, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (writers.hasNext()) {
            ImageWriter writer = writers.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), param);
            } finally {
                writer.dispose();
            }
        }
    }

    private static BufferedImage scaleImage(BufferedImage img, int maxWidth, int maxHeight) {
        int width = img.getWidth();
        int height = img.getHeight();

        double scaleWidth = (double) maxWidth / width;
        double scaleHeight = (double) maxHeight / height;

        double scale = Math.min(scaleWidth, scaleHeight);

        width = (int) (width * scale);
        height = (int) (height * scale);

        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(img, 0, 0, width, height, null);
        g2d.dispose();

        return scaledImage;
    }

    private static byte[] imageBase64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;
        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();
            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = is.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 对字节数组Base64编码
            //System.out.println("解压后大小kb："+outStream.toByteArray().length/1024);
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }//下载
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpUrl != null) {
                httpUrl.disconnect();
            }
        }
        return null;
    }

}
