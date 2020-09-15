package com.example.placesearch

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class PlacesPojo {
    inner class Root : Serializable {
        @SerializedName("results")
        var customA: List<CustomA> = ArrayList()

        @SerializedName("status")
        var status: String? = null

        @SerializedName("error_message")
        var error_message: String? = null
    }

    inner class CustomA : Serializable {
        @SerializedName("geometry")
        var geometry: Geometry? = null

        @SerializedName("vicinity")
        var vicinity: String? = null

        @SerializedName("name")
        var name: String? = null
    }

    inner class Geometry : Serializable {
        @SerializedName("location")
        var locationA: LocationA? = null
    }

    inner class LocationA : Serializable {
        @SerializedName("lat")
        var lat: String? = null

        @SerializedName("lng")
        var lng: String? = null
    }
}
