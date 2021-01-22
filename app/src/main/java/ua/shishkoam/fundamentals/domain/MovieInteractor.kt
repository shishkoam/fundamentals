package ua.shishkoam.fundamentals.domain

import by.kirich1409.result.RequestResult
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

interface MovieInteractor {
    suspend fun getMovies() : RequestResult<List<Movie>>
    suspend fun getMoreMovies() : RequestResult<List<Movie>>
    suspend fun getActors(id : Int): RequestResult<List<Actor>>
    fun getFavoriteFilms(): HashMap<String, Boolean>
    fun setFavoriteFilmState(id:String, isFavorite:Boolean)
    fun getTotalPageNumber(): Int
    fun getCurrentPageNumber(): Int
}