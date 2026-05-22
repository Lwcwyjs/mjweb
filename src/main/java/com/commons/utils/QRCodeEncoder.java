package com.commons.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class QRCodeEncoder {
	
    private static Logger LOGGER = LoggerFactory.getLogger(QRCodeEncoder.class);

  
    /**
     * 创建二维码并将二维码转为base64编码的字符串
     * @param contents
     * @param width
     * @param height
     * @param imgFormat   如bmp jpg等
     * @return
     */
	public String encodeBase64(String contents, int width, int height,String imgFormat) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 指定编码格式
		String qrCodeBase64="";
		try {
			BitMatrix byteMatrix;
			byteMatrix = new MultiFormatWriter().encode(new String(contents.getBytes("UTF-8"), "iso-8859-1"),
					BarcodeFormat.QR_CODE, width, height,hints);
			byteMatrix=deleteWhite(byteMatrix);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(byteMatrix, imgFormat, bao);
			qrCodeBase64 = Base64Code(bao.toByteArray());
			LOGGER.debug("create QR code to base64 success");
		} catch (Exception e) {
			LOGGER.error("create QR code to base64 failed:"+e.getMessage());
		}		
		return qrCodeBase64;
	}
	
	/**
	 * 去除二维码的白边
	 * @param matrix
	 * @return
	 */
	public static BitMatrix deleteWhite(BitMatrix matrix){  
	    int[] rec = matrix.getEnclosingRectangle();  
	    int resWidth = rec[2] + 1;  
	    int resHeight = rec[3] + 1;  
	  
	    BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);  
	    resMatrix.clear();  
	    for (int i = 0; i < resWidth; i++) {  
	        for (int j = 0; j < resHeight; j++) {  
	            if (matrix.get(i + rec[0], j + rec[1]))  
	                resMatrix.set(i, j);  
	        }  
	    }  
	    return resMatrix;  
	} 
	
	public String encodeImage(String contents, int width, int height,String imgPath,String imgFormat)
	{
		try {
			String codeBase64=encodeBase64(contents, width, height,imgFormat);
			createImage(codeBase64, imgPath);	
			LOGGER.debug("create QR code to image success");
		} catch (Exception e) {
			LOGGER.error("create QR code failed:"+e.getMessage());
		}
		return imgPath;
	}

	/**
	 * 编码
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public void encode(String contents, int width, int height, String imgPath) {

		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.MARGIN, 0);
		// 指定编码格式
		try {

			BitMatrix byteMatrix;
			byteMatrix = new MultiFormatWriter().encode(new String(contents.getBytes("UTF-8"), "iso-8859-1"),
					BarcodeFormat.QR_CODE, width, height,hints);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(byteMatrix, "png", bao);
			// System.out.println(bao.toByteArray());
			String ok = Base64Code(bao.toByteArray());
			System.out.println("-----------------------------------------------------------");
			System.out.println(ok);
			System.out.println("--------------------------------------------------------------");
			createImage(ok, imgPath);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public String Base64Code(byte[] b) {
		BASE64Encoder encoder = new BASE64Encoder();
		String codeBase64 = "";
		StringBuilder pictureBuffer = new StringBuilder();
		pictureBuffer.append(encoder.encode(b));
		// System.out.println(pictureBuffer.toString());
		codeBase64 = pictureBuffer.toString();
		return codeBase64;
	}

	public void createImage(String Base64, String imgPath) {
		BASE64Decoder decoder = new BASE64Decoder();
		FileOutputStream write;
		try {
			write = new FileOutputStream(new File(imgPath));
			byte[] decoderBytes = decoder.decodeBuffer(Base64);
			write.write(decoderBytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Decoding the picture Success");
	}

	/**
	 * 
	 * @param args
	 * 
	 */

	public static void main(String[] args) {

		String imgPath = "c:/zxing.bmp";

		String contents = "http://www.baidu.com";

		//String contents = "你好";

		int width = 200, height = 200;

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder();

		//qrCodeEncoder.encode(contents, width, height, imgPath);
		System.out.println(qrCodeEncoder.encodeBase64(contents, width, height,"bmp"));
		
		System.out.println(qrCodeEncoder.encodeImage(contents, width, height, imgPath,"bmp"));
		
		System.out.println("successful");

	}
}