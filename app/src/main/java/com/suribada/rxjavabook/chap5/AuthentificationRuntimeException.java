package com.suribada.rxjavabook.chap5;

/**
 * Created by Noh.Jaechun on 2018. 7. 29..
 */
public class AuthentificationRuntimeException extends RuntimeException {
    private String message;

    public AuthentificationRuntimeException(String message) {
        this.message = message;
    }
}
