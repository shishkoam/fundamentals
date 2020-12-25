package ua.shishkoam.fundamentals.data

import android.content.Context
import ua.shishkoam.fundamentals.domain.MovieRepository

class MovieRepositoryImpl(val context : Context) : MovieRepository {
    override suspend fun getMovies() : List<Movie> {
        return loadMovies(context)
    }

    override val favoriteFilms: HashMap<String, Boolean>
        get() = HashMap()
}