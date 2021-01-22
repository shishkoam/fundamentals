package ua.shishkoam.fundamentals.data

import by.kirich1409.result.RequestResult
import by.kirich1409.result.asSuccess
import by.kirich1409.result.isFailure
import by.kirich1409.result.isSuccess
import ua.shishkoam.fundamentals.data.room.RoomRepository
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieInteractorImpl(
    val movieRepository: MovieRepository,
    val roomRepository: RoomRepository
) : MovieInteractor {
    private val favoriteFilms: HashMap<String, Boolean> = HashMap()
    private var currentPage: Int = 1
    private var totalPages: Int = 1

    override suspend fun getMovies(): RequestResult<List<Movie>> {
        currentPage = 1//save page number
        val result = getMovies(currentPage)
        if (result.isFailure()) {
            val roomResult = roomRepository.getAllMovies()
            if (!roomResult.isNullOrEmpty()) {
                return RequestResult.Success.Value(roomResult)
            }
        }
        return result
    }

    private suspend fun getMovies(page: Int): RequestResult<List<Movie>> {
        val result = movieRepository.getMovies(page)
        if (result.isSuccess()) {
            totalPages = movieRepository.getTotalPagesNumber()
            roomRepository.addMovies(result.asSuccess().value)
        }
        return result
    }

    override suspend fun getMoreMovies(): RequestResult<List<Movie>> {
        currentPage++
        return getMovies(currentPage)
    }

    override suspend fun getActors(id: Int): RequestResult<List<Actor>> {
        val result = movieRepository.getActors(id)
        if (result.isSuccess()) {
            roomRepository.addActors(id.toLong(), result.asSuccess().value)
        } else {
            val actors = roomRepository.getActors(id.toLong())
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