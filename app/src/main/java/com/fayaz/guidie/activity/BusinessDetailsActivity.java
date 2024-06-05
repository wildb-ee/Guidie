package com.fayaz.guidie.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fayaz.guidie.R;
import com.fayaz.guidie.dto.YelpBusinessResponse;
import com.fayaz.guidie.network.YelpApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessDetailsActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private Toolbar toolbar;

    private ImageView businessImage;
    private TextView category;

    private RatingBar ratingBar;

    private YelpBusinessResponse business;

    private TextView openedText;

    private TextView locationText;
    private TextView phoneText;

    private Button saveButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_details);
        toolbar= findViewById(R.id.business_details_toolbar);
        toolbarTitle = findViewById(R.id.business_name_toolbar_title);
        businessImage = findViewById(R.id.business_image);
        category = findViewById(R.id.business_category);
        ratingBar = findViewById(R.id.business_rating_bar);
        openedText = findViewById(R.id.business_opened);
        locationText = findViewById(R.id.business_location);
        phoneText = findViewById(R.id.business_phone);
        saveButton = findViewById(R.id.business_save_button);

        mAuth = FirebaseAuth.getInstance();


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        Bundle bundle = getIntent().getExtras();
        String businessName = bundle.getString("businessName");
        toolbarTitle.setText(businessName);





        if(bundle.getBoolean("isLocation")){
            String id =bundle.getString("businessID");
            YelpApiClient.getApiService().findBusinessByID(id).enqueue(new Callback<YelpBusinessResponse>() {
                @Override
                public void onResponse(Call<YelpBusinessResponse> call, Response<YelpBusinessResponse> response) {
                    if(response.isSuccessful()){
                        business = response.body();
                        updateUi();

                    }

                    Log.println(Log.ASSERT, "asd", "asd");

                }

                @Override
                public void onFailure(Call<YelpBusinessResponse> call, Throwable t) {
                    Log.println(Log.ASSERT, "asd", "asd");
                }
            });
        }
        else{
            SharedPreferences mPrefs = getSharedPreferences("BUS_PREF", MODE_PRIVATE );

            Gson gson = new Gson();
            String json = mPrefs.getString("selectedBusinessInfo", "");
            if (json.isEmpty()) {
                Log.println(Log.ERROR,"asd","asd");
            }
            business = gson.fromJson(json, YelpBusinessResponse.class);

            updateUi();
        }








    }

    private void updateUi(){
        Glide.with(this)
                .load(business.getImage_url())
                .into(businessImage);
        StringBuilder cats = new StringBuilder();
        for (int i = 0 ; i<business.getCategories().size();i++){
            cats.append(business.getCategories().get(i).getTitle());
            if(i<business.getCategories().size()-1)
                cats.append(",");
        }
        category.setText(cats.toString());

        ratingBar.setRating((float)business.getRating());
        openedText.setText(business.getIs_closed()?"Closed":"Opened");
        locationText.setText(String.format("%s: %s,%s,%s", business.getLocation().getCity(), business.getLocation().getAddress1(), business.getLocation().getAddress2(), business.getLocation().getAddress3()));
        phoneText.setText(business.getPhone());



        saveButton.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> docData = new HashMap<>();
            docData.put("businessID", business.getId());
            docData.put("businessName", business.getName());
            docData.put("businessImage", business.getImage_url());
            docData.put("businessRating", business.getRating());
            docData.put("businessCategory", business.getCategories().get(0));
            docData.put("businessLocation", business.getLocation());

            db.collection("users").document(user.getUid()).collection("savedBusiness").document(business.getId())
                    .set(docData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(LOG, "DocumentSnapshot successfully written!");
                            Intent intent = new Intent(BusinessDetailsActivity.this, NavActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(LOG, "Error writing document", e);
                        }
                    });


        });
    }




}