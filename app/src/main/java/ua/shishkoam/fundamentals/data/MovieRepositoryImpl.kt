package ua.shishkoam.fundamentals.data

import by.kirich1409.result.RequestResult
import by.kirich1409.result.asFailure
import by.kirich1409.result.asSuccess
import by.kirich1409.result.isSuccess
import ua.shishkoam.fundamentals.data.dto.Configuration
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieRepositoryImpl(
    private val movieRetrofitInterface: MovieRetrofitInterface
) : MovieRepository {
    private val favoriteFilms: HashMap<String, Boolean> = HashMap()
    private var configuration: Configuration? = null
    private var genreMap: HashMap<Int, String> = HashMap()

    override suspend fun getMovies(): RequestResult<List<Movie>> {
        val movies = ArrayList<Movie>()
        val result = movieRetrofitInterface.getNowPlaying()
        return if (result.isSuccess()) {
            val moviesDTO = result.asSuccess().value?.results ?: emptyList()
            val genres = getGenreMap()
            val configuration = getConfiguration()
            for (movie in moviesDTO) {
                movies.add(movie.toDomainMovie(configuration, genres))
            }
            RequestResult.Success.Value(movies)
        } else {
            result.asFailure()
        }
    }

    private suspend fun getGenreMap(): HashMap<Int, String> {
        if (genreMap.isNullOrEmpty()) {
            val result = movieRetrofitInterface.getGenreList()
            if (result.isSuccess()) {
                val genreList = result.asSuccess().value?.genres
                genreList?.let {
                    for (genre in genreList) {
                        genreMap[genre.id] = genre.name
                    }
                }
            } else {
                print(result.asFailure().error)
            }
        }
        return genreMap
    }

    private suspend fun getConfiguration(): Configuration? {
        if (configuration == null) {
            val result = movieRetrofitInterface.getConfiguration()
            if (result.isSuccess()) {
                configuration = result.asSuccess().value
            } else {
                print(result.asFailure().error)
            }
        }
        return configuration
    }

    override suspend fun getActors(id: Int): RequestResult<List<Actor>> {
        val result = movieRetrofitInterface.getMovieCredits(id)
        val actors: ArrayList<Actor> = ArrayList()
        if (result.isSuccess()) {
            val credits = result.asSuccess().value
            val config = getConfiguration()
            credits?.cast?.let { cast ->
                config?.let { config ->
                    for (actor in cast) {
                        actors.add(actor.toDomainActor(config))
                    }
                }
                return RequestResult.Success.Value(actors)
            }
        } else {
            return result.asFailure()
        }
        return RequestResult.Success.Value(emptyList())
    }

    override fun getFavoriteFilms(): HashMap<String, Boolean> {
        return favoriteFilms
    }

    override fun setFavoriteFilmState(id: String, isFavorite: Boolean) {
        favoriteFilms[id] = isFavorite
    }

}