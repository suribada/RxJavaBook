package com.suribada.rxjavabook.error;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import io.reactivex.rxjava3.core.Single;

/**
 * Created by Naver on 2017. 10. 16..
 */

public class DateFormatter {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String getFormatedDate(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.format(date);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    public long getTimeInMillis1(String input) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.parse(input).getTime();
    }

    public long getTimeInMillis2(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(input).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1L; // -1을 상수로 해도 큰 차이가 없음
        }
    }

    public long getTimeInMillis3(String input) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(input).getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Illegal input pattern");
        }
    }

    public Single<Long> getTimeInMillis(String input) {
        return Single.fromCallable(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(input).getTime();
        });
    }

}
