package com.byundonghwan.interpark_week5.model

import com.google.gson.annotations.SerializedName

data class BestCellerDto(
    @SerializedName("title") val title : String,
    @SerializedName("item")  val books : List<Book>,
)