package com.suribada.rxjavabook.chap5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.io.IOException;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Noh.Jaechun on 2018. 9. 5..
 */
public class ProxyActivity extends Activity {

    private Subject<Message> messageSubject = PublishSubject.create();
    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_two_buttons);
        title = findViewById(R.id.title);
        findViewById(R.id.button1).setOnClickListener(this::onClickRecentMessage);
        findViewById(R.id.button2).setOnClickListener(this::onClickPopularMessage);
        messageSubject.observeOn(AndroidSchedulers.mainThread()) // (1)
            .subscribe(message -> title.setText(message.toString()),
                e -> title.setText("error occurred=" + e.getMessage()));
    }


    public void onClickRecentMessage(View view) {
        getRecentMessage().subscribeOn(Schedulers.io())
            .subscribe(messageSubject); // (2)
    }

    public void onClickPopularMessage(View view) {
        getPopularMessage().subscribeOn(Schedulers.io())
            .subscribe(messageSubject); // (3)
    }

    private Observable<Message> getRecentMessage() {
        Random random = new Random();
        int value = random.nextInt(2);
        if (value == 0) {
            return Observable.error(new IOException("io error"));
        }
        return Observable.just(new Message("recent value=" + value));
    }

    private Observable<Message> getPopularMessage() {
        Random random = new Random();
        int value = random.nextInt(2);
        if (value == 0) {
            return Observable.error(new IOException("io error"));
        }
        return Observable.just(new Message("hot value=" + value));
    }

    private class Message {

        private String msg;

        Message(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }
}
