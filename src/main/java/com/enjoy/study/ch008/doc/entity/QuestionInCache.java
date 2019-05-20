package com.enjoy.study.ch008.doc.entity;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 14:30<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 缓存中的题目：只需要缓存题目文本和题目的摘要sha值，通过判断sha值是否变化来判定题目是否更改过 <br/>
 * <b>Description</b>:
 */
public class QuestionInCache {

    private final String detail;
    private final String sha;

    public QuestionInCache(String detail, String sha) {
        this.detail = detail;
        this.sha = sha;
    }

    public String getDetail() {
        return detail;
    }

    public String getSha() {
        return sha;
    }
}
