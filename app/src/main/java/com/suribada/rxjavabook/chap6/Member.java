package com.suribada.rxjavabook.chap6;

public class Member {

    private String name;

    public Member(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "member=" + name;
    }
}
