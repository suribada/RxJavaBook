package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * Created by lia on 2018-06-21.
 */
public class RxBindingActivity extends Activity {

    private TextView title;
    private CheckBox checkBox;
    private View area;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
     private Consumer<? super CharSequence> textChange = RxTextView.text(title); // (1)
     //private Consumer<? super CharSequence> textChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxbinding_layout);
        title = (TextView) findViewById(R.id.title);
        textChange = RxTextView.text(title); // (3)
        checkBox = (CheckBox) findViewById(R.id.check);
        area = findViewById(R.id.area);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked)
                -> area.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        RxCompoundButton.checkedChanges(checkBox).subscribe(isChecked -> area.setVisibility(isChecked ? View.VISIBLE : View.GONE)); // (1)
        RxCompoundButton.checkedChanges(checkBox).subscribe(RxView.visibility(area)); // (2)
    }

    public void onClickButton(View view) {
        compositeDisposable.add(Observable.interval(5, TimeUnit.SECONDS)
                .map(value -> ("current value1=" + value))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(title::setText)); // (2)
        compositeDisposable.add(Observable.interval(7, TimeUnit.SECONDS)
                .map(value -> ("current value2=" + value))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxTextView.text(title))); // (3)
        compositeDisposable.add(Observable.interval(9, TimeUnit.SECONDS)
                .map(value -> ("current value3=" + value))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textChange)); // (7)
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

}
