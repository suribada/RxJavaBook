package com.suribada.rxjavabook.model;

public class Dog extends Animal{
    String name;
    public Dog(String name) {
        this.name = name;
    }
    int nameLength() {
        return name.length();
    }

    public String name() {
        return name;
    }
}
