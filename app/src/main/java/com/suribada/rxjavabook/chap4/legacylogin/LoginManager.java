package com.suribada.rxjavabook.chap4.legacylogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.suribada.rxjavabook.seudo.LoginMessage;

import java.util.ArrayList;

/**
 * Created by Naver on 2017. 10. 13..
 */
public class LoginManager {

    private static final Object lock = new Object();

    private static LoginManager instance;

    public static LoginManager getInstance(Context context) { // (1) 시작
        synchronized (lock) {
            if (instance == null) {
                instance =  new LoginManager(context.getApplicationContext());
            }
            return instance;
        }
    } // (1) 끝

    public interface LoginListener {
        void loginStatusChanged(boolean login);
    }

    // java.util.Observables 처럼 동기화 고려 필요
    private ArrayList<LoginListener> loginListeners = new ArrayList<>(); // (2) 시작

    public  void  addLoginListener(LoginListener loginListener) {
        loginListeners.add(loginListener);
    }

    public  void removeLoginListener(LoginListener loginListener) {
        loginListeners.remove(loginListener);
    }

    private void notifyLoginStatus(boolean login) {
        for (LoginListener each : loginListeners) {
            each.loginStatusChanged(login);
        }
    } // (2) 끝

    private LoginManager(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginMessage.LOGIN_FINISH);
        intentFilter.addAction(LoginMessage.LOGOUT_FINISH);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(LoginMessage.LOGIN_FINISH)) {
                    notifyLoginStatus(true);
                } else if (action.equals(LoginMessage.LOGOUT_FINISH)) {
                    notifyLoginStatus(false);
                }
            }
        }, intentFilter);
    }

    public boolean isLogin() { // 현재 로그인 상태로 최종 상태 저장 필요
        return true;
    }

    public void ssoLogin() {
        // 콜백 등에서 호출
        notifyLoginStatus(true);
    }

    public void startLoginActivity() {
        // 로그인 화면 뛰워서 그 안에서 로그인하고 그 결과를 브로드캐스트
    }

    public void login() {
        //...
        notifyLoginStatus(true); // (3)
    }

    public void logout() {
        //...
        notifyLoginStatus(false); // (4)
    }

}
