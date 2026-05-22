package com.commons.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

public class VideoUtil {

    public static void main(String args[]) {
        try {
            getGrabberFFmpegImage("/Users/liwqer/Desktop/abc.mp4","/Users/liwqer/Desktop/","aaa");
        }catch (java.lang.Exception e){
            e.printStackTrace();
        }
    }

    public static void getGrabberFFmpegImage(String filePath, String targerFilePath, String targetFileName) throws java.lang.Exception {
        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
        ff.start();
        int ffLength = ff.getLengthInFrames();
        List<Integer> randomGrab = new ArrayList<>();
        randomGrab.add(0, 1);
        int maxRandomGrab = randomGrab.get(randomGrab.size() - 1);
        Frame f;
        int i = 0;
        while (i < ffLength) {
            f = ff.grabImage();
            if (randomGrab.contains(i)) {
                doExecuteFrame(f, targerFilePath, targetFileName);
            }
            if (i >= maxRandomGrab) {
                break;
            }
            i++;
        }
        ff.stop();
    }

    private static void doExecuteFrame(Frame f, String targerFilePath, String targetFileName) {
        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "png";
        String FileName = targerFilePath + File.separator + targetFileName + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(FileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //海康  "rtsp://admin:12345@172.6.22.106:554/Streaming/tracks/101？starttime=20120802t063812z&endtime=20120802t064816z";

    //大华  "rtsp://172.22.0.26:554/cam/playback？channel=1&subtype=0&starttime=2016_04_18_11_50_00"
    public static String generateRTSP(String spxx, Date lxkssj, Date lxjssj) {
        String rtsp = "";
        String[] spxxArr = spxx.split(",");
        switch (spxxArr[0]) {
            //海康设备
            case "1":
                SimpleDateFormat dateSdf_hk = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat timeSdf_hk = new SimpleDateFormat("HHmmss");
                String ksDate = dateSdf_hk.format(lxkssj);
                String ksTime = timeSdf_hk.format(lxkssj);
                String jsDate = dateSdf_hk.format(lxjssj);
                String jsTime = timeSdf_hk.format(lxjssj);
                rtsp = "rtsp://" + spxxArr[4] + ":" + spxxArr[5] + "@" + spxxArr[1] + ":554/Streaming/tracks/" + spxxArr[3] + "01?starttime=" + ksDate + "t" + ksTime + "z&endtime=" + jsDate + "t" + jsTime + "z";
                break;
            case "2":
                SimpleDateFormat sdf_dh = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String kssj_dh = sdf_dh.format(lxkssj);
                String jssj_dh = sdf_dh.format(lxjssj);
                rtsp = "rtsp://" + spxxArr[4] + ":" + spxxArr[5] + "@" + spxxArr[1] + ":554/cam/playback?channel=" + spxxArr[3] + "&subtype=0&starttime=" + kssj_dh + "&endtime=" + jssj_dh;
                break;
            default:
                break;
        }
        return rtsp;
    }

}
