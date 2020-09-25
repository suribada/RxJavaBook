package com.suribada.rxjavabook.chap7;

import com.suribada.rxjavabook.api.model.Weather;

import io.reactivex.rxjava3.core.Observable;

public class KmaDataSource implements WeatherDataSource {

    @Override
    public Observable<Weather> getWeather() {
        return null;
    }
}
