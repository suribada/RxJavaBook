package com.suribada.rxjavabook.replay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class ReplaySubjectActivity extends Activity {

    private Subject<String> replaySubject = ReplaySubject.create();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView title, title2;

    private Observable<String> repeatObservable = Observable.interval(1, TimeUnit.SECONDS)
            .map(value -> "current=" + value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_subject);
        title = (TextView) findViewById(R.id.title);
        title2 = (TextView) findViewById(R.id.title2);
        repeatObservable.subscribe(replaySubject);
    }

    public void onClickButton1(View view) {
        compositeDisposable.add(replaySubject.observeOn(AndroidSchedulers.mainThread())
            .subscribe(text -> title.setText(text)));
    }

    private void showTitle(String input) {
        title.setText(input);
    }

    public void onClickButton2(View view) {
        compositeDisposable.add(replaySubject.observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> {
                    Log.d("Replay", "replay text=" + text);
                    title2.setText(text);
                }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
