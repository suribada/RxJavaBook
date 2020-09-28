package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import io.reactivex.rxjava3.core.Observable;

public class KmaDataSource implements WeatherDataSource {

    @Override
    public Observable<Weather> getWeather() {
        return Observable.create(emitter -> {
            Weather weather = new Weather();
            weather.setCurrentTemperature(10.0f);
            weather.setWeatherCode(10);
            weather.setWeatherText("비");
        });
    }
}
