package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import io.reactivex.rxjava3.core.Observable;

public class KweatherDataSource implements WeatherDataSource {

    @Override
    public Observable<Weather> getWeather() {
        return Observable.create(emitter -> {
            Weather weather = new Weather();
            weather.setCurrentTemperature(12.0f);
            weather.setWeatherCode(11);
            weather.setWeatherText("맑음");
        });
    }

}
