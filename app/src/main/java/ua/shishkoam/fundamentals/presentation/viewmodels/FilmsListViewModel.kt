package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.kirich1409.result.asSuccess
import by.kirich1409.result.isSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.Movie

class FilmsListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var filmList: MutableLiveData<HashMap<Int, Movie>> =
        MutableLiveData<HashMap<Int, Movie>>()
    private var errorData: MutableLiveData<RepositoryError> = MutableLiveData<RepositoryError>()

    init {
        loadFilm()
    }

    val movies: LiveData<HashMap<Int, Movie>> get() = filmList
    val error: LiveData<RepositoryError> get() = errorData

    fun loadFilm() {
        viewModelScope.launch(exceptionHandler) {
            errorData.value = null
            val result = movieRepository.getMovies()
            withContext(Dispatchers.Main) {
                if (result.isSuccess()) {
                    val movies = HashMap<Int, Movie>()
                    for (movie in result.asSuccess().value) {
                        movies[movie.id] = movie
                    }
                    filmList.value = movies
                } else {
                    errorData.value = RepositoryError.LOAD_ERROR
                }
            }
        }
    }

    fun setLike(id: Int, isLiked: Boolean) {
        val oldMovie = filmList.value?.get(id)
        val newMovie = oldMovie?.copy()
        newMovie?.run {
            isFavorite = isLiked
            filmList.value?.put(id, this)
            filmList.postValue(filmList.value)
        }

    }

    private val exceptionHandler
        get() = CoroutineExceptionHandler { context, throwable ->
            viewModelScope.launch {
                throwable.printStackTrace()
                throwable.stackTrace
                showExceptionToUser()
            }
        }

    private suspend fun showExceptionToUser() =
        withContext(Dispatchers.Main) {
            errorData.value = RepositoryError.LOAD_ERROR
        }
}

