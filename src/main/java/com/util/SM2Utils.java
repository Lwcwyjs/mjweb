package com.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class SM2Utils {
    private static final BouncyCastleProvider bc = new BouncyCastleProvider();
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";

    public SM2Utils() {
    }

    public static Map<String, Object> generateSM2Key() {
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        Map<String, Object> map = new HashMap();
        map.put("publicKey", pair.getPublic());
        map.put("privateKey", pair.getPrivate());
        return map;
    }

    public static String encrypt(String body, PublicKey publicKey) {
        SM2 sm2 = SmUtil.sm2();
        sm2.setPublicKey(publicKey);
        return sm2.encryptBcd(body, KeyType.PublicKey);
    }

    public static String decrypt(String data, PrivateKey privateKey) {
        SM2 sm2 = SmUtil.sm2();
        sm2.setPrivateKey(privateKey);
        return StrUtil.utf8Str(sm2.decryptFromBcd(data, KeyType.PrivateKey));
    }

    public static String sign(PrivateKey privateKey, String content) {
        SM2 sm2 = SmUtil.sm2();
        sm2.setPrivateKey(privateKey);
        return sm2.signHex(HexUtil.encodeHexStr(content));
    }

    public static boolean verify(PublicKey publicKey, String content, String sign) {
        SM2 sm2 = SmUtil.sm2();
        sm2.setPublicKey(publicKey);
        return sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
    }

    public static PrivateKey strToPrivateKey(String privateKeyStr) {
        PrivateKey privateKey = null;

        try {
            byte[] encPriv = Base64.decode(privateKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            privateKey = keyFact.generatePrivate(new PKCS8EncodedKeySpec(encPriv));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return privateKey;
    }

    public static PublicKey strToPublicKey(String publicKeyStr) {
        PublicKey publicKey = null;

        try {
            byte[] encPub = Base64.decode(publicKeyStr);
            KeyFactory keyFact = KeyFactory.getInstance("EC", bc);
            publicKey = keyFact.generatePublic(new X509EncodedKeySpec(encPub));
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return publicKey;
    }

    public static void exportPublicKey(PublicKey publicKey, String path) {
        File file = new File(path);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] encPub = publicKey.getEncoded();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(encPub);
            fos.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static void exportPrivateKey(PrivateKey privateKey, String keyPath) {
        File file = new File(keyPath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] encPriv = privateKey.getEncoded();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(encPriv);
            fos.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static PublicKey importPublicKey(String path) {
        File file = new File(path);

        try {
            if (!file.exists()) {
                return null;
            } else {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[16];

                int size;
                while((size = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, size);
                }

                fis.close();
                byte[] bytes = baos.toByteArray();
                String publicKeyStr = Base64.encode(bytes);
                return strToPublicKey(publicKeyStr);
            }
        } catch (IOException var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static PrivateKey importPrivateKey(String keyPath) {
        File file = new File(keyPath);

        try {
            if (!file.exists()) {
                return null;
            } else {
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[16];

                int size;
                while((size = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, size);
                }

                fis.close();
                byte[] bytes = baos.toByteArray();
                String privateKeyStr = Base64.encode(bytes);
                return strToPrivateKey(privateKeyStr);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static void test1() {
        String text = "测试aaaaaaaaa";
        SM2 sm2 = SmUtil.sm2();
        String s = sm2.encryptBcd(text, KeyType.PublicKey);
        System.out.println(s);
        String s1 = StrUtil.utf8Str(sm2.decryptFromBcd(s, KeyType.PrivateKey));
        System.out.println(s1);
    }

    public static void test2() {
        String text = "测试aaaaaaaaa";
        System.err.println(text);
        KeyPair keyPair = SecureUtil.generateKeyPair("SM2");
        byte[] priKey = keyPair.getPrivate().getEncoded();
        byte[] pubKey = keyPair.getPublic().getEncoded();
        System.err.println("=====================================");
        SM2 sm2obj = SmUtil.sm2(priKey, pubKey);
        String encStr = sm2obj.encryptBcd(text, KeyType.PublicKey);
        System.err.println(encStr);
        String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(encStr, KeyType.PrivateKey));
        System.err.println(decStr);
    }

    public static void test3() {
        String text = "测试aaaaaaaaa";
        System.err.println(text);
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        PublicKey aPublic = pair.getPublic();
        PrivateKey aPrivate = pair.getPrivate();
        exportPublicKey(aPublic, "F:/sm2/public_key.pem");
        exportPrivateKey(aPrivate, "F:/sm2/private_key.pem");
        PublicKey pubk2 = importPublicKey("F:/sm2/public_key.pem");
        PrivateKey priK2 = importPrivateKey("F:/sm2/private_key.pem");
        SM2 sm2 = SmUtil.sm2();
        sm2.setPublicKey(pubk2);
        String encStr = sm2.encryptBcd(text, KeyType.PublicKey);
        System.err.println(encStr);
        SM2 sm2obj = SmUtil.sm2();
        sm2obj.setPrivateKey(priK2);
        String decStr = StrUtil.utf8Str(sm2obj.decryptFromBcd(encStr, KeyType.PrivateKey));
        System.err.println(decStr);
    }

    public static void main(String[] args) {
        Map<String, Object> sm2Key = generateSM2Key();
        String str = "我次奥";
        System.out.println("元数据=" + str);
        String publicKeyStr = Base64.encode(((PublicKey)sm2Key.get("publicKey")).getEncoded());
        System.out.println("公钥=" + publicKeyStr);
        String privateKeyStr = Base64.encode(((PrivateKey)sm2Key.get("privateKey")).getEncoded());
        System.out.println("私钥=" + privateKeyStr);
        PublicKey publicKey = strToPublicKey(publicKeyStr);
        System.out.println("base64转公钥=" + publicKey.toString());
        PrivateKey privateKey = strToPrivateKey(privateKeyStr);
        System.out.println("base64转私钥=" + privateKey.toString());
        String encrypt = encrypt(str, publicKey);
        System.out.println("公钥加密=" + encrypt);
        String decrypt = decrypt(encrypt, privateKey);
        System.out.println("私钥解密=" + decrypt);
        String sign = sign(privateKey, str);
        System.out.println("私钥签名=" + sign);
        boolean verify = verify(publicKey, str, sign);
        System.out.println("公钥验签=" + verify);
    }
}