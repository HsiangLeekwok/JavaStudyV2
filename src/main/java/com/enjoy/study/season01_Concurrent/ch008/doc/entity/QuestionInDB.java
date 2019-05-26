package com.enjoy.study.season01_Concurrent.ch008.doc.entity;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 12:03<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 数据库中的实体<br/>
 * <b>Description</b>:
 */
public class QuestionInDB {
    private final int id;
    private final String details;
    private final String sha;

    public QuestionInDB(int id, String details, String sha) {
        this.id = id;
        this.details = details;
        this.sha = sha;
    }

    public int getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public String getSha() {
        return sha;
    }
}
