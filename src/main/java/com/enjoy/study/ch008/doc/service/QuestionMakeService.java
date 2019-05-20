package com.enjoy.study.ch008.doc.service;

import com.enjoy.utils.ThreadTool;

import java.util.Random;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 13:19<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 模拟解析题目文本，下载图片等<br/>
 * <b>Description</b>:
 */
public class QuestionMakeService {

    /**
     * 解析题目文本，下载图片等，大约每个题目耗时约 400~500ms
     *
     * @param questionId     题目id
     * @param questionDetail 题目内容
     * @return
     */
    public static String makeQuestion(int questionId, String questionDetail) {
        Random random = new Random();
        ThreadTool.sleepMilliseconds(400 + random.nextInt(150));
        return "Make Question Complete[id=" + questionId + ", detail=" + questionDetail + "]";
    }
}
