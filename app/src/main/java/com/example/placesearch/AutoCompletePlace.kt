package com.example.placesearch

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_auto_complete_place.*
import java.util.*

class AutoCompletePlace : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete_place)

        Places.initialize(applicationContext,"AIzaSyAzARqfJquT8n2i3nIcVa3GPH-Zvqzv9Lo")

        edtAddress.setOnClickListener {
            var fieldList : List<Place.Field> = Arrays.asList(Place.Field.ADDRESS , Place.Field.LAT_LNG , Place.Field.NAME)
            var intent : Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY , fieldList).build(this)
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK)
        {
            var place : Place = Autocomplete.getPlaceFromIntent(data!!)
            edtAddress.setText(place.address)
            txtView.setText(String.format("Locality Name : %s", place.name))
            txtView1.setText(place.latLng.toString())
        }
        else if(resultCode == AutocompleteActivity.RESULT_ERROR)
        {
            var status : Status = Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(applicationContext, status.statusMessage , Toast.LENGTH_LONG).show()
            Log.d("TAG", "onActivityResult: "+status.statusMessage)
        }
    }
}