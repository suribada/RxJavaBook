package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherRepository {

    KmaDataSource kmaDataSource;
    KweatherDataSource kweatherDataSource;

    WeatherRepository(KmaDataSource kmaDataSource, KweatherDataSource kweatherDataSource) {
        this.kmaDataSource = kmaDataSource;
        this.kweatherDataSource = kweatherDataSource;
    }

    public Observable<Weather> getWeather() {
        Observable<Weather> kmaObs = Observable.interval(1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> kmaDataSource.getWeather());

        Observable<Weather> kweatherObs = Observable.interval(1, TimeUnit.MINUTES, Schedulers.io())
                .flatMap(ignored -> kweatherDataSource.getWeather());

        return Observable.ambArray(kmaObs, kweatherObs);
    }

}
