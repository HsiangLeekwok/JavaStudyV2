package com.enjoy.openclass._20190527.dynamic;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/27 20:55<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class BbFactory implements WomanToolsFactory {

    @Override
    public void saleWomenTools(float length) {
        System.out.println("根据您的需求，为您定制了一个高度为" + length + "的男模特");
    }
}
