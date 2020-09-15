package com.example.placesearch

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.placesearch.ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement
import com.google.android.gms.maps.model.LatLng
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.activity_place_api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlaceApi : AppCompatActivity() {
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected: ArrayList<String> = ArrayList()
    private val permissions: ArrayList<String> = ArrayList()
    private val ALL_PERMISSIONS_RESULT = 101
    var storeModels: List<StoreModel>? = null
  //  var ApiClient.getService(): ApiInterface? = null
    var latLngString: String? = null
    var latLng: LatLng? = null
    var results: List<PlacesPojo.CustomA>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_api)

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest!!.size > 0)
            // requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                requestPermissions(
                    arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                    ALL_PERMISSIONS_RESULT
                )
            else {
                fetchLocation();
            }
        } else {
            fetchLocation();
        }
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true);
        recyclerView.layoutManager = LinearLayoutManager(this)
        button.setOnClickListener {
            val split: Array<String> = editText.text.toString().trim().split("\\s+").toTypedArray()
            if (split.size != 2) {
                Toast.makeText(
                    getApplicationContext(),
                    "Please enter text in the required format",
                    Toast.LENGTH_SHORT
                ).show();
                Log.d("TAG", "onCreate: "+split[0] + " " + split[0])
            } else {
                fetchStores("restaurant", "mcdonalds");
            }

            fetchStores("restaurant", "mcdonalds");
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                for (perms in permissionsToRequest!!) {
                    if (!hasPermission(perms!!)) {
                        permissionsRejected.add(perms!!)
                    }
                }
                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(
                                            permissionsRejected.toTypedArray(),
                                            ALL_PERMISSIONS_RESULT
                                        )
                                    }
                                })
                            return
                        }
                    }
                } else {
                    fetchLocation()
                }
            }
        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun fetchStores(
        placeType: String,
        businessName: String
    ) {
        /**
         * For Locations In India McDonalds stores aren't returned accurately
         */
        Log.d("DATA", "fetchStores: "+placeType + latLngString!! +businessName+  ApiClient.GOOGLE_PLACE_API_KEY)
        Log.d("DATAs", "fetchStores: "+placeType + latLngString +businessName+ApiClient.GOOGLE_PLACE_API_KEY)
        //Call<PlacesPOJO.Root> call = ApiClient.getService().doPlaces(placeType, latLngString,"\""+ businessName +"\"", true, "distance", APIClient.GOOGLE_PLACE_API_KEY);
       ApiClient.getService()!!.Places(
            placeType,
            latLngString!!,
            businessName,
            true,
            "distance",
            ApiClient.GOOGLE_PLACE_API_KEY
        ).
        enqueue(object : Callback<PlacesPojo.Root?> {
            override fun onResponse(
                call: Call<PlacesPojo.Root?>?,
                response: Response<PlacesPojo.Root?>
            ) {
                val root: PlacesPojo.Root = response.body()!!
                if (response.isSuccessful()) {
                    if (root.status.equals("OK")) {
                        results = root.customA
                        storeModels = ArrayList()
                        for (i in results!!.indices) {
                            if (i == 10) break
                            val info: PlacesPojo.CustomA = results!![i]
                            fetchDistance(info)
                        }
                    } else {
                        Log.d("Status", "onResponse: "+response.body()!!.status + " " + response.body()!!.error_message)
                        Toast.makeText(
                            applicationContext,
                            "No matches found near you",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (response.code() !== 200) {
                    Toast.makeText(
                        applicationContext,
                        "Error " + response.code().toString() + " found.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PlacesPojo.Root?>, t: Throwable?) {
                // Log error here since request failed
                call.cancel()
            }
        })
    }


    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String>? {
        val result: ArrayList<String> = ArrayList()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    private fun fetchLocation() {
        SmartLocation.with(this).location()
            .oneFix()
            .start { location ->
                latLngString = location.latitude.toString() + "," + location.longitude
                latLng = LatLng(location.latitude, location.longitude)
            }
    }

    private fun fetchDistance(info: PlacesPojo.CustomA) {
        val call: Call<ResultDistanceMatrix> = ApiClient.getService()!!.Distance(
            ApiClient.GOOGLE_PLACE_API_KEY,
            latLngString!!,
            info.geometry!!.locationA!!.lat.toString() + "," + info.geometry!!.locationA!!.lng
        )
        call.enqueue(object : Callback<ResultDistanceMatrix> {
            override fun onResponse(
                call: Call<ResultDistanceMatrix>,
                response: Response<ResultDistanceMatrix>
            ) {
                val resultDistance = response.body()
                if ("OK".equals(resultDistance!!.status, ignoreCase = true)) {
                    val infoDistanceMatrix = resultDistance.rows!![0]
                    val distanceElement: DistanceElement =
                        infoDistanceMatrix.elements!![0] as DistanceElement
                    if ("OK".equals(distanceElement.status, ignoreCase = true)) {
                        val itemDuration = distanceElement.duration
                        val itemDistance = distanceElement.distance
                        val totalDistance = itemDistance!!.text.toString()
                        val totalDuration = itemDuration!!.text.toString()
                        (storeModels as ArrayList<StoreModel>).add(
                            StoreModel(
                                info.name!!,
                                info.vicinity!!,
                                totalDistance,
                                totalDuration
                            )
                        )

                        if (storeModels!!.size == 10 || storeModels!!.size == results!!.size) {
                            val adapterStores =
                                PlaceAdapter(applicationContext, results!!, storeModels!!)
                            recyclerView.setAdapter(adapterStores)
                        }
                    }
                }
            }

            override fun onFailure(
                call: Call<ResultDistanceMatrix>,
                t: Throwable
            ) {
                call.cancel()
            }
        })
    }


}

