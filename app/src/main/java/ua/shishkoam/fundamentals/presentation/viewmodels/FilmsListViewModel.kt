package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.data.Movie
import ua.shishkoam.fundamentals.domain.MovieRepository

class FilmsListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var filmList: MutableLiveData<HashMap<Int, Movie>> = MutableLiveData<HashMap<Int, Movie>>()
    private var errorData: MutableLiveData<FilmsListError> = MutableLiveData<FilmsListError>()

    init {
        loadFilm()
    }

    val movies: LiveData<HashMap<Int, Movie>> get() = filmList
    val error: LiveData<FilmsListError> get() = errorData

    fun loadFilm() {
        viewModelScope.launch(exceptionHandler) {
            errorData.value = FilmsListError.WITHOUT_ERROR
            val list = movieRepository.getMovies()
            withContext(Dispatchers.Main) {
                val movies = HashMap<Int, Movie>()
                for (movie in list){
                    movies[movie.id] = movie
                }
                filmList.value = movies
                if (movies.isEmpty()) {
                    errorData.value = FilmsListError.LOAD_ERROR
                }
            }
        }
    }

    fun setLike(id: Int, isLiked: Boolean) {
        val oldMovie = filmList.value?.get(id)
        val newMovie = oldMovie?.clone()
        newMovie?.run{
            isFavorite = isLiked
            filmList.value?.put(id, this)
            filmList.postValue(filmList.value)
        }

    }

    private val exceptionHandler get() = CoroutineExceptionHandler { context, throwable ->
        viewModelScope.launch {
            throwable.printStackTrace()
            throwable.stackTrace
            showExceptionToUser()
        }
    }

    private suspend fun showExceptionToUser() =
        withContext(Dispatchers.Main) {
            errorData.value = FilmsListError.LOAD_ERROR
        }

    enum class FilmsListError {
        WITHOUT_ERROR, LOAD_ERROR, EMPTY_LIST
    }
}

