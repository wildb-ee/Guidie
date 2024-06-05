package com.fayaz.guidie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fayaz.guidie.R;
import com.fayaz.guidie.activity.BusinessDetailsActivity;
import com.fayaz.guidie.adapter.BusinessListAdapter;
import com.fayaz.guidie.dto.YelpBusinessResponse;
import com.fayaz.guidie.dto.YelpCategoryResponse;
import com.fayaz.guidie.dto.YelpLocationResponse;
import com.fayaz.guidie.dto.YelpSearchResponse;
import com.fayaz.guidie.network.YelpApiClient;
import com.fayaz.guidie.network.YelpApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView categoryRecycler;
    private RecyclerView cityRecycler;
    private RecyclerView.LayoutManager layoutManagerCity;
    private RecyclerView.LayoutManager layoutManagerCategory;

    private BusinessListAdapter mAdapterCity;
    private BusinessListAdapter mAdapterCategory;

    private List<YelpBusinessResponse> suggestedByCity = new ArrayList<>();
    private List<YelpBusinessResponse> suggestedByCategory= new ArrayList<>();

    private FirebaseAuth mAuth;



    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryRecycler = view.findViewById(R.id.recycler_suggested_by_category);
        cityRecycler = view.findViewById(R.id.recycler_suggested_by_city);
        categoryRecycler.setNestedScrollingEnabled(false);
        cityRecycler.setNestedScrollingEnabled(false);
        layoutManagerCity = new LinearLayoutManager(getContext());
        layoutManagerCategory = new LinearLayoutManager(getContext());

        categoryRecycler.setLayoutManager(layoutManagerCity);
        cityRecycler.setLayoutManager(layoutManagerCategory);

        mAdapterCity = new BusinessListAdapter(suggestedByCity, getContext());
        cityRecycler.setAdapter(mAdapterCity);

        mAdapterCategory = new BusinessListAdapter(suggestedByCategory,getContext());
        categoryRecycler.setAdapter(mAdapterCategory);
        mAdapterCategory.setOnClickListener((position, model) -> {
            Intent intent = new Intent(getContext(), BusinessDetailsActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("businessID", model.getId());
            bundle1.putString("businessName", model.getName());
            bundle1.putString("businessName", model.getName());
            bundle1.putBoolean("isLocation", true);


            intent.putExtras(bundle1);


            startActivity(intent);
        });

        mAdapterCity.setOnClickListener((position, model) -> {
            Intent intent = new Intent(getContext(), BusinessDetailsActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("businessID", model.getId());
            bundle1.putString("businessName", model.getName());
            bundle1.putString("businessName", model.getName());
            bundle1.putBoolean("isLocation", true);


            intent.putExtras(bundle1);


            startActivity(intent);
        });


        loadSuggested();

    }

    private void loadSuggested(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("savedBusiness")
                .get().addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        List<YelpBusinessResponse> saved_businesses = new ArrayList<>();

                        Log.d("TAG", "get failed with ", task.getException());

                        for(int i = 0; i<documents.size();i++){
                            HashMap<String, String> cat = (HashMap<String, String>) documents.get(i).get("businessCategory");
                            HashMap<String, String> loc = (HashMap<String, String>) documents.get(i).get("businessLocation");


                            List<YelpCategoryResponse> lst = new ArrayList<>();
                            lst.add(new YelpCategoryResponse(cat.get("alias"),cat.get("title")));
                            YelpLocationResponse location = new YelpLocationResponse(loc.get("address1"),loc.get("address2"),loc.get("address3"),loc.get("city"),loc.get("zip_code"),loc.get("country"),loc.get("state") );
                            YelpBusinessResponse bt =  new YelpBusinessResponse(
                                    (String) documents.get(i).get("businessID"),
                                    (String) documents.get(i).get("businessName"),
                                    (String)documents.get(i).get("businessImage"),
                                    lst,
                                    (double)documents.get(i).get("businessRating"),
                                    location
                            );
                            saved_businesses.add(bt);

                            Random rand = new Random();


                            suggestByCity(saved_businesses.get(rand.nextInt(saved_businesses.size())).getLocation().getCity());

                            YelpBusinessResponse rndBus = saved_businesses.get(rand.nextInt(saved_businesses.size()));
                            suggestByCategory(rndBus.getCategories().get(0).getTitle(), rndBus.getLocation().getCity());

                        }


                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                });


    }

    private void suggestByCity(String cityName){
        //Log.println(Log.ASSERT,"asd",cityName);

        Call<YelpSearchResponse> searchResponseCall= YelpApiClient.getApiService().findBusinessesByCategory(cityName,7,null);
        searchResponseCall.enqueue(new Callback<YelpSearchResponse>() {
            @Override
            public void onResponse(Call<YelpSearchResponse> call, Response<YelpSearchResponse> response) {
                List<YelpBusinessResponse> businesses = response.body().getBusinesses();
                suggestedByCity.addAll(businesses);
                mAdapterCity.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<YelpSearchResponse> call, Throwable t) {

            }
        });
    }

    private void suggestByCategory(String category, String city){
        List<String> categories = new ArrayList<>();
        categories.add(category);
        Call<YelpSearchResponse> searchResponseCall= YelpApiClient.getApiService().findBusinessesByCategory(city,7,categories);
        searchResponseCall.enqueue(new Callback<YelpSearchResponse>() {
            @Override
            public void onResponse(Call<YelpSearchResponse> call, Response<YelpSearchResponse> response) {
                List<YelpBusinessResponse> businesses = response.body().getBusinesses();
                suggestedByCategory.addAll(businesses);
                mAdapterCategory.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<YelpSearchResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}