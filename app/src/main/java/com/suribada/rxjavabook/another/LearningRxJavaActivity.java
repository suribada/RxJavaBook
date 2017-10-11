package com.suribada.rxjavabook.another;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;

import com.suribada.rxjavabook.R;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Learing RxJava Code Test
 *
 * Created by lia on 2017-09-28.
 */
public class LearningRxJavaActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_rxjava);
    }

    public void onClickButton1(View view) {
        Observable.range(1, 999_999_999)
                .map(MyItem::new)
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    SystemClock.sleep(50);
                    System.out.println("Received MyItem " +
                            myItem.id);
                });

    }

    public void onClickButton2(View view) {
        Flowable.range(1, 999_999_999)
                .map(MyItem::new)
                .observeOn(Schedulers.io())
                .subscribe(myItem -> {
                    SystemClock.sleep(50);
                    System.out.println("Received MyItem " +
                            myItem.id);
                });
    }

    static final class MyItem {
        final int id;

        MyItem(int id) {
            this.id = id;
            // System.out.println("Constructing MyItem " + id);
        }
    }

    public void onClickButton3(View view) {
        Flowable.interval(1, TimeUnit.MILLISECONDS)
                .doOnNext(s -> System.out.println("Source pushed "
                        + s))
                .observeOn(Schedulers.io())
                .map(i -> intenseCalculation(i))
                .subscribe(s -> System.out.println("Subscriber received " + s),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!")
                );
    }


    public static <T> T intenseCalculation(T value) {
//sleep up to 200 milliseconds
        SystemClock.sleep(ThreadLocalRandom.current().nextInt(200));
        return value;
    }
}
