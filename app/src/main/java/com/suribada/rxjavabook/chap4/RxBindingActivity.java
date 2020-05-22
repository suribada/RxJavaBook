package com.suribada.rxjavabook.chap4;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding4.view.RxView;
import com.jakewharton.rxbinding4.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding4.widget.RxCompoundButton;
import com.jakewharton.rxbinding4.widget.RxRadioGroup;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.suribada.rxjavabook.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * Created by lia on 2018-06-21.
 */
public class RxBindingActivity extends Activity {

    private TextView title;
    private CheckBox checkBox;
    private View area;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // RxBindg4에서는 제거됨
    // https://github.com/JakeWharton/RxBinding/pull/425
//     private Consumer<? super CharSequence> textChange = RxTextView.text(title); // (1)
//     private Consumer<? super CharSequence> textChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxbinding_layout);
        title = findViewById(R.id.title);
        //textChange = RxTextView.text(title); // (3)
        checkBox = findViewById(R.id.check);
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
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

}
