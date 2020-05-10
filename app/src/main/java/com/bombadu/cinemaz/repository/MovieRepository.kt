package com.bombadu.cinemaz.repository

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.bombadu.cinemaz.db.Movie
import com.bombadu.cinemaz.db.MovieDao

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies: LiveData<List<Movie>> = movieDao.getAllMovies()
    val allMoviesByRating: LiveData<List<Movie>> = movieDao.getAllMoviesByRating()
    val allMoviesByReviewer: LiveData<List<Movie>> = movieDao.getAllMoviesByRatingByReviewer()
    val allMoviesBySource: LiveData<List<Movie>> = movieDao.getAllMoviesBySource()

    fun insertMovie(movie: Movie) {
        InsertMovieAsyncTask(
            movieDao
        ).execute(movie)
    }

    fun deleteAllMovies() {
        DeleteAllMoviesAsyncTask(
            movieDao
        ).execute()

    }

    fun deleteMovie(movie: Movie) {
        DeleteMovieAsyncTask(
            movieDao
        ).execute(movie)

    }

    fun updateMovie(movie: Movie) {
        UpdateMovieAsyncTask(
            movieDao
        ).execute(movie)
    }

    private class InsertMovieAsyncTask(val movieDao: MovieDao) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg movie: Movie?) {
            movieDao.insertMovie(movie[0]!!)
        }
    }

    private class DeleteAllMoviesAsyncTask(val movieDao: MovieDao) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit) {
            movieDao.deleteAllMovies()
        }
    }

    private class DeleteMovieAsyncTask(val movieDao: MovieDao) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg movie: Movie?) {
            movieDao.deleteMovie(movie[0]!!)
        }
    }

    private class UpdateMovieAsyncTask(val movieDao: MovieDao) : AsyncTask<Movie, Unit, Unit>() {
        override fun doInBackground(vararg movie: Movie?) {
            movieDao.updateMovie(movie[0]!!)
        }
    }
}