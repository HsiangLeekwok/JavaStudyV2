package com.enjoy.study.season01_Concurrent.ch008.doc.app;

import com.enjoy.study.season01_Concurrent.ch008.doc.entity.Document;
import com.enjoy.study.season01_Concurrent.ch008.doc.etc.MakePendingDocument;
import com.enjoy.study.season01_Concurrent.ch008.doc.etc.QuestionBank;
import com.enjoy.study.season01_Concurrent.ch008.doc.service.ProduceDocumentService;

import java.util.List;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 13:25<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class SequentialTest {

    /**
     * 串行测试 produce 3 documents cost 165486ms: 55162ms/per document
     */
    public static void main(String[] args) {
        System.out.println("initialize question bank......");
        QuestionBank.init();
        System.out.println("initialize question bank complete.");

        // 模拟处理文档
        List<Document> documents = MakePendingDocument.makeDocuments(3);
        long begin = System.currentTimeMillis();
        for (Document document : documents) {
            System.out.println("begin make document \"" + document.getDocName() + "\", contains questions: " + document.getQuestions().size());
            long start = System.currentTimeMillis();
            String docName = ProduceDocumentService.makeDocument(document);
            System.out.println("make document cost: " + (System.currentTimeMillis() - start) + "ms");
            start = System.currentTimeMillis();
            docName = ProduceDocumentService.uploadDocument(docName);
            System.out.println("upload pdf to " + docName + " cost: " + (System.currentTimeMillis() - start) + "ms");
        }
        System.out.println("produce " + documents.size() + " documents cost " + (System.currentTimeMillis() - begin) + "ms");
    }
}
