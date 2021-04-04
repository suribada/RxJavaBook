package com.suribada.rxjavabook.chap1;

import android.text.format.DateFormat;

import org.junit.Test;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.suribada.rxjavabook.SystemClock.sleep;

public class ConceptTest {

    @Test
    public void interval() {
        Disposable disposable = Observable.interval(100, TimeUnit.MILLISECONDS) // (1)
                .subscribe(System.out::println); // (2)
        sleep(1000); // (3)
        disposable.dispose(); // (4)
        sleep(2000);
    }

    @Test
    public void iterable() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<String> days = Arrays.asList("2002-10-22,", "2020-10-01", "002-37", "2020-10-08");
        Iterator<String> iterator = days.iterator();
        try {
            while (iterator.hasNext()) { // (1)
                System.out.println(dateFormat.parse(iterator.next())); // (2)
            }
            System.out.println("parsed all"); // (3)
        } catch (ParseException e) { // (4) 시작
            System.out.println("error occurred: " + e.getMessage());
        } // (4) 끝
    }

    @Test
    public void observable() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<String> days = Arrays.asList("2002-10-22,", "2020-10-01", "002-37", "2020-10-08");
        Observable.fromIterable(days)
                .subscribe(day -> System.out.println(dateFormat.parse(day)), // (1)
                        e -> System.out.println("error occurred: " + e.getMessage()), // (2)
                        () -> System.out.println("parsed all")); // (3)
    }

    @Test
    public void observable_observer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<String> days = Arrays.asList("2002-10-22,", "2020-10-01", "002-37", "2020-10-08");
        Observable.fromIterable(days) // (1)
                .subscribe(new Observer<String>() {

                    private Disposable disposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d; // (2)
                    }

                    @Override
                    public void onNext(@NonNull String day) {
                        try {
                            System.out.println(dateFormat.parse(day)); // (3)
                        } catch (ParseException e) { // (4) 시작
                            onError(e);
                        } // (4) 끝
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (!disposable.isDisposed()) { // (5) 시작
                            disposable.dispose();
                        } // (5) 끝
                        System.out.println("error occurred: " + e.getMessage()); // (6)
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("parsed all"); // (7)
                    }

                });
    }

    /**
     * 동일한 출려
     */
    @Test
    public void add() {
        BigInteger value = new BigInteger("10");
        System.out.println(value.add(new BigInteger("5")));
        System.out.println(value.add(new BigInteger("5")));
    }

    @Test
    public void add2() {
        BigInteger value = new BigInteger("10");
        BigInteger value2 = value.add(new BigInteger("5"));
        BigInteger value3 = value2.add(new BigInteger("5"));
        System.out.println(value3);
    }

    @Test
    public void add3() {
        BigInteger value = new BigInteger("10");
        System.out.println(value.add(new BigInteger("5"))
            .add(new BigInteger("5")));
    }

    @Test
    public void flatten() {
        int[][] ints = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                System.out.println(ints[i][j]);
            }
        }
        int[] ints2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] ints3 = {1, 4, 7, 2, 5, 8, 3, 6, 9};
    }
}
