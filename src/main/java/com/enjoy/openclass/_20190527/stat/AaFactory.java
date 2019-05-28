package com.enjoy.openclass._20190527.stat;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/27 20:29<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class AaFactory implements ManToolsFactory {
    @Override
    public void saleMainTools(String size) {
        System.out.println("根据您的需求，定制一款size=" + size + "的女模特");
    }
}
