package com.suribada.rxjavabook.api.model;

/**
 {
 "currentTemp" : 11.5,
 "hourAfterTemp" : 15,
 "hours2AfterTemp" : 18.0,
 "hours3AfterTemp" : 5.0
 }
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class WeatherDetail {
    public float currentTemperature;
    public float hourAfterTemperature;
    public float hours2AfterTemperature;
    public float hours3AfterTemperature;

    @Override
    public String toString() {
        return "WeatherDetail{" +
                "currentTemperature=" + currentTemperature +
                ", hourAfterTemperature=" + hourAfterTemperature +
                ", hours2AfterTemperature=" + hours2AfterTemperature +
                ", hours3AfterTemperature=" + hours3AfterTemperature +
                '}';
    }
}
