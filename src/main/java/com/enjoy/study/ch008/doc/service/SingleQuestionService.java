package com.enjoy.study.ch008.doc.service;

import com.enjoy.study.ch008.doc.entity.QuestionInDB;
import com.enjoy.study.ch008.doc.etc.QuestionBank;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 13:17<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 解析题目文本，下载图片等，返回解析后的文本<br/>
 * <b>Description</b>:
 */
public class SingleQuestionService {

    /**
     * 解析题目
     *
     * @param questionId 题目id
     * @return 解析后的题目文本
     */
    public static String analyseQuestion(int questionId) {
        QuestionInDB question = QuestionBank.getQuestion(questionId);
        return QuestionMakeService.makeQuestion(questionId, question.getDetails());
    }
}
