package com.enjoy.openclass._20190527;

import com.enjoy.openclass._20190527.dynamic.BbFactory;
import com.enjoy.openclass._20190527.dynamic.LisonCompany;
import com.enjoy.openclass._20190527.dynamic.WomanToolsFactory;
import com.enjoy.openclass._20190527.stat.AaFactory;
import com.enjoy.openclass._20190527.stat.ManToolsFactory;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/27 21:04<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class ZhangSan {

    public static void main(String[] args) {
        ManToolsFactory aFactory = new AaFactory();
        WomanToolsFactory bFactory = new BbFactory();
        LisonCompany lisonCompany = new LisonCompany();
        lisonCompany.setFactory(aFactory);
        ManToolsFactory lison1 = (ManToolsFactory) lisonCompany.getProxyInstance();
        lison1.saleMainTools("D");
        System.out.println("----------------");
        lisonCompany.setFactory(bFactory);
        WomanToolsFactory lison2= (WomanToolsFactory) lisonCompany.getProxyInstance();
        lison2.saleWomenTools(1.0F);
    }
}
