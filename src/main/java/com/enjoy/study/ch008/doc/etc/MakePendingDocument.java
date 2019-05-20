package com.enjoy.study.ch008.doc.etc;

import com.enjoy.study.ch008.doc.entity.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 12:08<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 生成随机的待处理文档<br/>
 * <b>Description</b>:
 */
public class MakePendingDocument {

    /**
     * 生成指定大小的待处理文档列表
     *
     * @param size 文档数量
     * @return
     */
    public static List<Document> makeDocuments(int size) {
        List<Document> documents = new ArrayList<>();

        // 题目 id 随机
        Random questionIdRandom = new Random();
        // 文档中题目数量随机 + 基数
        Random questionSizeRandom = new Random();
        for (int i = 0; i < size; i++) {
            List<Integer> questions = new ArrayList<>();
            // 每份文档中至少应该有 60 道题目
            int qSize = questionSizeRandom.nextInt(Const.QUESTION_IN_DOCUMENT) + Const.QUESTION_IN_DOCUMENT;
            for (int j = 0; j < qSize; j++) {
                // 随机题目id
                int id = questionIdRandom.nextInt(Const.QUESTION_BANK_TOTAL);
                while (questions.contains(id)) {
                    // 同一文档中不能有相同的题目id
                    id = questionIdRandom.nextInt(Const.QUESTION_BANK_TOTAL);
                }
                questions.add(id);
            }
            Document document = new Document("document_" + i, questions);
            documents.add(document);
        }
        return documents;
    }
}
