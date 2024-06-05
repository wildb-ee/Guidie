package com.fayaz.guidie.network;

import com.fayaz.guidie.dto.WeatherResponse;
import com.fayaz.guidie.dto.YelpBusinessResponse;
import com.fayaz.guidie.dto.YelpSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YelpApiService {

    static final String API_KEY = "YOUR API KEY";
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer "+API_KEY,
            "accept: application/json"
    })
    @GET("search?sort_by=best_match")
    Call<YelpSearchResponse> findBusinessesByCategory(@Query("location") String location, @Query("limit") long limit,@Query("categories") List<String> categories);


    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer "+API_KEY,
            "accept: application/json"
    })
    @GET("{Id}")
    Call<YelpBusinessResponse> findBusinessByID(@Path("Id") String id);


}
