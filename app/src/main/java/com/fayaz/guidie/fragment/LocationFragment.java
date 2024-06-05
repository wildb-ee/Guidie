package com.fayaz.guidie.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.fayaz.guidie.activity.NavActivity;
import com.fayaz.guidie.adapter.BusinessListAdapter;
import com.fayaz.guidie.adapter.CityListAdapter;
import com.fayaz.guidie.dto.YelpBusinessResponse;
import com.fayaz.guidie.dto.YelpCategoryResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private BusinessListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth mAuth;


    private List<YelpBusinessResponse> saved_businesses  = new ArrayList<YelpBusinessResponse>();;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
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
        recyclerView = view.findViewById(R.id.saved_shops);
        recyclerView.setNestedScrollingEnabled(false);
        mAuth=FirebaseAuth.getInstance();


        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BusinessListAdapter(saved_businesses, this.getContext());
        recyclerView.setAdapter(mAdapter);

        loadSavedBusinesses();

        mAdapter.setOnClickListener((position, model) -> {
            Intent intent = new Intent(getContext(), BusinessDetailsActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("businessID", model.getId());
            bundle1.putString("businessName", model.getName());
            bundle1.putString("businessName", model.getName());
            bundle1.putBoolean("isLocation", true);


            intent.putExtras(bundle1);


            startActivity(intent);
        });


    }

    private void loadSavedBusinesses(){

        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        db.collection("users").document(user.getUid()).collection("savedBusiness")
                .get().addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();


                        Log.d("TAG", "get failed with ", task.getException());

                        for(int i = 0; i<documents.size();i++){
                            HashMap<String, String> cat = (HashMap<String, String>) documents.get(i).get("businessCategory");
                            List<YelpCategoryResponse> lst = new ArrayList<>();
                            lst.add(new YelpCategoryResponse(cat.get("alias"),cat.get("title")));
                            YelpBusinessResponse bt =  new YelpBusinessResponse(
                                    (String) documents.get(i).get("businessID"),
                                    (String) documents.get(i).get("businessName"),
                                    (String)documents.get(i).get("businessImage"),
                                    lst,
                                    (double)documents.get(i).get("businessRating")
                            );
                            saved_businesses.add(bt);
                            mAdapter.notifyItemInserted(i);
                        }


                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }
}