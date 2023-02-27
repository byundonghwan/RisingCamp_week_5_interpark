package com.byundonghwan.interpark_week5.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Book(
    @SerializedName("itemId") val id : Long,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("coverSmallUrl") val coverSmallUrl : String
) : Serializable