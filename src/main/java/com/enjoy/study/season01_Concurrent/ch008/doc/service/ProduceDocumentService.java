package com.enjoy.study.season01_Concurrent.ch008.doc.service;

import com.enjoy.study.season01_Concurrent.ch008.doc.entity.Document;
import com.enjoy.study.season01_Concurrent.ch008.doc.entity.QuestionInCache;
import com.enjoy.study.season01_Concurrent.ch008.doc.entity.QuestionInDB;
import com.enjoy.study.season01_Concurrent.ch008.doc.entity.TaskResult;
import com.enjoy.study.season01_Concurrent.ch008.doc.etc.QuestionBank;
import com.enjoy.utils.ThreadTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

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
            sb.append(SingleQuestionService.analyseQuestion(i));
        }
        return "complete_" + document.getDocName() + "_" + System.currentTimeMillis() + ".pdf";
    }

    /**
     * 缓存已处理过的文档以加快速度
     *
     * @param document 待处理的文档
     * @return 返回文档处理完毕的本地文件名
     */
    public static String makeDocumentCached(Document document) {
        System.out.println("begin to make document: " + document.getDocName());
        StringBuilder sb = new StringBuilder();
        int hitSize = 0;
        ConcurrentHashMap<Integer, QuestionInCache> cached = ConcurrentQuestionService.getCache();
        for (int i : document.getQuestions()) {
            // 检测缓存中是否已有处理过的记录
            QuestionInCache inCache = cached.get(i);
            if (null == inCache) {
                // 模拟解析题目以及其内容
                QuestionInDB inDB = QuestionBank.getQuestion(i);
                String detail = SingleQuestionService.analyseQuestion(i);
                sb.append(detail);
                cached.put(i, new QuestionInCache(detail, inDB.getSha()));
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
     * 并行生成题目并且同时更新题目内容
     *
     * @param document 待处理的文档
     * @return 返回文档处理完毕的本地文件名
     */
    public static String makeDocumentAsync(Document document) throws ExecutionException, InterruptedException {

        System.out.println("begin to make document: " + document.getDocName());

        Map<Integer, TaskResult> qstResultMap = new HashMap<>();
        for (Integer questionId : document.getQuestions()) {
            // 把题目按照顺序加入处理队列
            qstResultMap.put(questionId, ConcurrentQuestionService.analyseQuestion(questionId));
        }

        StringBuilder sb = new StringBuilder();
        // 按照顺序把题目组合成文档
        for (Integer questionId : document.getQuestions()) {
            TaskResult result = qstResultMap.get(questionId);
            // 返回的已处理题目，要么是题目文本，要么是等待处理的future
            sb.append(result.getDetail() == null ? result.getFuture().get().getDetail() : result.getDetail());
        }
        return "complete_" + document.getDocName() + "_" + System.currentTimeMillis() + ".pdf";
    }

    /**
     * 上传文档并返回上传之后的路径
     *
     * @param localPdfName 本地文档路径
     * @return 返回上传之后文档的url
     */
    public static String uploadDocument(String localPdfName) {
        Random random = new Random();
        ThreadTool.sleepMilliseconds(9000 + random.nextInt(500));
        return "http://www.xxx.com/upload/" + localPdfName;
    }
}
