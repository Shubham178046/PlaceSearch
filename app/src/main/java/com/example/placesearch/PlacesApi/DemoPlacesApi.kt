package com.example.placesearch.PlacesApi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.placesearch.R
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class DemoPlacesApi : AppCompatActivity() {
    var placesClient: PlacesClient? = null
    var apiKey = "AIzaSyAj3Ha5Cc6EhAldadzWnXuPN1fLQ7hsqfY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_places_api)
        if (!Places.isInitialized()) {
            Places.initialize(this@DemoPlacesApi, apiKey);
            placesClient = Places.createClient(this);
            val   autocompleteSupportFragment : AutocompleteSupportFragment =
            getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
            autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

            autocompleteSupportFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
                override fun onPlaceSelected(place: Place) {
                    val latLng : LatLng = place.latLng!!

                    Toast.makeText(this@DemoPlacesApi, ""+latLng.latitude, Toast.LENGTH_SHORT).show();
                }

                override fun onError(status: Status) {
                    Toast.makeText(this@DemoPlacesApi, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }

            })
        }
    }
}