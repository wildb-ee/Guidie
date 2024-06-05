package com.fayaz.guidie.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fayaz.guidie.R;
import com.fayaz.guidie.activity.CityDetailsActivity;
import com.fayaz.guidie.adapter.City;
import com.fayaz.guidie.adapter.CityListAdapter;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<City> cities = new ArrayList<City>();

    private List<City> selected_cities = new ArrayList<City>();
    private SearchBar searchBar;
    private SearchView searchView;

    private RecyclerView recyclerView;
    private CityListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public CityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityFragment newInstance(String param1, String param2) {
        CityFragment fragment = new CityFragment();
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

        try {
            this.loadJsonCities();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBar = view.findViewById(R.id.city_search_bar);
        searchView = view.findViewById(R.id.city_search_view);
        recyclerView = view.findViewById(R.id.selected_cities_list);
        recyclerView.setNestedScrollingEnabled(false);
        selected_cities.addAll(cities);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new CityListAdapter(selected_cities, this.getContext());
        recyclerView.setAdapter(mAdapter);

        searchView
                .getEditText()
                .setOnEditorActionListener(
                        (v, actionId, event) -> {
                            searchBar.setText(searchView.getText());
                            searchView.hide();
                            selected_cities.clear();
                            selected_cities.addAll(cities.stream()
                                    .filter(city -> city.getName().toLowerCase().contains(searchView.getText().toString().toLowerCase()))
                                    .collect(Collectors.toList()));
                            mAdapter.notifyDataSetChanged();

                            return false;
                        });

        mAdapter.setOnClickListener(new CityListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, City model) {

                Intent intent = new Intent(getContext(), CityDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("cityName", model.getName());
                bundle.putString("cityCountry", model.getCountry());
                bundle.putString("cityImage", model.getImage());
                bundle.putString("cityLat", Double.toString(model.getLatitude()));
                bundle.putString("cityLon", Double.toString(model.getLongitude()));
                intent.putExtras(bundle);
                startActivity(intent);

            }

        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false);
    }


    private void loadJsonCities() throws IOException, JSONException {
        InputStream inputStream = getContext().getAssets().open("cities.json");
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();

        String  json = new String(buffer, StandardCharsets.UTF_8);
        JSONArray jsonArray = new JSONArray(json);
        int cnt = jsonArray.length();

        for (int i = 0 ; i< cnt; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            cities.add(new City(jsonObject.getString("name"),
                    jsonObject.getString("image"),
                    Double.parseDouble(jsonObject.getString("latitude")),
                    Double.parseDouble(jsonObject.getString("longitude")),jsonObject.getString("country") ));
        }


    }


}