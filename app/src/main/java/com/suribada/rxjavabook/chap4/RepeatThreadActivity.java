package com.suribada.rxjavabook.chap4;


import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.suribada.rxjavabook.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by lia on 2018-06-04.
 */
public class RepeatThreadActivity extends Activity {

    private boolean cancelled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);
        ((Button) findViewById(R.id.button4)).setText("취소");
    }

    private int value = 0;
    private int value2 = 0;
    private int value3 = 0;
    private int value4 = 0;
    private int value5 = 0;

    public void onClickButton1(View view) {
        new Thread(() -> {
            while (!cancelled) {
                callApi();
                System.out.println(String.valueOf(++value));
                SystemClock.sleep(10_000); // (1)
            }
        }).start();
        new Thread(() -> {
            while (!cancelled) {
                callApi();
                System.out.println(String.valueOf(++value2));
                SystemClock.sleep(8_000); // (1)
            }
        }).start();
        new Thread(() -> {
            while (!cancelled) {
                callApi();
                System.out.println(String.valueOf(++value3));
                SystemClock.sleep(6_000); // (1)
            }
        }).start();
        new Thread(() -> {
            while (!cancelled) {
                callApi();
                System.out.println(String.valueOf(++value4));
                SystemClock.sleep(7_000); // (1)
            }
        }).start();
        new Thread(() -> {
            while (!cancelled) {
                callApi();
                System.out.println(String.valueOf(++value5));
                SystemClock.sleep(3_000); // (1)
            }
        }).start();
    }

    private void callApi() {
    }

    private ScheduledFuture future, future2, future3, future4, future5;

    public void onClickButton2(View view) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2); // (1)
        future = executor.scheduleAtFixedRate(() -> { // (2)
            System.out.println(String.valueOf(++value));
        },0, 6, TimeUnit.SECONDS);
        future2 = executor.scheduleAtFixedRate(() -> { // (3)
            System.out.println(String.valueOf(++value));
        },0, 8, TimeUnit.SECONDS);
        future3 = executor.scheduleAtFixedRate(() -> { // (4)
            System.out.println(String.valueOf(++value));
        },0, 10, TimeUnit.SECONDS);
        future4 = executor.scheduleAtFixedRate(() -> {
            System.out.println(String.valueOf(++value));
        },0, 7, TimeUnit.SECONDS);
        future5 = executor.scheduleAtFixedRate(() -> {
            System.out.println(String.valueOf(++value));
        },0, 3, TimeUnit.SECONDS);

    }

    private Disposable disposable, disposable2, disposable3, disposable4, disposable5;

    public void onClickButton3(View view) {
        disposable = Observable.interval(0, 6, TimeUnit.SECONDS)
                .subscribe(ignored -> {
                    System.out.println(String.valueOf(++value));
                });
        disposable2 = Observable.interval(0, 8, TimeUnit.SECONDS)
                .subscribe(ignored -> {
                    System.out.println(String.valueOf(++value));
                });
        disposable3 = Observable.interval(0, 10, TimeUnit.SECONDS)
                .subscribe(ignored -> {
                    System.out.println(String.valueOf(++value));
                });
        disposable4 = Observable.interval(0, 7, TimeUnit.SECONDS)
                .subscribe(ignored -> {
                    System.out.println(String.valueOf(++value));
                });
        disposable5 = Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribe(ignored -> {
                    System.out.println(String.valueOf(++value));
                });
    }

    public void onClickButton4(View view) {
        cancelled = true;
        if (future != null) {
            future.cancel(true);
        }
        if (future2 != null) {
            future.cancel(true);
        }
        if (future3 != null) {
            future3.cancel(true);
        }
        if (future4 != null) {
            future4.cancel(true);
        }
        if (future5 != null) {
            future5.cancel(true);
        }
        if (disposable != null) {
            disposable.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
        if (disposable3 != null) {
            disposable3.dispose();
        }
        if (disposable4 != null) {
            disposable4.dispose();
        }
        if (disposable5 != null) {
            disposable5.dispose();
        }
    }

    private List<String> getEmails() {
        return Arrays.asList("suribada@gmail.com", "endofhope@naver.com");
    }

    private void sendMail(String email) {
        System.out.println("start mail");
        SystemClock.sleep(1000);
        System.out.println("end mail");
    }
}
