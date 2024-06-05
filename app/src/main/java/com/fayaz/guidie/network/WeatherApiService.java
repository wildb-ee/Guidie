package com.fayaz.guidie.network;

import com.fayaz.guidie.dto.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApiService {

    static final String API_KEY = "YOUR API KEY";
    @GET("?cnt=6&appid="+API_KEY)
    Call<WeatherResponse> getWeekForecastByName(@Query("q") String cityName );

    @GET("?cnt=6&appid="+API_KEY)
    Call<WeatherResponse> getWeekForecastByCoord(@Query("lat") String latitude, @Query("lon") String longitude );



}
