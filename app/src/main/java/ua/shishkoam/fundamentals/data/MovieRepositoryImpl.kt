package ua.shishkoam.fundamentals.data

import android.content.Context
import ua.shishkoam.fundamentals.domain.MovieRepository

class MovieRepositoryImpl(val context : Context) : MovieRepository {
    private val favoriteFilms: HashMap<String, Boolean> = HashMap()

    override suspend fun getMovies() : List<Movie> {
        return loadMovies(context)
    }

    override fun getFavoriteFilms(): HashMap<String, Boolean> {
        return favoriteFilms
    }

    override fun setFavoriteFilmState(id: String, isFavorite: Boolean) {
        favoriteFilms[id] = isFavorite
    }

}