package ua.shishkoam.fundamentals.data

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
        val movies = movieRetrofitInterface.getTopRated().results ?: emptyList()
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
    }

    private suspend fun getGenreMap(): HashMap<Int, String> {
        if (genreMap.isNullOrEmpty()) {
            val genreList = movieRetrofitInterface.getGenreList().genres
            genreList?.let {
                for (genre in genreList) {
                    genreMap[genre.id] = genre.name
                }
            }
        }
        return genreMap
    }

    private suspend fun getConfiguration(): Configuration? {
        if (configuration == null) {
            configuration = movieRetrofitInterface.getConfiguration()
        }
        return configuration
    }

    override suspend fun getActors(id : Int): List<Actor> {
        val credits = movieRetrofitInterface.getMovieCredits(id)
        val configuration = getConfiguration()
        configuration?.let { config ->
            for (actor in credits.cast) {
                actor.setProfileFullImageUrl(config)
            }
        }
        return credits.cast
    }

    override fun getFavoriteFilms(): HashMap<String, Boolean> {
        return favoriteFilms
    }

    override fun setFavoriteFilmState(id: String, isFavorite: Boolean) {
        favoriteFilms[id] = isFavorite
    }

}