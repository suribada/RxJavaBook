package com.suribada.rxjavabook.api;

import com.suribada.rxjavabook.api.model.Book;
import com.suribada.rxjavabook.api.model.BookCategory;
import com.suribada.rxjavabook.api.model.Region;
import com.suribada.rxjavabook.api.model.WeatherDetail;
import com.suribada.rxjavabook.api.model.Weather;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Noh.Jaechun on 2018-04-08.
 */
public interface BookSampleService {

    @GET("api/region.json")
    Single<Region> getRegion(@Query("latitude") double latitude, @Query("longitude") double longitude);

    @GET("api/weather.json")
    Single<Weather> getWeather(@Query("query") int areaCode);

    @GET("api/weather_detail.json")
    Single<WeatherDetail> getWeatherDetail(@Query("query") int areaCode);

    @GET("api/book_category.json")
    Single<List<BookCategory>> getBookCategories();

    @GET("api/bestseller.json")
    Single<List<Book>> getBestSeller();

    @GET("api/recommend_books.json")
    Single<List<Book>> getRecommendBooks();

    @GET("api/category{id}.json")
    Single<List<Book>> getCategoryBooks(@Path("id") int id);

}
