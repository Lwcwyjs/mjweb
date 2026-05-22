package com.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.base.PublicService;
import com.service.businessmanage.PfbzService;
import com.service.configmanage.SysOptionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Cipher;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TestVeptsService {
	@Autowired
	private SysOptionsService optionsService;

	@Autowired
	private PublicService publicService;
	@Autowired
	private PfbzService pfbzService;

	private static String tokenYth = "";
	private static String tokenTimeYth = "";
	// JSON解析器（Jackson）
	private static final ObjectMapper objectMapper = new ObjectMapper();
	// 字符编码常量
	private static final String CHARSET = "UTF-8";

	@Test
	public void testimage() throws IOException {
		String smd5 = DigestUtils.md5Hex(DigestUtils.md5Hex("e8c212114861470b95e7b260c111e300" + "sysadmin" + "Bt@123456"));
		System.out.println(smd5);
	}
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
	}public double getSimilarity(String str1, String str2) {
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
	@Test
	public void tesths() throws IOException {
		String clsbdhex="ZGJLM444SX144167";
		String clsbdh="LZGJLM444SX144167";
		double dxsd=getSimilarity(clsbdhex,clsbdh);
		System.out.println(dxsd);
	}
	@Test
	public void testsq() throws IOException {
		String qybh="1880354058";
		String cojkxlh = DigestUtils.md5Hex(DigestUtils.md5Hex(qybh + "Bdmj"));
		System.out.println(cojkxlh);
	}
	@Test
	public void testupload() throws IOException {
		HttpURLConnection conn = null;
		OutputStream os = null;
		BufferedReader br = null;

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
			if (responseCode != HttpURLConnection.HTTP_OK) {
				System.out.println("请求失败，HTTP状态码: " + responseCode);
				return;
			}

			// 7. 读取响应体
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
			StringBuilder responseBody = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				responseBody.append(line);
			}
			String sRtzb = responseBody.toString();
			System.out.println( sRtzb);

			// 8. 解析JSON响应（和之前逻辑一致）
			JsonNode jRtzb = objectMapper.readTree(sRtzb);
			String rcode = jRtzb.get("code").asText();

			if ("10000".equals(rcode)) {
				// 获取token成功
				tokenTimeYth = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				JsonNode dataNode = jRtzb.get("data");
				tokenYth = dataNode.get("access_token").asText();
				System.out.println("获取token成功:" + tokenYth);
			} else {
				// 获取token失败
				String rmsg = jRtzb.get("msg").asText();
				tokenYth = "";
				System.out.println("获取token失败:" + rmsg);
			}

		} catch (Exception e) {
			// 捕获所有异常（网络、IO、JSON解析等）
			System.out.println("请求/解析异常: " + e.getMessage());
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
	}


	// 字节数组转16进制字符串
	public static String bytesToHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (byte b : bytes) {
			hex.append(String.format("%02X", b)); // 确保两位16进制，不足补0
		}
		return hex.toString();
	}

	// 16进制字符串转字节数组
	public static byte[] hexToBytes(String hexString) {
		int length = hexString.length();
		byte[] bytes = new byte[length / 2];
		for (int i = 0; i < length; i += 2) {
			// 每两位16进制字符转换为一个字节
			bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return bytes;
	}

	// 生成RSA密钥对
	public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(2048); // 2048位密钥
		return keyGen.generateKeyPair();
	}

	// 使用公钥加密
	public static String encryptWithPublicKey(String data, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encrypted); // 加密结果用Base64展示
	}

	// 使用私钥解密
	public static String decryptWithPrivateKey(String encryptedData, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
		return new String(decrypted, "UTF-8");
	}
	@Test
	public void test1() throws Exception {
		// 1. 生成RSA密钥对
		KeyPair keyPair = generateRSAKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		System.out.println("生成密钥对成功");

		// 2. 公钥转换为16进制字符串（用于传输或存储）
		byte[] publicKeyBytes = publicKey.getEncoded(); // X.509编码
		String publicKeyHex = bytesToHex(publicKeyBytes);
		publicKeyHex="04188B4463E4D6B943634923D192A54110B24E95A9E6C40F07323473AFAE55EF32B688FE69166AED812B44AEAC7B73577739016312F605E579CA681E07D1D2AE95";
		System.out.println("公钥（16进制）：" + publicKeyHex);

		// 3. 模拟场景：将16进制公钥还原为PublicKey对象（如接收方收到16进制公钥后）
		byte[] restoredPublicKeyBytes = hexToBytes(publicKeyHex);
		PublicKey restoredPublicKey = KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(restoredPublicKeyBytes));

		// 4. 使用还原的公钥加密数据
		String originalData = "ssXX@250826";
		String encryptedData = encryptWithPublicKey(originalData, restoredPublicKey);
		System.out.println("加密后的数据：" + encryptedData);
		encryptedData="13af6129fa0cfdbe336d915621ddd4c6be37d6ce7a20dc225ba6969801922f91f62bf8af850f803d89d00ecf2c50397fac8dd44d509d2502d92e551e7e21c0c74e82b2531e6beb4482f09ebb23dbc30cae80598b828413f83684dc1e0ed0500b0b18f756310507cd2bfa9ec548710320d928ee14fe960fa433584761c878bafb25916d5b7ed74d5e47bfdf49af5b17966e69b29de1bd6c0bbf59cf1afe007eb3185acb9c0e98a97cb45397a4111118a12c34607ca4b0802c6d2f9d4e4f3ffc25724aadf41b32e155a6e495ac474c8722f389127d55197a381fdc065594ce08cfc9fb1f5ddc2ed7be80e7a49b741f71990c6b1ea8296897ca11c4b11ba7c39aaa";
		// 5. 使用私钥解密
		String decryptedData = decryptWithPrivateKey(encryptedData, privateKey);
		System.out.println("解密后的数据：" + decryptedData);
	}
	@Test
	public void testpf() throws Exception {
		String srt = "";
		String urlStr = "http://81.70.45.19:18090/tVecc/accessControl/get_data"; // 替换为你的URL
		String username = "tvehiclemlistuser";
		String pwd = "4BVcV8nv89stzXCh";
		JSONObject dataJson = new JSONObject();
		dataJson.put("username", username);
		dataJson.put("password", pwd);
		dataJson.put("reqtype","getVehiclemountedList");
		dataJson.put("vin","LVAV2JVB5LE237629");

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
				System.out.println("返回:" + srt);
				JSONObject jsonObject=JSONObject.parseObject(srt);
				String code=jsonObject.getString("code");
				if(code.equals("200")){
					JSONArray bodyArray=jsonObject.getJSONArray("body");
					JSONObject bodyObject = bodyArray.getJSONObject(0);
					String result=bodyObject.getString("result");
					if(result.equals("true")){
						String pf=bodyObject.getString("pf");
						System.out.println(pf);
					}
				}

			} else {
				System.out.println("调用随车清单接口失败");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(srt);
	}
}
