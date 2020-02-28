package com.suribada.rxjavabook.chap4.legacylogin;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Created by lia on 2017-10-14.
 */

public class LoginManagerClient extends Activity {

    private LoginManager.LoginListener loginListener = new LoginManager.LoginListener() {
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
        LoginManager.getInstance(this).addLoginListener(loginListener);
        // 현재 상태는 별도로 반영
        applyLoginStatus(LoginManager.getInstance(this).isLogin());
    }

    @Override
    protected void onDestroy() {
        LoginManager.getInstance(this).removeLoginListener(loginListener);
        super.onDestroy();
    }
    
}
