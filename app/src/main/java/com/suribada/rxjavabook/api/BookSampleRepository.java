package com.suribada.rxjavabook.api;

import android.location.Location;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.suribada.rxjavabook.api.model.Book;
import com.suribada.rxjavabook.api.model.BookCategory;
import com.suribada.rxjavabook.api.model.Region;
import com.suribada.rxjavabook.api.model.Weather;
import com.suribada.rxjavabook.api.model.WeatherDetail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Noh.Jaechun on 2018-04-08.
 */
public class BookSampleRepository {

    private static String BASE_URL = "https://raw.githubusercontent.com/suribada/rxjavaSample/master/app/server/";

    private final OkHttpClient client;

    public BookSampleRepository() {
        client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public BookSampleService getBookSampleService() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BookSampleService.class);
    }

    public Single<Region> getRegion(Location location) {
        return getBookSampleService().getRegion(location.getLatitude(), location.getLongitude());
    }

    public Single<Weather> getWeather(int areaCode) {
        return getBookSampleService().getWeather(areaCode);
    }

    public Observable<Weather> getWeatherObservable(int areaCode) {
        return getBookSampleService().getWeather(areaCode).toObservable();
    }

    public Single<WeatherDetail> getWeatherDetail(int areaCode) {
        return getBookSampleService().getWeatherDetail(areaCode);
    }

    public Observable<WeatherDetail> getWeatherDetailObservable(int areaCode) {
        return getBookSampleService().getWeatherDetail(areaCode).toObservable();
    }

    public Single<List<BookCategory>> getBookCategories() {
        return getBookSampleService().getBookCategories();
    }

    public Single<List<Book>> getBestSeller() {
        return getBookSampleService().getBestSeller();
    }

    public Single<List<Book>> getRecommendBooks() {
        return getBookSampleService().getRecommendBooks();
    }

    public Single<List<Book>> getCategoryBooks(int categoryId) {
        return getBookSampleService().getCategoryBooks(categoryId);
    }

    public Observable<List<BookCategory>> getBookCategoriesObservable() {
        return getBookSampleService().getBookCategories().toObservable();
    }

    public Observable<List<Book>> getBestSellerObservable() {
        return getBookSampleService().getBestSeller().toObservable();
    }

    public Observable<List<Book>> getRecommendBooksObservable() {
        return getBookSampleService().getRecommendBooks().toObservable();
    }

    public Observable<List<Book>> getCategoryBooksObservable(int categoryId) {
        return getBookSampleService().getCategoryBooks(categoryId).toObservable();
    }

}
