package ua.shishkoam.fundamentals.data

import retrofit2.HttpException
import ua.shishkoam.fundamentals.MovieRetrofitInterface
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Configuration
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieRepositoryImpl(
    private val movieRetrofitInterface: MovieRetrofitInterface
) : MovieRepository {
    private val favoriteFilms: HashMap<String, Boolean> = HashMap()
    private var configuration: Configuration? = null
    private var genreMap: HashMap<Int, String> = HashMap()

    override suspend fun getMovies(): List<Movie> {
        try {
            val movies = movieRetrofitInterface.getNowPlaying()?.results ?: emptyList()
            val genres = getGenreMap()
            val configuration = getConfiguration()
            for (movie in movies) {
                configuration?.let { config ->
                    movie.setPosterFullImageUrl(config)
                    movie.setBackdropFullImageUrl(config)
                }
                movie.setGenresNames(genres)
            }
            return movies
        } catch (e: HttpException) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private suspend fun getGenreMap(): HashMap<Int, String> {
        if (genreMap.isNullOrEmpty()) {
            try {
                val genreList = movieRetrofitInterface.getGenreList()?.genres
                genreList?.let {
                    for (genre in genreList) {
                        genreMap[genre.id] = genre.name
                    }
                }
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }
        return genreMap
    }

    private suspend fun getConfiguration(): Configuration? {
        if (configuration == null) {
            try {
                configuration = movieRetrofitInterface.getConfiguration()
            } catch (e: HttpException) {
                e.printStackTrace()
            }
        }
        return configuration
    }

    override suspend fun getActors(id: Int): List<Actor> {
        try {
            val credits = movieRetrofitInterface.getMovieCredits(id)
            val config = getConfiguration()
            credits?.cast?.let { cast ->
                config?.let { config ->
                    for (actor in cast) {
                        actor.setProfileFullImageUrl(config)
                    }
                }
                return credits.cast
            }
        } catch (e: HttpException) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override fun getFavoriteFilms(): HashMap<String, Boolean> {
        return favoriteFilms
    }

    override fun setFavoriteFilmState(id: String, isFavorite: Boolean) {
        favoriteFilms[id] = isFavorite
    }

}