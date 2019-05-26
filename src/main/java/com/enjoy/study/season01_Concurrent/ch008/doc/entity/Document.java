package com.enjoy.study.season01_Concurrent.ch008.doc.entity;

import java.util.List;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 12:04<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 待处理文档<br/>
 * <b>Description</b>:
 */
public class Document {
    private final String docName;
    private final List<Integer> questions;

    public Document(String docName, List<Integer> questions) {
        this.docName = docName;
        this.questions = questions;
    }

    public String getDocName() {
        return docName;
    }

    public List<Integer> getQuestions() {
        return questions;
    }
}
