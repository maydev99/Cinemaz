package com.bombadu.cinemaz.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
class Movie (
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "rating") var rating: Int,
    @ColumnInfo(name = "reviewer") var  reviewer: String,
    @ColumnInfo(name = "source") var source: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}