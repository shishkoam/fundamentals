package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.data.Configuration
import ua.shishkoam.fundamentals.data.Movie

interface MovieRepository {
    suspend fun getMovies() : List<Movie>
    suspend fun getConfiguration(movieId: Int) : Configuration
    fun getFavoriteFilms(): HashMap<String, Boolean>
    fun setFavoriteFilmState(id:String, isFavorite:Boolean)
}