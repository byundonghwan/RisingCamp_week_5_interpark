package com.byundonghwan.interpark_week5.model

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey val uid : Int?, // Room 기본키 설정.
    @ColumnInfo(name = "Keyword") val keyword : String? // Room 컬럼 정보 설정.
)