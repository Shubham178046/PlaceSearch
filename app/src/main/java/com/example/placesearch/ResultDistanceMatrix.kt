package com.example.placesearch

import com.google.gson.annotations.SerializedName


class ResultDistanceMatrix {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("rows")
    var rows: List<InfoDistanceMatrix>? = null

    inner class InfoDistanceMatrix {
        @SerializedName("elements")
        var elements: List<*>? = null

        inner class DistanceElement {
            @SerializedName("status")
            var status: String? = null

            @SerializedName("duration")
            var duration: ValueItem? = null

            @SerializedName("distance")
            var distance: ValueItem? = null
        }

        inner class ValueItem {
            @SerializedName("value")
            var value: Long = 0

            @SerializedName("text")
            var text: String? = null
        }
    }
}
