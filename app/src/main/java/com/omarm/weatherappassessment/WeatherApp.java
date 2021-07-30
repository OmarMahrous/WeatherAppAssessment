package com.omarm.weatherappassessment;

import android.app.Application;

import com.google.android.libraries.places.api.Places;

public class WeatherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initAutocompleteFragment();
    }

    private void initAutocompleteFragment() {
        // Initialize the AutocompleteSupportFragment.

        Places.initialize(this, getString(R.string.map_api_key));

    }

}
