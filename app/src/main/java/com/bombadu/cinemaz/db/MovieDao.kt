package com.bombadu.cinemaz.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Insert
    fun insertMovie(movie: Movie)

    @Update
    fun updateMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movie_table")
    fun deleteAllMovies()

    @Query("SELECT * FROM movie_table ORDER BY name ASC")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_table ORDER BY rating DESC")
    fun getAllMoviesByRating(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_table ORDER BY reviewer ASC")
    fun getAllMoviesByRatingByReviewer(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_table ORDER BY source ASC")
    fun getAllMoviesBySource(): LiveData<List<Movie>>


}