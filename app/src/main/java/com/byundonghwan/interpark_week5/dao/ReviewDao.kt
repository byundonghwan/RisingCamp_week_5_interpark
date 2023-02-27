package com.byundonghwan.interpark_week5.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.byundonghwan.interpark_week5.model.Review

@Dao
interface ReviewDao {
    @Query("SELECT * FROM review WHERE id == :id")
    fun getOneReview(id : Int) : Review

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 똑같은 데이터있을 시 대체.
    fun saveReview(review: Review)

}