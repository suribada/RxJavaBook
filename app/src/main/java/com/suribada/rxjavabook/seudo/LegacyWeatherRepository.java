package com.suribada.rxjavabook.seudo;

import android.location.Location;

/**
 * 콜백 Hell 표현
 *
 * Created by Naver on 2017. 10. 12..
 */
public class LegacyWeatherRepository {

    public interface AreaResultListener {
        void onResult(int code, Area area);
        void onFailure(Throwable e);
    }

    public interface WeatherResultListener {
        void onResult(int code, Weather weather);
        void onFail(Throwable e);
    }

    public void requestWeather(Location location) {
        requestLocationCode(location, new AreaResultListener() {
            @Override
            public void onResult(int code, Area area) {
                String areaCode = "1111"; // 디폴트 위치
                if (code == 200) {
                    areaCode = area.code;
                }
                requestWeather(areaCode, new WeatherResultListener() {

                    @Override
                    public void onResult(int code, Weather weather) {
                        // 화면에 보여주기
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
