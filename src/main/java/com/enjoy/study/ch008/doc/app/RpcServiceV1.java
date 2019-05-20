package com.enjoy.study.ch008.doc.app;

import com.enjoy.study.ch008.doc.entity.Document;
import com.enjoy.study.ch008.doc.entity.QuestionInDB;
import com.enjoy.study.ch008.doc.etc.Const;
import com.enjoy.study.ch008.doc.etc.MakePendingDocument;
import com.enjoy.study.ch008.doc.etc.QuestionBank;
import com.enjoy.study.ch008.doc.service.ProduceDocumentService;

import java.util.List;
import java.util.concurrent.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/20 13:47<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 服务化，拆分业务逻辑为不同的子服务<br/>
 * <b>Description</b>: 1、拆分文档处理为单独的服务；2、拆分上传业务为单独的服务；3、将两者分别交给不同的线程池来执行
 */
public class RpcServiceV1 {

    // produce 60 documents(cpu x2) cost 372067ms: 6201ms/per document
    // produce 60 documents(cpu x4) cost 193804ms: 3230ms/per document
    // produce 60 documents(cpu x8) cost 116189ms: 1936ms/per document
    // produce 60 documents(cpu x16) cost 67201ms: 1120ms/per document
    /**
     * IO 密集型任务，线程数量可以定为 CPU 数量 x2
     */
    private static ExecutorService makeService = Executors.newFixedThreadPool(Const.CPU * 16);
    /**
     * 上传任务
     */
    private static ExecutorService uploadService = Executors.newFixedThreadPool(Const.CPU * 16);

    /**
     * 文档解析
     */
    private static CompletionService<String> completionMakeService = new ExecutorCompletionService<>(makeService);
    /**
     * 文档上传
     */
    private static CompletionService<String> completionUploadService = new ExecutorCompletionService<>(uploadService);

    /**
     * 处理单个文档的方法
     */
    private static class MakeDocumentTask implements Callable<String> {

        private Document document;

        public MakeDocumentTask(Document document) {
            this.document = document;
        }

        @Override
        public String call() throws Exception {
            long start = System.currentTimeMillis();
            String docName = ProduceDocumentService.makeDocument(document);
            System.out.println("make document " + document.getDocName() + " cost: " + (System.currentTimeMillis() - start) + "ms");
            return docName;
        }
    }

    /**
     * 上传任务
     */
    private static class UploadDocumentTask implements Callable<String> {

        private String localName;

        public UploadDocumentTask(String localName) {
            this.localName = localName;
        }

        @Override
        public String call() throws Exception {
            long start = System.currentTimeMillis();
            String uploadName = ProduceDocumentService.uploadDocument(localName);
            System.out.println("upload pdf to " + uploadName + " cost: " + (System.currentTimeMillis() - start) + "ms");
            return uploadName;
        }
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("initialize question bank......");
        QuestionBank.init();
        System.out.println("initialize question bank complete.");

        int docSize = 60;
        List<Document> documents = MakePendingDocument.makeDocuments(docSize);
        long begin = System.currentTimeMillis();

        for (Document document : documents) {
            // 提交任务
            completionMakeService.submit(new MakeDocumentTask(document));
        }

        // 获取文档解析任务的执行结果并提交给上传任务
        for (int i = 0; i < docSize; i++) {
            // 获取解析任务的执行结果
            Future<String> future = completionMakeService.take();
            completionUploadService.submit(new UploadDocumentTask(future.get()));
        }

        // 获取上传结果
        for (int i = 0; i < docSize; i++) {
            // 获取解析任务的执行结果
            String uploaded = completionUploadService.take().get();
            //completionUploadService.submit(new UploadDocumentTask(future.get()));
        }
        System.out.println("produce " + documents.size() + " documents cost " + (System.currentTimeMillis() - begin) + "ms");
    }
}
