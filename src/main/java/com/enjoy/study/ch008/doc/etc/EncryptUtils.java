package com.enjoy.study.ch008.doc.etc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 12:50<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 摘要加密<br/>
 * <b>Description</b>:
 */
public class EncryptUtils {

    /**
     * 加密指定的字符串
     *
     * @param origin      原始字符串
     * @param encryptName 加密方法
     * @return 返回已加密的字符串
     */
    private static String encrypt(String origin, String encryptName) {
        // parameter strSrc is a string will be encrypted,
        // parameter encName is the algorithm name will be used.
        // encName dafault to "MD5"
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = origin.getBytes();
        try {
            if (encryptName == null || encryptName.equals("")) {
                encryptName = "MD5";
            }
            md = MessageDigest.getInstance(encryptName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    /**
     * MD5加密
     *
     * @param string 待加密的字符串
     * @return 返回 MD5 加密后的字符串
     */
    public static String md5(String string) {
        return encrypt(string, "MD5");
    }

    /**
     * SHA-1 加密
     *
     * @param string 待加密的字符串
     * @return 返回 SHA-1 摘要后的字符串
     */
    public static String sha1(String string) {
        return encrypt(string, "SHA-1");
    }

    /**
     * SHA-256 加密
     *
     * @param string 待加密的字符串
     * @return 返回 sha-256 摘要后的字符串
     */
    public static String sha256(String string) {
        return encrypt(string, "SHA-256");
    }
}
