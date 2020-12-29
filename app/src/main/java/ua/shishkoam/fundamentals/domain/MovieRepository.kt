package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.data.Movie

interface MovieRepository {
    suspend fun getMovies() : List<Movie>
    fun getFavoriteFilms(): HashMap<String, Boolean>
    fun setFavoriteFilmState(id:String, isFavorite:Boolean)
}