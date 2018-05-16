package com.suribada.rxjavabook.chap4.javalogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.suribada.rxjavabook.seudo.LoginMessage;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Naver on 2017. 10. 13..
 */
public class LoginManager extends Observable {

    private static final Object lock = new Object();

    private static LoginManager instance;
    private boolean loginStatus;

    public static LoginManager getInstance(Context context) { // (1) 시작
        synchronized (lock) {
            if (instance == null) {
                instance =  new LoginManager(context.getApplicationContext());
            }
            return instance;
        }
    } // (1) 끝

    private void setLoginStatus(boolean loginStauts) { // (1) 시작
        this.loginStatus = loginStauts;
        setChanged();
        notifyObservers();
    } // (1) 끝

    public boolean getLoginStatus() { // (2)
        return loginStatus;
    } // (2)

    private LoginManager(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginMessage.LOGIN_FINISH);
        intentFilter.addAction(LoginMessage.LOGOUT_FINISH);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(LoginMessage.LOGIN_FINISH)) {
                    setLoginStatus(true);
                } else if (action.equals(LoginMessage.LOGOUT_FINISH)) {
                    setLoginStatus(false);
                }
            }
        }, intentFilter);
    }

    public boolean isLogin() { // 현재 로그인 상태로 최종 상태 저장 필요
        return loginStatus;
    }

    public void ssoLogin() {
        // 콜백 등에서 호출
        setLoginStatus(true);
    }

    public void startLoginActivity() {
        // 로그인 화면 뛰워서 그 안에서 로그인하고 그 결과를 브로드캐스트
    }

    public void login() {
        //...
        setLoginStatus(true);
    }

    public void logout() {
        //...
        setLoginStatus(false);
    }

}
