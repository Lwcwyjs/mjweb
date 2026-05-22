package com.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VinWebAutoUtil {
    private static Map<String, WebDriver> driverMap = new ConcurrentHashMap<>();
    private static final String TARGET_URL = "https://info.vecc.org.cn/ve/vin/index";
    private static Map<String, String> veccCookieMap = new ConcurrentHashMap<>();
    private static final int CAPTCHA_CONNECT_TIMEOUT_MS = 20000;
    private static final int CAPTCHA_READ_TIMEOUT_MS = 20000;
    private static final int QUERY_CONNECT_TIMEOUT_MS = 20000;
    private static final int QUERY_READ_TIMEOUT_MS = 60000;
    private static final int RENDER_WAIT_TIMEOUT_MS = 90000;
    private static final String CHROME_DESKTOP_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36";

    public String initAndGetCaptcha(String sessionId) {
        try {
            CaptchaFetchResult captchaFetchResult = fetchCaptchaFromVecc(veccCookieMap.get(sessionId));
            if (captchaFetchResult == null || captchaFetchResult.imageBytes == null || captchaFetchResult.imageBytes.length == 0) {
                return "";
            }
            if (captchaFetchResult.cookie != null && !captchaFetchResult.cookie.isEmpty()) {
                veccCookieMap.put(sessionId, captchaFetchResult.cookie);
            }
            return toDataImageBase64(captchaFetchResult.imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public QueryResult queryAndScreenshot(String sessionId, String vin, String engineLast6, String captcha) {
        QueryResult result = new QueryResult();
        WebDriver driver = driverMap.get(sessionId);
        if (driver == null) {
            result.code = 500;
            result.msg = "会话已过期，请刷新验证码";
            return result;
        }

        try {
            WebElement vinInput = findVinInput(driver);
            WebElement engineInput = findEngineInput(driver);
            WebElement captchaInput = findCaptchaInput(driver);
            WebElement queryBtn = findQueryButton(driver);
            if (vinInput == null || engineInput == null || captchaInput == null || queryBtn == null) {
                result.code = 500;
                result.msg = "页面元素定位失败";
                result.captchaBase64 = safeCaptchaBase64(driver);
                return result;
            }

            vinInput.clear();
            vinInput.sendKeys(vin);
            engineInput.clear();
            engineInput.sendKeys(engineLast6);
            captchaInput.clear();
            captchaInput.sendKeys(captcha);

            queryBtn.click();

            String alertText = tryGetAndAcceptAlert(driver, 2);
            if (alertText != null && !alertText.trim().isEmpty()) {
                result.code = 400;
                result.msg = alertText.trim();
                result.captchaBase64 = safeCaptchaBase64(driver);
                return result;
            }

            Thread.sleep(800);

            String currentUrl = driver.getCurrentUrl();
            result.currentUrl = currentUrl;
            if (currentUrl != null && !currentUrl.contains("/ve/vin/index")) {
                result.code = 200;
                result.msg = "OK";
                result.screenshotPng = fullPageScreenshot(driver);
                return result;
            }

            String pageSource = driver.getPageSource();
            if (pageSource != null) {
                if (pageSource.contains("验证码") && pageSource.contains("错误")) {
                    result.code = 400;
                    result.msg = "验证码错误";
                } else if (pageSource.contains("参数") && pageSource.contains("错误")) {
                    result.code = 400;
                    result.msg = "参数错误";
                } else {
                    result.code = 500;
                    result.msg = "查询失败";
                }
            } else {
                result.code = 500;
                result.msg = "查询失败";
            }
            result.captchaBase64 = safeCaptchaBase64(driver);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 500;
            result.msg = "查询异常：" + e.getMessage();
            try {
                result.captchaBase64 = safeCaptchaBase64(driver);
            } catch (Exception ignored) {
            }
            return result;
        } finally {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
            driverMap.remove(sessionId);
        }
    }

    public String refreshCaptcha(String sessionId) {
        try {
            CaptchaFetchResult captchaFetchResult = fetchCaptchaFromVecc(veccCookieMap.get(sessionId));
            if (captchaFetchResult == null || captchaFetchResult.imageBytes == null || captchaFetchResult.imageBytes.length == 0) {
                return "";
            }
            if (captchaFetchResult.cookie != null && !captchaFetchResult.cookie.isEmpty()) {
                veccCookieMap.put(sessionId, captchaFetchResult.cookie);
            }
            return toDataImageBase64(captchaFetchResult.imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static class QueryResult {
        public int code;
        public String msg;
        public String currentUrl;
        public byte[] screenshotPng;
        public String captchaBase64;
        public String html;
    }

    private static void ensureChromeDriverProperty() {
        String existing = System.getProperty("webdriver.chrome.driver");
        if (existing != null && !existing.trim().isEmpty()) {
            return;
        }
        try {
            File fixed = new File("D:\\chromedriver.exe");
            if (fixed.exists()) {
                System.setProperty("webdriver.chrome.driver", fixed.getAbsolutePath());
                return;
            }
            File local = new File("chromedriver.exe");
            if (local.exists()) {
                System.setProperty("webdriver.chrome.driver", local.getAbsolutePath());
            }
        } catch (Exception ignored) {
        }
    }

    private static String tryGetAndAcceptAlert(WebDriver driver, int seconds) {
        long endAt = System.currentTimeMillis() + seconds * 1000L;
        while (System.currentTimeMillis() < endAt) {
            try {
                org.openqa.selenium.Alert alert = driver.switchTo().alert();
                String text = alert.getText();
                alert.accept();
                return text;
            } catch (NoAlertPresentException e) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private static WebElement findCaptchaElement(WebDriver driver) {
        List<WebElement> candidates = driver.findElements(By.xpath(
            "//*[self::img or self::canvas][" +
                "contains(@src,'captcha') or contains(@src,'Captcha') or contains(@src,'verify') or contains(@src,'Verify') or " +
                "contains(@src,'kaptcha') or contains(@src,'Kaptcha') or " +
                "contains(@class,'captcha') or contains(@id,'captcha') or contains(@id,'verify') or " +
                "@id='vaild' or contains(@id,'vaild') or " +
                "contains(@aria-label,'验证码') or contains(@alt,'验证码')" +
            "]"
        ));
        WebElement exact = null;
        try {
            exact = driver.findElement(By.id("vaild"));
        } catch (Exception ignored) {
        }
        if (exact != null) {
            return exact;
        }
        WebElement best = pickBestCaptchaCandidate(candidates);
        if (best != null) {
            return best;
        }

        candidates = driver.findElements(By.xpath("//label[contains(normalize-space(.),'验证码')]/following::*[self::img or self::canvas][1]"));
        best = pickBestCaptchaCandidate(candidates);
        if (best != null) {
            return best;
        }

        candidates = driver.findElements(By.xpath("//*[self::img or self::canvas]"));
        return pickBestCaptchaCandidate(candidates);
    }

    private static WebElement pickBestCaptchaCandidate(List<WebElement> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        WebElement best = null;
        int bestScore = -1;
        for (WebElement el : candidates) {
            try {
                if (el == null || !el.isDisplayed()) {
                    continue;
                }
                int w = el.getSize().getWidth();
                int h = el.getSize().getHeight();
                if (w <= 0 || h <= 0) {
                    continue;
                }
                int score = 0;
                if (w >= 50 && w <= 250) {
                    score += 2;
                }
                if (h >= 20 && h <= 120) {
                    score += 2;
                }
                if ("canvas".equalsIgnoreCase(el.getTagName())) {
                    score += 1;
                }
                if ("img".equalsIgnoreCase(el.getTagName())) {
                    score += 1;
                }
                if (score > bestScore) {
                    bestScore = score;
                    best = el;
                }
            } catch (Exception ignored) {
            }
        }
        return best;
    }

    private static WebElement findVinInput(WebDriver driver) {
        List<WebElement> inputs = driver.findElements(By.xpath("//input[(contains(@placeholder,'VIN') or contains(@placeholder,'车架') or contains(@placeholder,'后6位') or contains(@name,'vin') or contains(@id,'vin')) and (not(@type) or @type='text')]"));
        if (inputs != null && !inputs.isEmpty()) {
            return inputs.get(0);
        }
        return null;
    }

    private static WebElement findEngineInput(WebDriver driver) {
        List<WebElement> inputs = driver.findElements(By.xpath("//input[(contains(@placeholder,'发动机') or contains(@name,'engine') or contains(@id,'engine')) and (not(@type) or @type='text')]"));
        if (inputs != null && !inputs.isEmpty()) {
            return inputs.get(0);
        }
        return null;
    }

    private static WebElement findCaptchaInput(WebDriver driver) {
        List<WebElement> inputs = driver.findElements(By.xpath("//input[(contains(@placeholder,'验证码') or contains(@name,'captcha') or contains(@id,'captcha') or @name='vaild' or @id='vaild') and (not(@type) or @type='text')]"));
        if (inputs != null && !inputs.isEmpty()) {
            return inputs.get(0);
        }
        return null;
    }

    private static WebElement findQueryButton(WebDriver driver) {
        List<WebElement> buttons = driver.findElements(By.xpath("//button[contains(.,'查询') or contains(.,'校验') or contains(.,'提交') or @type='submit']|//input[@type='submit']"));
        if (buttons != null && !buttons.isEmpty()) {
            return buttons.get(0);
        }
        return null;
    }

    private static String safeCaptchaBase64(WebDriver driver) {
        try {
            WebElement captchaEl = findCaptchaElement(driver);
            if (captchaEl == null) {
                return "";
            }
            byte[] imgBytes = getCaptchaImageBytes(driver, captchaEl);
            if (imgBytes == null || imgBytes.length == 0) {
                return "";
            }
            return toDataImageBase64(imgBytes);
        } catch (Exception e) {
            return "";
        }
    }

    private static byte[] screenshotElementPng(WebDriver driver, WebElement element) {
        try {
            if (driver instanceof JavascriptExecutor) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center',inline:'center'});", element);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception ignored) {
        }
        return element.getScreenshotAs(OutputType.BYTES);
    }

    private static byte[] getCaptchaImageBytes(WebDriver driver, WebElement captchaEl) {
        if (captchaEl == null) {
            return null;
        }
        try {
            String tag = captchaEl.getTagName();
            if ("img".equalsIgnoreCase(tag)) {
                String src = safeGetAttribute(captchaEl, "src");
                if (src == null || src.trim().isEmpty()) {
                    return screenshotElementPng(driver, captchaEl);
                }
                String abs = toAbsoluteUrl(driver.getCurrentUrl(), src);
                if (abs == null || abs.trim().isEmpty()) {
                    return screenshotElementPng(driver, captchaEl);
                }
                return httpGetBytes(abs, driver.manage().getCookies());
            }
        } catch (Exception ignored) {
        }
        try {
            return screenshotElementPng(driver, captchaEl);
        } catch (Exception e) {
            return null;
        }
    }

    private static String toDataImageBase64(byte[] bytes) {
        String mime = sniffImageMime(bytes);
        return "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(bytes);
    }

    private static String sniffImageMime(byte[] bytes) {
        if (bytes == null || bytes.length < 4) {
            return "image/jpeg";
        }
        if ((bytes[0] & 0xFF) == 0x89 && (bytes[1] & 0xFF) == 0x50 && (bytes[2] & 0xFF) == 0x4E && (bytes[3] & 0xFF) == 0x47) {
            return "image/png";
        }
        return "image/jpeg";
    }

    private static String safeGetAttribute(WebElement el, String name) {
        try {
            return el.getAttribute(name);
        } catch (Exception e) {
            return null;
        }
    }

    private static void waitCaptchaSrcChanged(WebDriver driver, WebElement captchaEl, String oldSrc, int timeoutMs) {
        long endAt = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < endAt) {
            try {
                String now = safeGetAttribute(captchaEl, "src");
                if (now != null && !now.equals(oldSrc) && !now.trim().isEmpty()) {
                    return;
                }
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static String toAbsoluteUrl(String currentUrl, String src) {
        try {
            URL base = new URL(currentUrl);
            URL abs = new URL(base, src);
            return abs.toString();
        } catch (Exception e) {
            return src;
        }
    }

    private static byte[] httpGetBytes(String url, Set<Cookie> cookies) {
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            String cookieHeader = buildCookieHeader(cookies);
            if (cookieHeader != null && !cookieHeader.isEmpty()) {
                conn.setRequestProperty("Cookie", cookieHeader);
            }
            conn.connect();
            is = conn.getInputStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ignored) {
            }
            try {
                baos.close();
            } catch (Exception ignored) {
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static String buildCookieHeader(Set<Cookie> cookies) {
        if (cookies == null || cookies.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Cookie c : cookies) {
            if (c == null) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(c.getName()).append("=").append(c.getValue());
        }
        return sb.toString();
    }

    public QueryResult queryHttp(String sessionId, String vin, String engineLast6, String captcha) {
        QueryResult result = new QueryResult();
        String cookie = veccCookieMap.get(sessionId);
        if (cookie == null || cookie.isEmpty()) {
            result.code = 500;
            result.msg = "验证码会话已过期，请刷新验证码";
            result.captchaBase64 = refreshCaptcha(sessionId);
            return result;
        }
        try {
            HttpPageResult pageResult = postVeccQueryHtml(cookie, vin, engineLast6, captcha);
            if (pageResult != null && pageResult.cookie != null && !pageResult.cookie.isEmpty()) {
                veccCookieMap.put(sessionId, pageResult.cookie);
            }
            String html = pageResult == null ? "" : pageResult.html;
            String alertMsg = extractAlertMessage(html);
            if (alertMsg != null && !alertMsg.isEmpty()) {
                result.code = 400;
                result.msg = alertMsg;
                result.captchaBase64 = refreshCaptcha(sessionId);
                result.currentUrl = pageResult == null ? TARGET_URL : pageResult.finalUrl;
                return result;
            }
            if (html == null || html.trim().isEmpty()) {
                result.code = 500;
                result.msg = "查询结果为空，请重试";
                result.captchaBase64 = refreshCaptcha(sessionId);
                result.currentUrl = pageResult == null ? TARGET_URL : pageResult.finalUrl;
                return result;
            }
            String finalUrl = pageResult == null ? TARGET_URL : pageResult.finalUrl;
            QueryResult fastResult = tryFastPathQuery(finalUrl, pageResult == null ? cookie : pageResult.cookie, vin, engineLast6, html);
            if (fastResult != null) {
                fastResult.currentUrl = finalUrl;
                return fastResult;
            }
            if (isLoadingPlaceholderPage(html) || (finalUrl != null && !finalUrl.contains("/ve/vin/index"))) {
                BrowserCaptureResult captureResult = captureRenderedPageByBrowser(finalUrl, pageResult == null ? cookie : pageResult.cookie);
                if (captureResult != null && captureResult.loaded && captureResult.screenshotPng != null && captureResult.screenshotPng.length > 0) {
                    result.code = 200;
                    result.msg = "OK";
                    result.currentUrl = finalUrl;
                    result.screenshotPng = captureResult.screenshotPng;
                    result.html = captureResult.pageSource == null || captureResult.pageSource.trim().isEmpty() ? html : captureResult.pageSource;
                    return result;
                }
                result.code = 500;
                result.msg = captureResult != null && captureResult.errorMsg != null && !captureResult.errorMsg.trim().isEmpty()
                    ? captureResult.errorMsg
                    : "查询结果页仍在加载，请稍后重试";
                result.currentUrl = finalUrl;
                return result;
            }
            result.code = 200;
            result.msg = "OK";
            result.currentUrl = finalUrl;
            result.html = html;
            return result;
        } catch (Exception e) {
            result.code = 500;
            result.msg = "查询异常：" + e.getMessage();
            result.captchaBase64 = refreshCaptcha(sessionId);
            return result;
        }
    }

    private static QueryResult tryFastPathQuery(String finalUrl, String cookie, String vin, String engineLast6, String fallbackHtml) {
        if (finalUrl == null || !finalUrl.contains("xxgk.vecc.org.cn")) {
            return null;
        }
        try {
            String resultHtml = tryBuildScqdHtmlByApi(finalUrl, cookie, vin, engineLast6);
            if (resultHtml == null || resultHtml.trim().isEmpty()) {
                return null;
            }
            QueryResult result = new QueryResult();
            result.code = 200;
            result.msg = "OK";
            result.html = resultHtml;
            result.screenshotPng = null;
            return result;
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String tryBuildScqdHtmlByApi(String finalUrl, String cookie, String vin, String engineLast6) throws Exception {
        String apiId = extractScqdId(finalUrl);
        if (apiId == null || apiId.trim().isEmpty()) {
            String detailUrl = fetchScqdDetailUrl(finalUrl, cookie, vin, engineLast6);
            apiId = extractScqdId(detailUrl);
        }
        if (apiId == null || apiId.trim().isEmpty()) {
            return "";
        }
        String json = getHtml("http://xxgk.vecc.org.cn/prod-api/server/getVinInfo/" + URLEncoder.encode(apiId, "UTF-8"), cookie, finalUrl);
        return buildScqdHtmlFromJson(json);
    }

    private static String fetchScqdDetailUrl(String finalUrl, String cookie, String vin, String engineLast6) throws Exception {
        if (vin == null || vin.trim().isEmpty() || engineLast6 == null || engineLast6.trim().isEmpty()) {
            return "";
        }
        String apiUrl = "http://xxgk.vecc.org.cn/prod-api/server/checkFdj/?vin="
            + URLEncoder.encode(vin.trim(), "UTF-8")
            + "&fdjh=" + URLEncoder.encode(engineLast6.trim(), "UTF-8");
        String json = getHtml(apiUrl, cookie, finalUrl);
        if (json == null || json.trim().isEmpty()) {
            return "";
        }
        JSONObject root = JSONObject.parseObject(json);
        Integer status = root.getInteger("status");
        if (status == null || status.intValue() != 200) {
            return "";
        }
        Object data = root.get("data");
        return data == null ? "" : String.valueOf(data);
    }

    private static String extractScqdId(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "";
        }
        try {
            String clean = url;
            int q = clean.indexOf('?');
            if (q >= 0) {
                clean = clean.substring(0, q);
            }
            while (clean.endsWith("/")) {
                clean = clean.substring(0, clean.length() - 1);
            }
            int idx = clean.lastIndexOf('/');
            if (idx >= 0 && idx + 1 < clean.length()) {
                return clean.substring(idx + 1);
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    private static String buildScqdHtmlFromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return "";
        }
        try {
            JSONObject root = JSONObject.parseObject(json);
            JSONObject dataObj = extractPrimaryDataObject(root);
            if (dataObj == null || dataObj.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            sb.append("<style>");
            sb.append("body{font-family:'Microsoft YaHei',sans-serif;background:#fff;color:#222;margin:0;padding:16px;}");
            sb.append(".wrap{max-width:1180px;margin:0 auto;}");
            sb.append(".title{font-size:28px;font-weight:700;text-align:center;margin:0 0 10px 0;}");
            sb.append(".sub{font-size:14px;color:#666;text-align:center;margin:0 0 20px 0;}");
            sb.append("table{width:100%;border-collapse:collapse;table-layout:fixed;}");
            sb.append("td{border:1px solid #d9d9d9;padding:10px 12px;vertical-align:top;font-size:16px;line-height:1.5;word-break:break-all;}");
            sb.append("td.k{width:28%;background:#f7f7f7;font-weight:600;}");
            sb.append("</style></head><body><div class='wrap'>");
            sb.append("<div class='title'>随车清单</div>");
            sb.append("<div class='sub'>车辆信息公众查询结果</div>");
            sb.append("<table>");
            for (Map.Entry<String, Object> entry : dataObj.entrySet()) {
                String key = normalizeApiKey(entry.getKey());
                String value = stringifyApiValue(entry.getValue());
                if (value == null || value.trim().isEmpty()) {
                    continue;
                }
                sb.append("<tr><td class='k'>")
                    .append(escapeHtml(key))
                    .append("</td><td>")
                    .append(escapeHtml(value))
                    .append("</td></tr>");
            }
            sb.append("</table></div></body></html>");
            return sb.toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    private static JSONObject extractPrimaryDataObject(JSONObject root) {
        if (root == null || root.isEmpty()) {
            return null;
        }
        Object data = root.get("data");
        if (data instanceof JSONObject) {
            return (JSONObject) data;
        }
        if (data instanceof JSONArray) {
            JSONArray arr = (JSONArray) data;
            if (!arr.isEmpty() && arr.get(0) instanceof JSONObject) {
                return arr.getJSONObject(0);
            }
        }
        JSONObject flat = new JSONObject();
        for (Map.Entry<String, Object> entry : root.entrySet()) {
            String key = entry.getKey();
            if ("status".equalsIgnoreCase(key) || "code".equalsIgnoreCase(key) || "message".equalsIgnoreCase(key) || "msg".equalsIgnoreCase(key)) {
                continue;
            }
            Object value = entry.getValue();
            if (value instanceof JSONObject || value instanceof JSONArray || value instanceof String || value instanceof Number || value instanceof Boolean) {
                flat.put(key, value);
            }
        }
        return flat.isEmpty() ? null : flat;
    }

    private static String stringifyApiValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof JSONArray) {
            JSONArray arr = (JSONArray) value;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                if (i > 0) {
                    sb.append("；");
                }
                sb.append(stringifyApiValue(arr.get(i)));
            }
            return sb.toString();
        }
        if (value instanceof JSONObject) {
            JSONObject obj = (JSONObject) value;
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("；");
                }
                sb.append(normalizeApiKey(entry.getKey())).append("：").append(stringifyApiValue(entry.getValue()));
            }
            return sb.toString();
        }
        return String.valueOf(value);
    }

    private static String normalizeApiKey(String key) {
        if (key == null) {
            return "";
        }
        return key.replace('_', ' ').trim();
    }

    private static String escapeHtml(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }

    private static class CaptchaFetchResult {
        private String cookie;
        private byte[] imageBytes;
    }

    private static class HttpPageResult {
        private String cookie;
        private String finalUrl;
        private String html;
    }

    private static class BrowserCaptureResult {
        private boolean loaded;
        private String errorMsg;
        private String pageSource;
        private byte[] screenshotPng;
    }

    private static CaptchaFetchResult fetchCaptchaFromVecc(String cookie) throws Exception {
        String url = "https://info.vecc.org.cn/ve/kaptcha.jpg?t=" + Math.random();
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(CAPTCHA_CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(CAPTCHA_READ_TIMEOUT_MS);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            conn.setRequestProperty("Referer", "https://info.vecc.org.cn/ve/vin/index");
            if (cookie != null && !cookie.isEmpty()) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.connect();
            String setCookie = conn.getHeaderField("Set-Cookie");
            is = conn.getInputStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            CaptchaFetchResult ret = new CaptchaFetchResult();
            ret.cookie = mergeCookie(cookie, setCookie);
            ret.imageBytes = baos.toByteArray();
            return ret;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ignored) {
            }
            try {
                baos.close();
            } catch (Exception ignored) {
            }
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static HttpPageResult postVeccQueryHtml(String cookie, String vin, String engineLast6, String captcha) throws Exception {
        String postUrl = "https://info.vecc.org.cn/ve/vin/index";
        String body = "vin=" + URLEncoder.encode(vin, "UTF-8") +
            "&fdjh=" + URLEncoder.encode(engineLast6, "UTF-8") +
            "&vaild=" + URLEncoder.encode(captcha, "UTF-8");

        HttpURLConnection conn = null;
        HttpPageResult result = new HttpPageResult();
        result.cookie = cookie;
        result.finalUrl = postUrl;
        try {
            conn = (HttpURLConnection) new URL(postUrl).openConnection();
            conn.setConnectTimeout(QUERY_CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(QUERY_READ_TIMEOUT_MS);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            conn.setRequestProperty("Referer", "https://info.vecc.org.cn/ve/vin/index");
            if (cookie != null && !cookie.isEmpty()) {
                conn.setRequestProperty("Cookie", cookie);
            }
            byte[] postBytes = body.getBytes(StandardCharsets.UTF_8);
            conn.getOutputStream().write(postBytes);
            conn.getOutputStream().flush();
            int code = conn.getResponseCode();
            String setCookie = conn.getHeaderField("Set-Cookie");
            if (setCookie != null && !setCookie.isEmpty()) {
                result.cookie = mergeCookie(cookie, setCookie);
            }
            if (isRedirect(code)) {
                String location = conn.getHeaderField("Location");
                String abs = toAbsoluteUrl(postUrl, location);
                result.finalUrl = abs;

                String html = getHtml(abs, result.cookie, postUrl);
                result.html = html;
                if (result.html == null || result.html.trim().isEmpty()) {
                    // 兜底：部分场景重定向后再跳首页，先取结果页失败时再取首页
                    result.html = getHtml(TARGET_URL, result.cookie, abs);
                    result.finalUrl = TARGET_URL;
                }
                return result;
            }
            result.finalUrl = conn.getURL() == null ? postUrl : conn.getURL().toString();
            result.html = readHtml(conn);
            if (result.html == null || result.html.trim().isEmpty()) {
                result.html = getHtml(result.finalUrl, result.cookie, postUrl);
            }
            return result;
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static boolean isRedirect(int code) {
        return code == 301 || code == 302 || code == 303 || code == 307 || code == 308;
    }

    private static String getHtml(String url, String cookie, String referer) throws Exception {
        Thread.sleep(3000);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(QUERY_CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(QUERY_READ_TIMEOUT_MS);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
            if (referer != null && !referer.isEmpty()) {
                conn.setRequestProperty("Referer", referer);
            }
            if (cookie != null && !cookie.isEmpty()) {
                conn.setRequestProperty("Cookie", cookie);
            }
            return readHtml(conn);
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static String readHtml(HttpURLConnection conn) throws Exception {
        if (conn == null) {
            return "";
        }
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            try {
                is = conn.getInputStream();
            } catch (Exception e) {
                is = conn.getErrorStream();
            }
            if (is == null) {
                return "";
            }
            byte[] buf = new byte[8192];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            byte[] bodyBytes = baos.toByteArray();
            if (bodyBytes.length == 0) {
                return "";
            }
            String contentType = conn.getHeaderField("Content-Type");
            String charset = parseCharset(contentType);
            if (charset == null || charset.isEmpty()) {
                charset = detectCharsetFromMeta(bodyBytes);
            }
            if (charset == null || charset.isEmpty()) {
                charset = "UTF-8";
            }
            return decodeHtmlBestEffort(bodyBytes, charset);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ignored) {
            }
            try {
                baos.close();
            } catch (Exception ignored) {
            }
        }
    }

    private static String parseCharset(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return "";
        }
        Matcher m = Pattern.compile("charset\\s*=\\s*([^;\\s]+)", Pattern.CASE_INSENSITIVE).matcher(contentType);
        if (m.find()) {
            String cs = m.group(1).trim();
            cs = cs.replace("\"", "").replace("'", "");
            return cs;
        }
        return "";
    }

    private static String detectCharsetFromMeta(byte[] bodyBytes) {
        try {
            String iso = new String(bodyBytes, StandardCharsets.ISO_8859_1);
            Matcher m = Pattern.compile("<meta[^>]+charset\\s*=\\s*['\\\"]?([^'\\\"\\s/>]+)", Pattern.CASE_INSENSITIVE).matcher(iso);
            if (m.find()) {
                return m.group(1).trim();
            }
            m = Pattern.compile("content\\s*=\\s*['\\\"][^'\\\"]*charset\\s*=\\s*([^'\\\";\\s]+)", Pattern.CASE_INSENSITIVE).matcher(iso);
            if (m.find()) {
                return m.group(1).trim();
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    private static String decodeHtmlBestEffort(byte[] bodyBytes, String preferredCharset) {
        String[] candidates = new String[] {preferredCharset, "UTF-8", "GBK", "GB2312"};
        String best = "";
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < candidates.length; i++) {
            String cs = candidates[i];
            if (cs == null || cs.trim().isEmpty()) {
                continue;
            }
            String decoded;
            try {
                decoded = new String(bodyBytes, cs);
            } catch (Exception e) {
                continue;
            }
            int score = scoreDecodedText(decoded);
            if (score > bestScore) {
                bestScore = score;
                best = decoded;
            }
        }
        return best == null ? "" : best;
    }

    private static int scoreDecodedText(String s) {
        if (s == null || s.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        int replacement = 0;
        int cjk = 0;
        int latinNoise = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (ch == '\uFFFD') {
                replacement++;
            } else if (ch >= '\u4e00' && ch <= '\u9fa5') {
                cjk++;
            } else if (ch == 'Ã' || ch == 'Â' || ch == '¤' || ch == '�') {
                latinNoise++;
            }
        }
        int score = 0;
        score += cjk * 5;
        score -= replacement * 20;
        score -= latinNoise * 3;
        if (s.indexOf("DOCTYPE") >= 0 || s.indexOf("<html") >= 0) {
            score += 10;
        }
        return score;
    }

    private static String mergeCookie(String oldCookie, String setCookie) {
        String js = extractCookieValue(setCookie, "JSESSIONID");
        if (js == null || js.isEmpty()) {
            return oldCookie == null ? "" : oldCookie;
        }
        return "JSESSIONID=" + js;
    }

    private static String extractCookieValue(String setCookie, String key) {
        if (setCookie == null || setCookie.isEmpty()) {
            return "";
        }
        Pattern p = Pattern.compile(key + "=([^;]+)");
        Matcher m = p.matcher(setCookie);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private static String extractAlertMessage(String html) {
        if (html == null || html.isEmpty()) {
            return "";
        }
        Pattern p = Pattern.compile("alert\\('([^']*)'\\)");
        Matcher m = p.matcher(html);
        if (m.find()) {
            return decodeUnicodeEscapes(m.group(1));
        }
        p = Pattern.compile("alert\\(\"([^\"]*)\"\\)");
        m = p.matcher(html);
        if (m.find()) {
            return decodeUnicodeEscapes(m.group(1));
        }
        return "";
    }

    private static String decodeUnicodeEscapes(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 5 < len && s.charAt(i + 1) == 'u') {
                String hex = s.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                    i += 5;
                    continue;
                } catch (Exception ignored) {
                }
            }
            out.append(c);
        }
        return out.toString();
    }

    private static boolean isLoadingPlaceholderPage(String html) {
        if (html == null || html.trim().isEmpty()) {
            return false;
        }
        String lower = html.toLowerCase();
        return lower.contains("loader-wrapper")
            || lower.contains("load_title")
            || lower.contains("static/js/app.")
            || html.contains("正在加载资源，请耐心等待")
            || html.contains("请耐心等待");
    }

    private static BrowserCaptureResult captureRenderedPageByBrowser(String url, String cookieHeader) {
        BrowserCaptureResult result = captureRenderedPageByBrowser(url, cookieHeader, true);
        if (result != null && !result.loaded && isThreatBlockedPage(result.pageSource)) {
            BrowserCaptureResult retry = captureRenderedPageByBrowser(url, cookieHeader, false);
            if (retry != null && (retry.loaded || !isThreatBlockedPage(retry.pageSource))) {
                return retry;
            }
            if (retry != null) {
                return retry;
            }
        }
        return result;
    }

    private static BrowserCaptureResult captureRenderedPageByBrowser(String url, String cookieHeader, boolean headless) {
        BrowserCaptureResult result = new BrowserCaptureResult();
        if (url == null || url.trim().isEmpty()) {
            result.errorMsg = "结果页地址为空";
            return result;
        }
        WebDriver driver = null;
        try {
            ensureChromeDriverProperty();
            driver = new ChromeDriver(buildChromeOptions(headless));
            driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            try {
                driver.get(TARGET_URL);
                applyCookiesToDriver(driver, cookieHeader);
                hideAutomationMarkers(driver);
            } catch (Exception ignored) {
            }

            driver.navigate().to(url);
            hideAutomationMarkers(driver);
            result.pageSource = safeGetPageSource(driver);
            if (isThreatBlockedPage(result.pageSource)) {
                result.errorMsg = "目标站点风控拦截(403)";
                return result;
            }
            if (!waitUntilRendered(driver, RENDER_WAIT_TIMEOUT_MS)) {
                result.pageSource = safeGetPageSource(driver);
                if (looksLikeRenderedResult(result.pageSource)) {
                    result.loaded = true;
                    result.screenshotPng = fullPageScreenshot(driver);
                    return result;
                }
                result.errorMsg = "结果页加载超时";
                return result;
            }

            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            result.loaded = true;
            result.pageSource = safeGetPageSource(driver);
            result.screenshotPng = fullPageScreenshot(driver);
            return result;
        } catch (Exception e) {
            result.errorMsg = "浏览器渲染失败：" + e.getMessage();
            result.pageSource = safeGetPageSource(driver);
            return result;
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1600,3000");
        options.addArguments("--lang=zh-CN");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--user-agent=" + CHROME_DESKTOP_UA);
        ArrayList<String> excludeSwitches = new ArrayList<String>();
        excludeSwitches.add("enable-automation");
        excludeSwitches.add("enable-logging");
        options.setExperimentalOption("excludeSwitches", excludeSwitches);
        options.setExperimentalOption("useAutomationExtension", false);
        return options;
    }

    private static void hideAutomationMarkers(WebDriver driver) {
        if (!(driver instanceof JavascriptExecutor)) {
            return;
        }
        try {
            ((JavascriptExecutor) driver).executeScript(
                "try{" +
                    "Object.defineProperty(navigator,'webdriver',{get:function(){return undefined;}});" +
                    "Object.defineProperty(navigator,'platform',{get:function(){return 'Win32';}});" +
                    "Object.defineProperty(navigator,'language',{get:function(){return 'zh-CN';}});" +
                    "Object.defineProperty(navigator,'languages',{get:function(){return ['zh-CN','zh','en-US','en'];}});" +
                "}catch(e){}"
            );
        } catch (Exception ignored) {
        }
    }

    private static void applyCookiesToDriver(WebDriver driver, String cookieHeader) {
        if (driver == null || cookieHeader == null || cookieHeader.trim().isEmpty()) {
            return;
        }
        String[] parts = cookieHeader.split(";");
        ArrayList<Cookie> cookies = new ArrayList<Cookie>();
        for (String part : parts) {
            if (part == null) {
                continue;
            }
            String s = part.trim();
            int idx = s.indexOf('=');
            if (idx <= 0) {
                continue;
            }
            String name = s.substring(0, idx).trim();
            String value = s.substring(idx + 1).trim();
            if (name.isEmpty()) {
                continue;
            }
            cookies.add(new Cookie(name, value, "info.vecc.org.cn", "/", null));
        }
        for (Cookie c : cookies) {
            try {
                driver.manage().addCookie(c);
            } catch (Exception ignored) {
            }
        }
    }

    private static boolean waitUntilRendered(WebDriver driver, int timeoutMs) {
        long endAt = System.currentTimeMillis() + timeoutMs;
        int stableCount = 0;
        long lastTextLen = -1;
        long lastHtmlLen = -1;
        while (System.currentTimeMillis() < endAt) {
            try {
                Object state = ((JavascriptExecutor) driver).executeScript("return document.readyState;");
                boolean domReady = state != null && "complete".equalsIgnoreCase(String.valueOf(state));
                Object info = ((JavascriptExecutor) driver).executeScript(
                    "var body=document.body||document.documentElement;" +
                    "var txt=''; try{txt=(body.innerText||'').trim();}catch(e){}" +
                    "var html=''; try{html=(body.innerHTML||'');}catch(e){}" +
                    "var bodyCls=''; try{bodyCls=document.body&&document.body.className?document.body.className:'';}catch(e){}" +
                    "var loader=document.getElementById('loader-wrapper');" +
                    "var hidden=false;" +
                    "if(!loader){hidden=true;} else {" +
                    "  var s=null; try{s=window.getComputedStyle(loader);}catch(e){}" +
                    "  hidden=!!(s && (s.display==='none' || s.visibility==='hidden' || s.opacity==='0'));" +
                    "  if(!hidden && document.body && document.body.classList){hidden=document.body.classList.contains('loaded');}" +
                    "}" +
                    "var app=document.getElementById('app');" +
                    "var appTxt=''; try{appTxt=(app&&app.innerText||'').trim();}catch(e){}" +
                    "var hasTable=false; try{hasTable=!!document.querySelector('table,.el-table,.el-descriptions,.el-card');}catch(e){}" +
                    "var hasContentNode=false; try{hasContentNode=!!document.querySelector('.el-row,.el-col,.el-form,.el-form-item,.app-main,.main-container');}catch(e){}" +
                    "var titleHit=html.indexOf('随车清单')>=0 || html.indexOf('信息公开')>=0 || html.indexOf('车辆型号')>=0;" +
                    "return [hidden, txt.length, html.length, html.indexOf('loader-wrapper')>=0, hasTable, appTxt.length, hasContentNode, bodyCls, titleHit];"
                );
                if (info instanceof List) {
                    List vals = (List) info;
                    boolean loaderGone = vals.size() > 0 && Boolean.TRUE.equals(vals.get(0));
                    long textLen = vals.size() > 1 && vals.get(1) instanceof Number ? ((Number) vals.get(1)).longValue() : 0;
                    long htmlLen = vals.size() > 2 && vals.get(2) instanceof Number ? ((Number) vals.get(2)).longValue() : 0;
                    boolean stillHasLoaderMarkup = vals.size() > 3 && Boolean.TRUE.equals(vals.get(3));
                    boolean hasTable = vals.size() > 4 && Boolean.TRUE.equals(vals.get(4));
                    long appTextLen = vals.size() > 5 && vals.get(5) instanceof Number ? ((Number) vals.get(5)).longValue() : 0;
                    boolean hasContentNode = vals.size() > 6 && Boolean.TRUE.equals(vals.get(6));
                    String bodyCls = vals.size() > 7 && vals.get(7) != null ? String.valueOf(vals.get(7)) : "";
                    boolean titleHit = vals.size() > 8 && Boolean.TRUE.equals(vals.get(8));

                    boolean bodyLoaded = bodyCls.contains("loaded");
                    boolean hasRealContent = textLen > 80 || appTextLen > 50 || hasTable || hasContentNode || titleHit;
                    boolean pageStable = (textLen > 0 && textLen == lastTextLen) || (htmlLen > 0 && htmlLen == lastHtmlLen);

                    if (domReady && hasRealContent && (loaderGone || bodyLoaded || !stillHasLoaderMarkup)) {
                        if (pageStable) {
                            stableCount++;
                        } else {
                            stableCount = 0;
                            lastTextLen = textLen;
                            lastHtmlLen = htmlLen;
                        }
                        if (stableCount >= 2 || hasTable || appTextLen > 120) {
                            return true;
                        }
                    } else if (domReady && hasRealContent && pageStable) {
                        stableCount++;
                        if (stableCount >= 3) {
                            return true;
                        }
                    } else {
                        lastTextLen = textLen;
                        lastHtmlLen = htmlLen;
                        stableCount = 0;
                    }
                }
            } catch (Exception ignored) {
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private static String safeGetPageSource(WebDriver driver) {
        if (driver == null) {
            return "";
        }
        try {
            return driver.getPageSource();
        } catch (Exception ignored) {
            return "";
        }
    }

    private static boolean looksLikeRenderedResult(String html) {
        if (html == null || html.trim().isEmpty()) {
            return false;
        }
        String lower = html.toLowerCase();
        if (lower.contains("正在加载资源，请耐心等待") || lower.contains("loader-section section-left")) {
            return false;
        }
        return html.contains("随车清单")
            || html.contains("信息公开")
            || html.contains("车辆型号")
            || html.contains("<table")
            || html.contains("el-table")
            || html.contains("el-form-item");
    }

    private static boolean isThreatBlockedPage(String html) {
        if (html == null || html.trim().isEmpty()) {
            return false;
        }
        return html.contains("403")
            && (html.contains("已被拦截") || html.contains("您的请求可能存在威胁") || html.toLowerCase().contains("forbidden"));
    }

    private static byte[] fullPageScreenshot(WebDriver driver) throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Number totalHeightNum = (Number) js.executeScript("return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);");
        Number viewportHeightNum = (Number) js.executeScript("return window.innerHeight;");
        long totalHeight = totalHeightNum == null ? 0 : totalHeightNum.longValue();
        long viewportHeight = viewportHeightNum == null ? 0 : viewportHeightNum.longValue();
        if (totalHeight <= 0 || viewportHeight <= 0) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        long contentBottom = detectContentBottom(driver);
        if (contentBottom > 0) {
            long expectedHeight = Math.max(viewportHeight, contentBottom + 60);
            if (expectedHeight < totalHeight) {
                totalHeight = expectedHeight;
            }
        }
        if (totalHeight > 8000) {
            totalHeight = 8000;
        }

        int parts = (int) Math.ceil((double) totalHeight / (double) viewportHeight);
        BufferedImage stitched = null;
        int stitchedHeight = 0;

        for (int i = 0; i < parts; i++) {
            long y = Math.min((long) i * viewportHeight, totalHeight - viewportHeight);
            js.executeScript("window.scrollTo(0, arguments[0]);", y);
            Thread.sleep(200);
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(png));
            if (img == null) {
                continue;
            }
            int effectiveHeight = img.getHeight();
            if (i == parts - 1) {
                long remaining = totalHeight - y;
                if (remaining > 0 && remaining < effectiveHeight) {
                    effectiveHeight = (int) remaining;
                    img = img.getSubimage(0, img.getHeight() - effectiveHeight, img.getWidth(), effectiveHeight);
                }
            }

            if (stitched == null) {
                stitched = new BufferedImage(img.getWidth(), (int) totalHeight, BufferedImage.TYPE_INT_RGB);
            }
            Graphics2D g2d = stitched.createGraphics();
            g2d.drawImage(img, 0, stitchedHeight, null);
            g2d.dispose();
            stitchedHeight += effectiveHeight;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (stitched != null) {
            stitched = trimOuterBlank(stitched);
            ImageIO.write(stitched, "png", out);
            return out.toByteArray();
        }
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private static long detectContentBottom(WebDriver driver) {
        try {
            Object val = ((JavascriptExecutor) driver).executeScript(
                "return (function(){" +
                    "var body=document.body||document.documentElement;" +
                    "if(!body){return 0;}" +
                    "var maxBottom=0;" +
                    "var nodes=body.querySelectorAll('*');" +
                    "for(var i=0;i<nodes.length;i++){" +
                    "  var el=nodes[i];" +
                    "  if(!el||!el.tagName){continue;}" +
                    "  var tag=String(el.tagName).toUpperCase();" +
                    "  if(tag==='SCRIPT'||tag==='STYLE'||tag==='LINK'||tag==='META'||tag==='NOSCRIPT'){continue;}" +
                    "  var s=null; try{s=window.getComputedStyle(el);}catch(e){}" +
                    "  if(s&&(s.display==='none'||s.visibility==='hidden'||s.opacity==='0')){continue;}" +
                    "  var rect=el.getBoundingClientRect();" +
                    "  if(!rect||rect.height<4||rect.width<4){continue;}" +
                    "  var text=''; try{text=(el.innerText||el.textContent||'').trim();}catch(e){}" +
                    "  var meaningful=text.length>0||tag==='IMG'||tag==='CANVAS'||tag==='SVG'||tag==='TABLE';" +
                    "  if(!meaningful){continue;}" +
                    "  var bottom=rect.bottom+(window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0);" +
                    "  if(bottom>maxBottom){maxBottom=bottom;}" +
                    "}" +
                    "return Math.ceil(maxBottom);" +
                "})();"
            );
            if (val instanceof Number) {
                return ((Number) val).longValue();
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    private static BufferedImage trimOuterBlank(BufferedImage src) {
        if (src == null || src.getWidth() <= 0 || src.getHeight() <= 0) {
            return src;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        int bg = detectBackgroundColor(src);

        int top = 0;
        while (top < height - 1 && isMostlyBackgroundRow(src, top, bg)) {
            top++;
        }

        int bottom = height - 1;
        while (bottom > top && isMostlyBackgroundRow(src, bottom, bg)) {
            bottom--;
        }

        int left = 0;
        while (left < width - 1 && isMostlyBackgroundColumn(src, left, bg)) {
            left++;
        }

        int right = width - 1;
        while (right > left && isMostlyBackgroundColumn(src, right, bg)) {
            right--;
        }

        if (right - left < width / 5 || bottom - top < height / 5) {
            return src;
        }

        int margin = 10;
        left = Math.max(0, left - margin);
        top = Math.max(0, top - margin);
        right = Math.min(width - 1, right + margin);
        bottom = Math.min(height - 1, bottom + margin);

        int cropWidth = right - left + 1;
        int cropHeight = bottom - top + 1;
        if (cropWidth <= 0 || cropHeight <= 0 || (cropWidth == width && cropHeight == height)) {
            return src;
        }

        BufferedImage cropped = new BufferedImage(cropWidth, cropHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = cropped.createGraphics();
        g2d.drawImage(src, 0, 0, cropWidth, cropHeight, left, top, right + 1, bottom + 1, null);
        g2d.dispose();
        return cropped;
    }

    private static int detectBackgroundColor(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] colors = new int[] {
            img.getRGB(0, 0),
            img.getRGB(width - 1, 0),
            img.getRGB(0, height - 1),
            img.getRGB(width - 1, height - 1)
        };
        int best = colors[0];
        int bestScore = -1;
        for (int i = 0; i < colors.length; i++) {
            int score = 0;
            for (int j = 0; j < colors.length; j++) {
                if (isNearColor(colors[i], colors[j], 16)) {
                    score++;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                best = colors[i];
            }
        }
        return best;
    }

    private static boolean isMostlyBackgroundRow(BufferedImage img, int y, int bg) {
        int width = img.getWidth();
        if (width <= 0) {
            return false;
        }
        int samples = 0;
        int nearBg = 0;
        int step = Math.max(1, width / 200);
        for (int x = 0; x < width; x += step) {
            int rgb = img.getRGB(x, y);
            if (isNearColor(rgb, bg, 10)) {
                nearBg++;
            }
            samples++;
        }
        return samples > 0 && nearBg >= samples * 0.98;
    }

    private static boolean isMostlyBackgroundColumn(BufferedImage img, int x, int bg) {
        int height = img.getHeight();
        if (height <= 0) {
            return false;
        }
        int samples = 0;
        int nearBg = 0;
        int step = Math.max(1, height / 200);
        for (int y = 0; y < height; y += step) {
            int rgb = img.getRGB(x, y);
            if (isNearColor(rgb, bg, 10)) {
                nearBg++;
            }
            samples++;
        }
        return samples > 0 && nearBg >= samples * 0.985;
    }

    private static boolean isNearColor(int c1, int c2, int tolerance) {
        int r1 = (c1 >> 16) & 0xff;
        int g1 = (c1 >> 8) & 0xff;
        int b1 = c1 & 0xff;
        int r2 = (c2 >> 16) & 0xff;
        int g2 = (c2 >> 8) & 0xff;
        int b2 = c2 & 0xff;
        return Math.abs(r1 - r2) <= tolerance
            && Math.abs(g1 - g2) <= tolerance
            && Math.abs(b1 - b2) <= tolerance;
    }
}
