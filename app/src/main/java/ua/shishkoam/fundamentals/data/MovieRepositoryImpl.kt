package ua.shishkoam.fundamentals.data

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.shishkoam.fundamentals.MovieRetrofitInterface
import ua.shishkoam.fundamentals.domain.MovieRepository

class MovieRepositoryImpl(
    private val movieRetrofitInterface: MovieRetrofitInterface
) : MovieRepository {
    private val favoriteFilms: HashMap<String, Boolean> = HashMap()

    override suspend fun getMovies(): List<Movie> {
        val movies = movieRetrofitInterface.getTopRated().results ?: emptyList()
        for (movie in movies) {
            movie.configuration = getConfiguration(movieId = movie.id)
        }
        return movies
    }

    override suspend fun getConfiguration(movieId: Int) : Configuration{
        return movieRetrofitInterface.getConfiguration()
    }

    override fun getFavoriteFilms(): HashMap<String, Boolean> {
        return favoriteFilms
    }

    override fun setFavoriteFilmState(id: String, isFavorite: Boolean) {
        favoriteFilms[id] = isFavorite
    }

}