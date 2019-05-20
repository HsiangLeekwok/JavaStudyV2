package com.enjoy.study.ch008.doc.service;

import com.enjoy.study.ch008.doc.entity.Document;
import com.enjoy.study.ch008.doc.entity.QuestionInCache;
import com.enjoy.study.ch008.doc.entity.QuestionInDB;
import com.enjoy.study.ch008.doc.etc.QuestionBank;
import com.enjoy.utils.ThreadTool;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 13:00<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 生成文档的服务<br/>
 * <b>Description</b>:
 */
public class ProduceDocumentService {

    /**
     * 生成文档并返回文档在本地的存储文件名
     *
     * @param document 待处理的文档
     */
    public static String makeDocument(Document document) {
        System.out.println("begin to make document: " + document.getDocName());
        StringBuilder sb = new StringBuilder();
        for (int i : document.getQuestions()) {
            // 模拟解析题目以及其内容
            sb.append(QuestionService.analyseQuestion(i));
        }
        return "complete_" + document.getDocName() + "_" + System.currentTimeMillis() + ".pdf";
    }

    /**
     * 已处理过的题目缓存列表
     */
    private static ConcurrentHashMap<Integer, QuestionInCache> cachedQuestion = new ConcurrentHashMap<>();

    /**
     * 并行，异步化文档处理过程，缓存已处理过的文档已加快速度
     *
     * @param document 待处理的文档
     * @return
     */
    public static String makeDocumentCached(Document document) {
        System.out.println("begin to make document: " + document.getDocName());
        StringBuilder sb = new StringBuilder();
        int hitSize = 0;
        for (int i : document.getQuestions()) {
            // 检测缓存中是否已有处理过的记录
            QuestionInCache inCache = cachedQuestion.get(i);
            if (null == inCache) {
                // 模拟解析题目以及其内容
                QuestionInDB inDB = QuestionBank.getQuestion(i);
                String detail = QuestionService.analyseQuestion(i);
                sb.append(detail);
                cachedQuestion.put(i, new QuestionInCache(detail, inDB.getSha()));
            } else {
                hitSize++;
                sb.append(inCache.getDetail());
                //System.out.println("question " + i + " has cached, do not need make again.");
            }
        }
        System.out.println("make document " + document.getDocName() + " complete, hit cache: " + hitSize + "/" + document.getQuestions().size());
        return "complete_" + document.getDocName() + "_" + System.currentTimeMillis() + ".pdf";
    }

    /**
     * 上传文档并返回上传之后的路径
     *
     * @param localPdfName 本地文档路径
     * @return
     */
    public static String uploadDocument(String localPdfName) {
        Random random = new Random();
        ThreadTool.sleepMilliseconds(9000 + random.nextInt(500));
        return "http://www.xxx.com/upload/" + localPdfName;
    }
}
