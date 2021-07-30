package com.omarm.weatherappassessment.Acivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.omarm.weatherappassessment.Adapters.CityListAdapter;
import com.omarm.weatherappassessment.R;
import com.omarm.weatherappassessment.Utils.Constants;
import com.omarm.weatherappassessment.Utils.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private final String TAG = getClass().getSimpleName();

    private ConstraintLayout mMainRootView;


    private RecyclerView mCityRecView;
    private CityListAdapter listAdapter;
    private ArrayList<String> cityNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


        addSwipeHelperToRecyclerView();

        initListAdapter();

        populateCityNameData();

    }


    private void initViews() {
        mMainRootView = (ConstraintLayout) findViewById(R.id.main_root_view);

        mCityRecView = (RecyclerView) findViewById(R.id.city_rec_view);
        mCityRecView.setLayoutManager(new LinearLayoutManager(this));
        mCityRecView.setItemAnimator(new DefaultItemAnimator());
    }

    private void addSwipeHelperToRecyclerView() {
// adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END,
                this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mCityRecView);
    }

    private void initListAdapter() {

        listAdapter = new CityListAdapter(this);

        cityNameList = listAdapter.getCityNameList();
    }

    public void populateCityNameData() {
        listAdapter.addItem("London, UK", 0);
        mCityRecView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_search_city) {
            callPlaceAutocompleteFragmentIntent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void callPlaceAutocompleteFragmentIntent() {

        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                .build(this);


        startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
//                String city_address = place.getLatLng();


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (requestCode == RESULT_CANCELED) {

            }
        }


    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CityListAdapter.CityViewHolder) {
            // get the removed item name to display it in snack bar
            String name = ((String) cityNameList.get(viewHolder.getAdapterPosition()));

            // backup of removed item for undo purpose
            final String deletedItem = (String) cityNameList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            listAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(mMainRootView, name + " is deleted !", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    listAdapter.addItem(deletedItem, deletedIndex);

                    mCityRecView.smoothScrollToPosition(deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }
}