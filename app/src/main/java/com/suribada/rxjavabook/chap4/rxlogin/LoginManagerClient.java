package com.suribada.rxjavabook.chap4.rxlogin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.disposables.Disposable;

/**
 * Created by lia on 2017-10-14.
 */

public class LoginManagerClient extends Activity {

    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //...
        disposable = LoginManager.getInstance(this).getObservable() // (1) 시작
                .subscribe(login -> {
                   if (login) {
                       // 로그인 상태에 따른 화면 변경
                   } else {
                       // 로그아웃 상태에 따른 화면 변경
                   }
                }); // (1) 끝
    }

    @Override
    protected void onDestroy() {
        disposable.dispose(); // (2)
        super.onDestroy();
    }

}
