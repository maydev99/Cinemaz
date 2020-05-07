package com.bombadu.cinemaz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bombadu.cinemaz.db.Movie
import com.bombadu.cinemaz.db.MovieDatabase
import com.bombadu.cinemaz.repository.MovieRepository

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MovieRepository
    private val allMovies: LiveData<List<Movie>>


    init {
        val movieDao = MovieDatabase.getDatabase(application, viewModelScope).movieDao()
        repository = MovieRepository(movieDao)
        allMovies = repository.allMovies
    }

    fun insertMovie(movie: Movie) {
        repository.insertMovie(movie)
    }

    fun deleAllMovies() {
        repository.deleteAllMovies()
    }

    fun deleteMovie(movie: Movie) {
        repository.deleteMovie(movie)
    }

    fun updateMovie(movie: Movie) {
        repository.updateMovie(movie)
    }

    fun getAllMovies(): LiveData<List<Movie>> {
        return  allMovies
    }

    fun getAllMoviesByRating(): LiveData<List<Movie>> {
        return allMovies
    }
}