package com.enjoy.study.ch008.doc.etc;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 12:12<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 常量定义<br/>
 * <b>Description</b>:
 */
public class Const {

    /**
     * 系统中 CPU 个数
     */
    public static final int CPU = Runtime.getRuntime().availableProcessors();

    /**
     * 模拟数据库中题目个数
     */
    public static final int QUESTION_BANK_TOTAL = 2000;

    /**
     * 每份文档中题目数量基数
     */
    public static final int QUESTION_IN_DOCUMENT = 60;

    /**
     * 题目最大长度基数
     */
    public static final int QUESTION_DETAIL_LENGTH = 800;
    /**
     * 题目长度变量基数
     */
    public static final int QUESTION_DETAIL_RANDOM = 500;

    /**
     * 提供随机生成题目内容的文本基数
     */
    public static final String DATA = "0123456789abcdefghijklkmopqrstuvwxyz";
}
