package com.suribada.rxjavabook.chap8;

public class Program {

    String title;

    public Program(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Program{" +
                "title='" + title + '\'' +
                '}';
    }
}
