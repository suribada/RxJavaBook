package com.suribada.rxjavabook.chap11;

import com.suribada.rxjavabook.api.model.Weather;

import io.reactivex.rxjava3.core.Observable;

public interface WeatherDataSource {
    Observable<Weather> getWeather();
}
