package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

interface CacheRepository {

    suspend fun getAllMovies(): List<Movie>

    suspend fun addMovies(movies: List<Movie>): List<Movie>

    suspend fun clearMovies()

    suspend fun addActors(id: Long, actors: List<Actor>)

    suspend fun getActors(id: Long): List<Actor>

}