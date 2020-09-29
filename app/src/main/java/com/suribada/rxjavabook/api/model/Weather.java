package com.suribada.rxjavabook.api.model;

import java.util.Objects;

/**
 {
 "weatherCode" : 10,
 "weatherText" : "흐리고 갬",
 "currentTemperature" : 10.0
 }
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class Weather {
    private int weatherCode;
    private String weatherText;
    private float currentTemperature;

    public static Weather create(int weatherCode, String weatherText, float currentTemperature) {
        Weather weather = new Weather();
        weather.weatherCode = weatherCode;
        weather.weatherText = weatherText;
        weather.setCurrentTemperature(currentTemperature);
        return weather;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return weatherCode == weather.weatherCode &&
                Float.compare(weather.currentTemperature, currentTemperature) == 0 &&
                weatherText.equals(weather.weatherText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weatherCode, weatherText, currentTemperature);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "weatherCode=" + weatherCode +
                ", weatherText='" + weatherText + '\'' +
                ", currentTemperature=" + getCurrentTemperature() +
                '}';
    }
}
