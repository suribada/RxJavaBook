package com.suribada.rxjavabook.chap6;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;
import com.suribada.rxjavabook.seudo.LoginMessage;
import com.suribada.rxjavabook.seudo.Weather;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.Subject;

public class WrongSubscribeOnActivity extends Activity {

    private Subject<String> subject = PublishSubject.create();
    private Subject<LoginMessage> loginSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.five_buttons);
        subject.subscribeOn(Schedulers.io())
                .subscribe(value -> System.out.println(Thread.currentThread().getName() + ":" + value));
        loginSubject.subscribe(this::refreshUI); // 크래시 발생 가능
        loginSubject.subscribeOn(AndroidSchedulers.mainThread()) // 역시 크래시 발생 가능
                .subscribe(this::refreshUI);
        loginSubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::refreshUI);
    }

    public void onClickButton1(View view) {
        subject.onNext("시크릿쥬쥬");
    }

    public void onClickButton2(View view) {
        new Thread(() -> subject.onNext("공룡메카드")).start();
    }

    public void onClickButton3(View view) {
        showWeather();
    }

    public void onClickButton4(View view) {
        loginSubject.onNext(new LoginMessage());
    }

    public void onClickButton5(View view) {
        new Thread(() -> {
            loginSubject.onNext(new LoginMessage());
        }).start();
    }

    private void refreshUI(LoginMessage loginMessage) {
    }

    private void requestWeather(String areaName, WeatherResultListener listener) { // (1) 시작
        new Thread(() -> {
            //...
            listener.onResult("성남시 분당구", new Weather());
        }, "WeatherThread").start();
    } // (1) 끝

    private Observable<Weather> getWeatherObservable() { // (2) 시작
        return Observable.create(emitter -> {
            requestWeather("분당구 정자동", new WeatherResultListener() {
                @Override
                public void onResult(String code, Weather weather) {
                    emitter.onNext(weather);
                    emitter.onComplete();
                }

                @Override
                public void onFail(Throwable e) {
                    emitter.onError(e);
                }
            });
        });
    } // (2) 끝

    public void showWeather() {
        getWeatherObservable()
                .subscribeOn(Schedulers.io()) // (3)
                .subscribe(weather -> System.out.println(Thread.currentThread().getName() + ": " + weather));
    }

    public interface WeatherResultListener {
        void onResult(String area, Weather weather);
        void onFail(Throwable e);
    }

}
