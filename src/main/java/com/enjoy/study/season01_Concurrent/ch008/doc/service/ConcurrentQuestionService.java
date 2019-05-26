package com.enjoy.study.season01_Concurrent.ch008.doc.service;

import com.enjoy.study.season01_Concurrent.ch008.doc.entity.QuestionInCache;
import com.enjoy.study.season01_Concurrent.ch008.doc.entity.QuestionInDB;
import com.enjoy.study.season01_Concurrent.ch008.doc.entity.TaskResult;
import com.enjoy.study.season01_Concurrent.ch008.doc.etc.QuestionBank;

import java.util.concurrent.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/21 09:46<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 使用线程池并发处理题目以及更新题目的服务<br/>
 * <b>Description</b>:
 */
public class ConcurrentQuestionService {

    private static ExecutorService analyseService = Executors.newCachedThreadPool();

    /**
     * 已处理过的题目缓存列表
     */
    private static ConcurrentHashMap<Integer, QuestionInCache> cachedQuestion = new ConcurrentHashMap<>();

    static ConcurrentHashMap<Integer, QuestionInCache> getCache() {
        return cachedQuestion;
    }

    /**
     * 正在处理的题目列表
     */
    private static ConcurrentHashMap<Integer, Future<QuestionInCache>> processingQuestion = new ConcurrentHashMap<>();

    /**
     * 解析题目
     *
     * @param questionId 题目id
     * @return 解析后的题目文本
     */
    public static TaskResult analyseQuestion(int questionId) {
        QuestionInCache cache = cachedQuestion.get(questionId);
        if (null == cache) {
            //System.out.println("Question " + questionId + " not cached, refresh it.");
            return new TaskResult(getQuestionFuture(questionId));
        } else {
            // 缓存中存在，则查询摘要是否已变化
            String sha = QuestionBank.getQuestion(questionId).getSha();
            if (cache.getSha().equals(sha)) {
                // 摘要没有变化
                //System.out.println("Question " + questionId + " has cached, reuse it.");
                return new TaskResult(cache.getDetail());
            } else {
                System.out.println("Question " + questionId + " has cached, but need update detail.");
                return new TaskResult(getQuestionFuture(questionId));
            }
        }
    }

    private static Future<QuestionInCache> getQuestionFuture(int questionId) {
        Future<QuestionInCache> cached = processingQuestion.get(questionId);
        try {
            if (null == cached) {
                QuestionInDB db = QuestionBank.getQuestion(questionId);
                MakeQuestion task = new MakeQuestion(db);
                //Future<QuestionInCache> questionFuture = analyseService.submit(task);
                FutureTask<QuestionInCache> future = new FutureTask<>(task);
                // 先 put 一次占位，如果是首次 put，则返回值会为空，详情见 putIfAbsent 方法的说明
                cached = processingQuestion.putIfAbsent(questionId, future);
                if (null == cached) {
                    // 第一次提交到map中
                    cached = future;
                    analyseService.execute(future);
                } else {
                    //System.out.println("Question " + questionId + " has other task processing");
                }
            } else {
                //System.out.println("Question " + questionId + " in processing, not need re-create task.");
            }
            return cached;
        } catch (Exception e) {
            // 发生异常之后需要移除本题目的处理 task，否则 task 会一直存在导致无法再次处理（更新或另一个 task 来处理题目）
            processingQuestion.remove(questionId);
            e.printStackTrace();
            throw e;
        }
    }

    private static class MakeQuestion implements Callable<QuestionInCache> {

        private QuestionInDB question;

        MakeQuestion(QuestionInDB question) {
            this.question = question;
        }

        @Override
        public QuestionInCache call() throws Exception {
            try {
                String detail = QuestionMakeService.makeQuestion(question.getId(), question.getDetails());
                String sha = question.getSha();
                QuestionInCache cache = new QuestionInCache(detail, sha);
                cachedQuestion.put(question.getId(), cache);
                return cache;
            } finally {
                // 无论正常与否，题目处理完毕之后都要从缓存队列中移除正在处理的题目记录，否则再次更新的时候会没有办法再处理本题目
                processingQuestion.remove(question.getId());
            }
        }
    }

}
