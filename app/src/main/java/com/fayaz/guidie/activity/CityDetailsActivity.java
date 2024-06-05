package com.fayaz.guidie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fayaz.guidie.adapter.BusinessListAdapter;
import com.fayaz.guidie.adapter.City;
import android.widget.Button;

import com.fayaz.guidie.R;
import com.fayaz.guidie.adapter.CityListAdapter;
import com.fayaz.guidie.dto.DayResponse;
import com.fayaz.guidie.dto.WeatherResponse;
import com.fayaz.guidie.dto.YelpBusinessResponse;
import com.fayaz.guidie.dto.YelpSearchResponse;
import com.fayaz.guidie.network.WeatherApiClient;
import com.fayaz.guidie.network.WeatherApiService;
import com.fayaz.guidie.network.YelpApiClient;
import com.fayaz.guidie.network.YelpApiService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private City city;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    private RecyclerView recyclerView;
    private BusinessListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<YelpBusinessResponse> businesses = new ArrayList<YelpBusinessResponse>();
    private TableLayout weatherForecastTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_city_details);

        Bundle bundle = getIntent().getExtras();
        city = new City(bundle.getString("cityName"),
                bundle.getString("cityImage"),Double.parseDouble( bundle.getString("cityLat")),
                Double.parseDouble(bundle.getString("cityLon")),bundle.getString("cityCountry"));
        toolbar= findViewById(R.id.city_details_toolbar);
        toolbarTitle = findViewById(R.id.city_name_toolbar_title);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        toolbarTitle.setText(city.getName());

        weatherForecastTable = findViewById(R.id.weather_forecast_table);


        fetchWeatherForecast();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.city_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        recyclerView = findViewById(R.id.suggested_shops);
        recyclerView.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BusinessListAdapter(businesses, this);
        recyclerView.setAdapter(mAdapter);

        loadSuggestedPlaces(4);
        mAdapter.setOnClickListener((position, model) -> {
           Intent intent = new Intent(this, BusinessDetailsActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("businessID", model.getId());
            bundle1.putString("businessName", model.getName());

            intent.putExtras(bundle1);


            SharedPreferences mPrefs =  getSharedPreferences("BUS_PREF", MODE_PRIVATE );
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(model);
            prefsEditor.putString("selectedBusinessInfo", json);
            prefsEditor.apply();



            startActivity(intent);
        });

    }


    private void loadSuggestedPlaces(int limit){
        Call<YelpSearchResponse> resp = YelpApiClient.getApiService().findBusinessesByCategory(city.getName(),limit,null);

        resp.enqueue(new Callback<YelpSearchResponse>() {
            @Override
            public void onResponse(Call<YelpSearchResponse> call, Response<YelpSearchResponse> response) {
                Log.println(Log.ERROR, "asd", "aasa");

                if(response.isSuccessful()){
                    List<YelpBusinessResponse> searchRes= response.body().getBusinesses();

                    businesses.clear();
                    businesses.addAll(searchRes);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<YelpSearchResponse> call, Throwable t) {
                Log.println(Log.ERROR, "asd", "aa");
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        LatLng coordCity = new LatLng(city.getLatitude(),city.getLongitude());


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordCity, 15));
        googleMap.addMarker(new MarkerOptions().position(coordCity));

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder().target(coordCity).zoom(15).build()
        ));
    }

    private void fetchWeatherForecast(){
        Call<WeatherResponse> responseCall = WeatherApiClient.getApiService().getWeekForecastByName(city.getName());
        responseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.println(Log.WARN,"sg", "as");
                if (response.isSuccessful()) {
                    WeatherResponse bResponse = response.body();
                    displayWeatherForecast(bResponse);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.println(Log.WARN,"sg", t.toString());
            }

        });
    }



    private void displayWeatherForecast(WeatherResponse response) {
        weatherForecastTable.removeViews(1, Math.max(0, weatherForecastTable.getChildCount() - 1)); // Remove all rows except the header
        List<DayResponse> days = response.getList();
        for (int i = 0; i < days.size(); i++) {
            TableRow row = new TableRow(this);
            TextView date = new TextView(this);
            date.setText(days.get(i).getDt_txt());
            date.setPadding(8, 8, 8, 8);


            ImageView weather = new ImageView(this);

            Glide.with(this)
                    .load("https://openweathermap.org/img/wn/"+days.get(i).getWeather().get(0).getIcon()+"@2x.png")
                    .apply(new RequestOptions().override(200, 200))
                    .into(weather);

            weather.setPadding(8, 8, 8, 8);

            TextView temperature = new TextView(this);
            String tmp =Double.toString(Math.floor(days.get(i).getMain().getTemp() - 273.15));
            temperature.setText(tmp);
            temperature.setPadding(8, 8, 8, 8);

            row.addView(date);
            row.addView(weather);
            row.addView(temperature);

            weatherForecastTable.addView(row);
        }
    }


}