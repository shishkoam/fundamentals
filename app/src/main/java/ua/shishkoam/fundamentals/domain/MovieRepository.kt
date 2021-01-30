package ua.shishkoam.fundamentals.domain

import by.kirich1409.result.RequestResult
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

interface MovieRepository {
    suspend fun getMovies(): RequestResult<List<Movie>>
    suspend fun getMovies(page: Int): RequestResult<List<Movie>>
    suspend fun getActors(id: Int): RequestResult<List<Actor>>
    suspend fun getTotalPagesNumber(): Int
}