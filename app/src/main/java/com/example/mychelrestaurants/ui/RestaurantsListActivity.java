package com.example.mychelrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychelrestaurants.Constants;
import com.example.mychelrestaurants.R;
import com.example.mychelrestaurants.adapters.RestaurantListAdapter;
import com.example.mychelrestaurants.models.Restaurant;
import com.example.mychelrestaurants.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
//public class RestaurantsListActivity extends AppCompatActivity {
//    private SharedPreferences mSharedPreferences;
//    private SharedPreferences.Editor mEditor;
//    private String mRecentAddress;
//    private static final String TAG = RestaurantsListActivity.class.getSimpleName();
//
//    @BindView(R.id.recyclerView)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.errorTextView)
//    TextView mErrorTextView;
//    @BindView(R.id.progressBar)
//    ProgressBar mProgressBar;
//
//    private RestaurantListAdapter mAdapter;
//
//    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_restaurants);
//        ButterKnife.bind(this);
//
//        Intent intent = getIntent();
//        String location = intent.getStringExtra("location");
//
////        getRestaurants(location);
//
//        YelpApi client = YelpClient.getClient();
//
//        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");
//
//        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
//            @Override
//            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {
//                hideProgressBar();
//
//                if (response.isSuccessful()) {
////                    restaurants = response.body().getBusinesses();
////                    mAdapter = new RestaurantListAdapter(RestaurantsListActivity.this, restaurants);
////                    mRecyclerView.setAdapter(mAdapter);
////                    RecyclerView.LayoutManager layoutManager =
////                            new LinearLayoutManager(RestaurantsListActivity.this);
////                    mRecyclerView.setLayoutManager(layoutManager);
////                    mRecyclerView.setHasFixedSize(true);
//
//                    showRestaurants();
//                } else {
//                    showUnsuccessfulMessage();
//                }
//            }
//            private void addToSharedPreferences(String location) {
//                mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
//            }
//
//            @Override
//            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
//                hideProgressBar();
//                showFailureMessage();
//            }
//
//        });
//    }
//
//    private void showFailureMessage() {
//        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
//        mErrorTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void showUnsuccessfulMessage() {
//        mErrorTextView.setText("Something went wrong. Please try again later");
//        mErrorTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void showRestaurants() {
//        mRecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    private void hideProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
//    }
//
////    public void saveLocationToFirebase(String location) {
////        mSearchedLocationReference.push().setValue(location);
////    }
//}
class RestaurantListActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private RestaurantListAdapter mAdapter;
    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        getRestaurants(location);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);

        if (mRecentAddress != null) {
            getRestaurants(mRecentAddress);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();

        yelpService.findRestaurants(location, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                RestaurantListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantListActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    private void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }

}

