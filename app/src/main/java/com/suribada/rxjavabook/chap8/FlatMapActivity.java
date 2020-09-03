package com.suribada.rxjavabook.chap8;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.suribada.rxjavabook.R;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FlatMapActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = findViewById(R.id.title);
    }

    public void onClickButton(View v) {
        getDepartments()
                .subscribeOn(Schedulers.io())
                .flatMap(department ->
                        getOutGoings(department)
                                .subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread()) // (1)
                .subscribe(value -> title.setText(value.toString()));
    }

    private Observable<Department> getDepartments() {
        return Observable.just(new Department("안드로이드팀"), new Department("iOS팀"),
                new Department("FE팀"), new Department("서버팀"));
    }

    int i = 0;

    private Observable<OutGoing> getOutGoings(Department department) {
        return Observable.just(new OutGoing(++i), new OutGoing(++i),
                new OutGoing(++i), new OutGoing(++i), new OutGoing(++i));
    }
}
