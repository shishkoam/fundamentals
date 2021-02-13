package ua.shishkoam.fundamentals.domain

import kotlinx.coroutines.flow.Flow
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

interface CacheRepository {

    fun getAllMovies(): Flow<List<Movie>>

    fun getMovie(id: Int): Flow<Movie>

    fun addMovies(movies: List<Movie>)
//    fun addMovies(movies: List<Movie>): List<Movie>

    suspend fun clearMovies()

    suspend fun addActors(id: Long, actors: List<Actor>)

    suspend fun getActors(id: Long): List<Actor>

}