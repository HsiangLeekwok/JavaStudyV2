package com.enjoy.study.ch008.doc.etc;

import com.enjoy.study.ch008.doc.entity.QuestionInDB;
import com.enjoy.utils.ThreadTool;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 12:29<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 题目仓库<br/>
 * <b>Description</b>: 初始化指定数量的题目，并提供查询等相关方法
 */
public class QuestionBank {

    /**
     * 原始题目列表
     */
    private static ConcurrentHashMap<Integer, QuestionInDB> questionMap = new ConcurrentHashMap<>();

    /**
     * 定时更新某个题库
     */
    private static ScheduledExecutorService updateQuestionService = new ScheduledThreadPoolExecutor(1);

    // 初始化题目仓库
    public static void init() {
        Random questionDetailRandom = new Random();
        for (int i = 0; i < Const.QUESTION_BANK_TOTAL; i++) {
            int detailLength = randomDetailLength(questionDetailRandom);
            String detail = randomDetail(detailLength);
            QuestionInDB question = new QuestionInDB(i, detail, EncryptUtils.sha1(detail));
            questionMap.put(i, question);
        }
        scheduleUpdate();
    }

    /**
     * 模拟数据库查找指定的题目
     *
     * @param id 题目 id
     * @return
     */
    public static QuestionInDB getQuestion(int id) {
        ThreadTool.sleepMilliseconds(10);
        return questionMap.get(id);
    }

    private static String randomDetail(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int index, len = Const.DATA.length();
        for (int i = 0; i < length; i++) {
            index = random.nextInt(len);
            sb.append(Const.DATA.charAt(index));
        }
        return sb.toString();
    }

    private static int randomDetailLength(Random random) {
        return Const.QUESTION_DETAIL_LENGTH - random.nextInt(Const.QUESTION_DETAIL_RANDOM);
    }

    /**
     * 随机更新某个题目的内容
     */
    private static class UpdateQuestion implements Runnable {

        @Override
        public void run() {
            Random random = new Random();
            int id = random.nextInt(Const.QUESTION_BANK_TOTAL);
            String detail = randomDetail(randomDetailLength(random));
            questionMap.put(id, new QuestionInDB(id, detail, EncryptUtils.sha1(detail)));
            //System.out.println("question " + id + " has updated.");
        }
    }

    /**
     * 启动定时更新题库
     */
    private static void scheduleUpdate() {
        updateQuestionService.scheduleAtFixedRate(new UpdateQuestion(), 1, 2, TimeUnit.SECONDS);
    }

//    static {
//        init();
//    }
}
