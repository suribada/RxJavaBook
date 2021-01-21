package com.suribada.rxjavabook.chap1;

public class Comment {

    private String userId;
    private String text;
    private int like;

    public Comment(String userId, String text, int like) {
        this.userId = userId;
        this.text = text;
        this.like = like;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public int getLike() {
        return like;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", like=" + like +
                '}';
    }

}
