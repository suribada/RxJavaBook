package com.suribada.rxjavabook.chap6;

public class Profile {

    private Member member;

    public Profile(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "name=" + member.getName();
    }
}
