package com.suribada.rxjavabook.chap3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.suribada.rxjavabook.R;

import java.util.Random;

import io.reactivex.Observable;

/**
 * Created by lia on 2018-03-21.
 */
public class ContractErrorActivity extends Activity {

    private TextView title;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        title = (TextView) findViewById(R.id.title);
        button = (Button) findViewById(R.id.button);
        RxView.clicks(button)
                .flatMap(ignored -> getBestSeller())
                .subscribe(bookTitle -> title.setText(bookTitle),
                        e -> Toast.makeText(this, "문제 발생", Toast.LENGTH_LONG).show()
                );
    }

    private Observable<String> getBestSeller() {
        Random random = new Random();
        int val = random.nextInt(5);
        return Observable.just("title " + 10 / val);
    }

}
