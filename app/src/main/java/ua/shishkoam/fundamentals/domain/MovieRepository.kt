package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.data.Movie

interface MovieRepository {
    suspend fun getMovies() : List<Movie>
    val favoriteFilms: HashMap<String, Boolean>
}