package com.suribada.rxjavabook.seudo;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Naver on 2017. 10. 13..
 */

public class LoginManager {

    private BehaviorSubject<Boolean> subject = BehaviorSubject.create();

    private NLoginManager nLoginManager = new NLoginManager();

    public void ssoLogin() {
        nLoginManager.ssoLogin(new SsoLoginCallback() {

            @Override
            public void onSsoLoginFinished(LoginResult loginResult) {
                subject.onNext(loginResult.login);
            }

        });
    }

    public void startLoginActivity() {
        // 로그인 화면 뛰워서 그 안에서 로그인하고 그 결과를 브로드캐스트한다.
    }

    public void logout() {
        nLoginManager.logout
    }



}
