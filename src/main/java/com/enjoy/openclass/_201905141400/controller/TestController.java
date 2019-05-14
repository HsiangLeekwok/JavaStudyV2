package com.enjoy.openclass._201905141400.controller;

import com.enjoy.openclass._201905141400.annotation.TestAutowired;
import com.enjoy.openclass._201905141400.annotation.TestRequestParam;
import com.enjoy.openclass._201905141400.annotation.TestService;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 15:10<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */

@TestController
public class TestController {

    @TestAutowired
    private TestService testService;

    public void query(HttpServletRequest request, HttpServletResponse response,
                      @TestRequestParam("name") String name,
                      @TestRequestParam("age") String age) {
        try {
            PrintWriter pw=response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
