package com.byundonghwan.interpark_week5.api


import com.byundonghwan.interpark_week5.model.BestCellerDto
import com.byundonghwan.interpark_week5.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey : String,
        @Query("query") keyword : String
    ):Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ):Call<BestCellerDto>

}