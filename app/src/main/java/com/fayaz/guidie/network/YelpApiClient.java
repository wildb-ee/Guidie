package com.fayaz.guidie.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpApiClient {

    private static final String BASE_URL = "https://api.yelp.com/v3/businesses/";
    private static YelpApiService apiService;

    public static YelpApiService getApiService() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(YelpApiService.class);
        }
        return apiService;
    }
}
