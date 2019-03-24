package com.suribada.rxjavabook.chap5;

/**
 * Created by Noh.Jaechun on 2018. 7. 29..
 */
public class AuthentificationException extends RuntimeException {
    private String message;

    public AuthentificationException(String message) {
        this.message = message;
    }
}
