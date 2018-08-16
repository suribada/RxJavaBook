package com.suribada.rxjavabook.chap5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;

/**
 * Created by Noh.Jaechun on 2018. 8. 6..
 */
public class RxStreamReader {

    public static Observable<String> lines(InputStream inputStream) {
        return Observable.create(emitter -> {
            try (BufferedReader br = new BufferedReader( // (1) 시작
                    new InputStreamReader(inputStream, "UTF-8"))) { // (1) 끝
                String line;
                while ((line = br.readLine()) != null && !emitter.isDisposed()) { // (2)
                    emitter.onNext(line); // (3)
                }
                if (!emitter.isDisposed()) {
                    emitter.onComplete(); // (4)
                }
            } catch (IOException e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e); // (5)
                }
            }
        });
    }

}
