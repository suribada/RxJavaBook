package com.suribada.rxjavabook.seudo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Naver on 2017. 10. 13..
 */
public class LegacyLoginManager {

    private static final Object lock = new Object();

    private static LegacyLoginManager instance;

    public static LegacyLoginManager getInstance(Context context) {
        synchronized (lock) {
            if (instance == null) {
                instance =  new LegacyLoginManager(context.getApplicationContext());
            }
            return instance;
        }
    }

    public interface LoginListener {

        void loginStatusChanged(boolean login);
    }

    // java.util.Observables 처럼 동기화 고려 필요
    private ArrayList<LoginListener> loginListeners = new ArrayList<>();

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
    }

    private LegacyLoginManager(Context context) {
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

    // 현재 로그인 상태로 최종 상태 저장 필요
    public boolean isLogin() {
        return true;
    }

    public void ssoLogin() {
        // 콜백 등에서 호출
        notifyLoginStatus(true);
    }

    public void startLoginActivity() {
        // 로그인 화면 뛰워서 그 안에서 로그인하고 그 결과를 브로드캐스트
    }

    public void logout() {
        // 콜백 등에서 호출
        notifyLoginStatus(false);
    }

}
