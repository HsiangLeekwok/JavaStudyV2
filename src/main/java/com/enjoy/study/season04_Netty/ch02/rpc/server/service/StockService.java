package com.enjoy.study.season04_Netty.ch02.rpc.server.service;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 22:23<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public interface StockService {
    void addStock(String goodsId,int addAmount);
    void deduceStock(String goodsId,int deduceAmount);
}
