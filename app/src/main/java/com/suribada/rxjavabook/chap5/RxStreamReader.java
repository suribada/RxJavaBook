package com.suribada.rxjavabook.chap5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import okio.Buffer;

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

    public static Observable<String> linesUsing(InputStream inputStream) {
        return Observable.using(() -> new BufferedReader( // (1)
            new InputStreamReader(inputStream, "UTF-8")), // (1) 끝
            br -> { // (2) 시작
                return Observable.create(emitter -> {
                    String line;
                    while ((line = br.readLine()) != null && !emitter.isDisposed()) {
                        emitter.onNext(line);
                    }
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                });
            }, // (2) 끝
            br -> br.close()); // (3)
    }

    public static Observable<String> linesGenerate(InputStream inputStream) {
        return Observable.generate(() -> new BufferedReader( // (1)
                    new InputStreamReader(inputStream, "UTF-8")), // (1) 끝
                (br, emitter) -> { // (2) 시작
                    String line = br.readLine(); // (3) 시작
                    if (line != null) {
                        emitter.onNext(line);
                    } else {
                        emitter.onComplete();
                    } // (3) 끝
                },
                br -> br.close()); // (4)
    }

}
