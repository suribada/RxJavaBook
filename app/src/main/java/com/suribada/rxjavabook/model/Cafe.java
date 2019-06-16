package com.suribada.rxjavabook.model;

public class Cafe {
    public int type; // 카페 타입
    public String name; // 카페명
    public String leader; // 운영자
    public String coleader; // 부운영자

    public Cafe(int type, String name, String leader, String coleader) {
        this.type = type;
        this.name = name;
        this.leader = leader;
        this.coleader = coleader;
    }

    @Override
    public String toString() {
        return "Cafe{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", leader='" + leader + '\'' +
                ", coleader='" + coleader + '\'' +
                '}';
    }
}
