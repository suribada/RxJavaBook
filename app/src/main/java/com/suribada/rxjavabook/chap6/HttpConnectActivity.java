package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.suribada.rxjavabook.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpConnectActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        Stetho.initializeWithDefaults(this);
    }

    private static String BASE_URL = "https://raw.githubusercontent.com/suribada/rxjavaSample/master/app/server/";

    public void onClickButton1(View view) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        for (int i = 0; i < 100; i++) {
            final int k = i;
            new Thread(() -> {
                Request request = new Request.Builder()
                        .url(BASE_URL + "api/region.json?key=" + k)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
