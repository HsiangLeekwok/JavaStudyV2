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

    private byte[] xor(byte[] data) throws IOException {
        for (byte b : data) {
            b = (byte) (b ^ KEY);
        }
        return data;
    }

    public void encrypt(File src) throws Exception {
        InputStream in = new FileInputStream(src);
        byte[] data = new byte[(int) src.length()];
        in.read(data)
        byte[] encrypted = xor(data);
        OutputStream out = new FileOutputStream(src);
        out.write(encrypted);
        out.flush();
    }

    public byte[] decrypt(File src) throws Exception {
        InputStream in = new FileInputStream(src);
        byte[] data = new byte[(int) src.length()];
        in.read(data);
        return xor(data);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\JavaStudy\\StudyV2.0\\target\\classes\\com\\enjoy\\study\\season02_JVM\\ch04\\xor\\DemoUser.class");
        XorEncrypt encrypt = new XorEncrypt();
        encrypt.encrypt(file);
    }
}
