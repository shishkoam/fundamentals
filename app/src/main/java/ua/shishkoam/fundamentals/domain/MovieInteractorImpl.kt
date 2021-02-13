package ua.shishkoam.fundamentals.domain

import by.kirich1409.result.RequestResult
import by.kirich1409.result.asSuccess
import by.kirich1409.result.isFailure
import by.kirich1409.result.isSuccess
import kotlinx.coroutines.flow.Flow
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieInteractorImpl(
    val movieRepository: MovieRepository,
    val cacheRepository: CacheRepository
) : MovieInteractor {
    private val favoriteFilms: HashMap<String, Boolean> = HashMap()
    private var currentPage: Int = 1
    private var totalPages: Int = 1

    override suspend fun getMovies(): RequestResult<Flow<List<Movie>>> {
        currentPage = 1//save page number
        val result = loadMovies(currentPage)
        val roomResult = cacheRepository.getAllMovies()
        if (result.isFailure()) {
            return result
        } else {
            return RequestResult.Success.Value(roomResult)
        }
    }

    override suspend fun getMovie(id: Int): RequestResult<Flow<Movie>> {
        val roomResult = cacheRepository.getMovie(id)
        return RequestResult.Success.Value(roomResult)
    }

    override suspend fun updateMoviesInDb() {
        val movies = ArrayList<Movie>()
        for (pageNumber in 1..currentPage) {
            val result = loadMovies(pageNumber)
            if (result.isSuccess()) {
                movies.addAll(result.asSuccess().value)
            }
        }
        if (movies.isNotEmpty()) {
            cacheRepository.clearMovies()
            cacheRepository.addMovies(movies)
        }
    }

    private suspend fun loadMovies(page: Int): RequestResult<List<Movie>> {
        val result = movieRepository.getMovies(page)
        if (result.isSuccess()) {
            totalPages = movieRepository.getTotalPagesNumber()
            cacheRepository.addMovies(result.asSuccess().value)
        }
        return result
    }

    override suspend fun getMoreMovies(): RequestResult<List<Movie>> {
        currentPage++
        return loadMovies(currentPage)
    }

    override suspend fun getActors(id: Int): RequestResult<List<Actor>> {
        val result = movieRepository.getActors(id)
        if (result.isSuccess()) {
            cacheRepository.addActors(id.toLong(), result.asSuccess().value)
        } else {
            val actors = cacheRepository.getActors(id.toLong())
        }
        return result
    }

    override fun getFavoriteFilms(): HashMap<String, Boolean> {
        return favoriteFilms
    }

    override fun setFavoriteFilmState(id: String, isFavorite: Boolean) {
        favoriteFilms[id] = isFavorite
    }

    override fun getTotalPageNumber(): Int {
        return totalPages
    }

    override fun getCurrentPageNumber(): Int {
        return currentPage
    }
}