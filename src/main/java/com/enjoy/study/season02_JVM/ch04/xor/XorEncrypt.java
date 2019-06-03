package com.enjoy.study.season02_JVM.ch04.xor;

import java.io.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 22:03<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 异或加密和解密的服务类<br/>
 * <b>Description</b>:
 */
public class XorEncrypt {

    private static final byte KEY = 0x1F;

    /**
     * 位运算：异或操作（一个数经过两次异或 = 自己）
     *
     * @param in
     * @param out
     * @throws Exception
     */
    private void xor(InputStream in, OutputStream out) throws Exception {
        int ch;
        while (-1 != (ch = in.read())) {
            ch = ch ^ KEY;
            out.write(ch);
        }
    }

    public void encrypt(File src, File des) throws Exception {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(des);
        xor(in, out);

        in.close();
        out.close();
    }

    public byte[] decrypt(File src) throws Exception {
        InputStream in = new FileInputStream(src);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        xor(in, bos);
        return bos.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        File src = new File("D:\\JavaStudy\\StudyV2.0\\target\\classes\\com\\enjoy\\study\\season02_JVM\\ch04\\xor\\DemoUserSrc.class");
        File des = new File("D:\\JavaStudy\\StudyV2.0\\target\\classes\\com\\enjoy\\study\\season02_JVM\\ch04\\xor\\DemoUser.class");

        XorEncrypt encrypt = new XorEncrypt();
        encrypt.encrypt(src, des);
        System.out.println("encrypt class complete.");
    }
}
