package com.suribada.rxjavabook.seudo;

import android.location.Location;
import android.widget.TextView;

/**
 * 콜백 Hell 표현
 *
 * Created by Naver on 2017. 10. 12..
 */
public class LegacyWeatherRepository {

    private TextView title;

    public interface AreaResultListener {
        void onResult(Area area);
        void onFailure(Throwable e);
    }

    public interface WeatherResultListener {
        void onResult(int code, Weather weather);
        void onFail(Throwable e);
    }

    public void requestWeather(Location location) {
        requestLocationCode(location, new AreaResultListener() {
            @Override
            public void onResult(Area area) {
                requestWeather(area.code, new WeatherResultListener() {
                    @Override
                    public void onResult(int code, Weather weather) {
                        title.setText(weather.toString());
                    }

                    @Override
                    public void onFail(Throwable e) {
                        // 로그나 Toast
                    }
                });
            }

            @Override
            public void onFailure(Throwable e) {
                // 로그나 Toast
            }
        });
    }

    public void requestLocationCode(Location location, AreaResultListener listener) {

    }

    public void requestWeather(String areaCode, WeatherResultListener listener) {

    }

}
