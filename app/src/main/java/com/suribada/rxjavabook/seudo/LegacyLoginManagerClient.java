package com.suribada.rxjavabook.seudo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import io.reactivex.disposables.Disposable;

/**
 * Created by lia on 2017-10-14.
 */

public class LegacyLoginManagerClient extends Activity {

    private LegacyLoginManager.LoginListener loginListener = new LegacyLoginManager.LoginListener() {
        @Override
        public void loginStatusChanged(boolean login) {
            applyLoginStatus(login);
        }
    };

    private void applyLoginStatus(boolean login) {
        if (login) {
            // 로그인 상태에 따른 화면 변경
        } else {
            // 로그아웃 상태에 따른 화면 변경
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LegacyLoginManager.getInstance(this).addLoginListener(loginListener);
        // 현재 상태는 별도로 반영
        applyLoginStatus(LegacyLoginManager.getInstance(this).isLogin());
    }

    @Override
    protected void onDestroy() {
        LegacyLoginManager.getInstance(this).removeLoginListener(loginListener);
        super.onDestroy();
    }
    
}
