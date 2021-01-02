package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

interface MovieRepository {
    suspend fun getMovies() : List<Movie>
    suspend fun getActors(id : Int): List<Actor>
    fun getFavoriteFilms(): HashMap<String, Boolean>
    fun setFavoriteFilmState(id:String, isFavorite:Boolean)
}