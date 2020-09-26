package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import io.reactivex.rxjava3.core.Observable;

public class KmaDataSource implements WeatherDataSource {

    @Override
    public Observable<Weather> getWeather() {
        return Observable.create(emitter -> {
            Weather weather = new Weather();
            weather.currentTemperature = 10.0f;
            weather.weatherCode = 10;
            weather.weatherText = "비";
        });
    }
}
