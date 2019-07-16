package com.enjoy.study.season04_Netty.ch02.rpc.server.service.impl;

import com.enjoy.study.season04_Netty.ch02.rpc.server.service.StockService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 22:26<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class StockServiceImpl implements StockService {

    private static ConcurrentHashMap<String, Integer> goods = new ConcurrentHashMap<>();

    static {
        goods.put("A001", 1000);
        goods.put("B002", 2000);
        goods.put("C003", 3000);
        goods.put("D004", 4000);
    }

    @Override
    public synchronized void addStock(String goodsId, int addAmount) {
        System.out.println("+++++++++++++ goods: " + goodsId + ", amount: " + addAmount);
        int amount = goods.get(goodsId) + addAmount;
        goods.put(goodsId, amount);
        System.out.println("+++++++++++++ goods " + goodsId + " stock amount is: " + amount);
    }

    @Override
    public synchronized void deduceStock(String goodsId, int deduceAmount) {
        System.out.println("------------ goods: " + goodsId + ", deduceAmount: " + deduceAmount);
        int amount = goods.get(goodsId) - deduceAmount;
        goods.put(goodsId, amount);
        System.out.println("------------ goods " + goodsId + " stock amount is: " + amount);
    }
}
