package com.enjoy.study.season05_Tomcat.ch02_Tomcat.container;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/23 16:44<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 管道的实现类<br/>
 * <b>Description</b>:
 */
public class StandardPipeline implements Pipeline {
    /**
     * 非基础阀门只需定义第一个即可
     */
    protected Valve first = null;
    /**
     * 基础阀门放到最后
     */
    protected Valve basic = null;

    @Override
    public Valve getFirst() {
        return first;
    }

    @Override
    public Valve getBasic() {
        return basic;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basic = valve;
    }

    /**
     * 添加阀门，链式构建阀门的执行顺序(先定制的阀门，最后基础阀门)
     * @param valve
     */
    @Override
    public void addValve(Valve valve) {
        if (null == first) {
            // 第一个阀门为空，则设置首阀门
            first = valve;
            valve.setNext(basic);
        } else {
            // 遍历链表，找到basic之前的阀门，然后将当前添加的阀门加入到末尾
            // 以此确保定制的阀门一定保持在基础阀门前面
            Valve current = first;
            while (null != current) {
                if (current.getNext() == basic) {
                    current.setNext(valve);
                    valve.setNext(basic);
                }
                current = current.getNext();
            }
        }
    }
}
