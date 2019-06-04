package com.enjoy.study.season02_JVM.ch05.memory_leak;

import java.util.HashSet;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/04 20:47<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Node {

    private int x, y;

    public Node(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static void main(String[] args) {
        HashSet<Node> hashSet = new HashSet<>();
        Node node1 = new Node(1, 3);
        Node node2 = new Node(3, 5);
        hashSet.add(node1);
        hashSet.add(node2);
        node2.setY(7);// node2 的hash值改变
        hashSet.remove(node2);// 删掉node2节点
        System.out.println(hashSet.size());
    }
}
