package com.example.placesearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("place/nearbysearch/json?")
    fun Places(@Query(value = "type", encoded = true)  type : String, @Query(value = "location", encoded = true) location : String, @Query(value = "name", encoded = true) name :String, @Query(value = "opennow", encoded = true) opennow : Boolean, @Query(value = "rankby", encoded = true) rankby : String, @Query(value = "key", encoded = true) key :String) : Call<PlacesPojo.Root>

    @GET("distancematrix/json")
    fun Distance(@Query("key") key :String, @Query("origins") origins : String, @Query("destinations") destinations : String) : Call<ResultDistanceMatrix>
}