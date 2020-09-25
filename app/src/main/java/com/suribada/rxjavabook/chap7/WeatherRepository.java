package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.api.model.Weather;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherRepository {

    public Observable<Weather> getWeather() {
        KmaDataSource kmaDataSource = new KmaDataSource();
        KweatherDataSource kweatherDataSource = new KweatherDataSource();
        Observable<Weather> kmaObs = Observable.interval(1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> kmaDataSource.getWeather());

        Observable<Weather> kweatherObs = Observable.interval(1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> kweatherDataSource.getWeather());

        return Observable.ambArray(kmaObs, kweatherObs);
    }

}
