package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.utils.InitMutableLiveData

class FilmsListViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    init {
        loadFilm()
    }

    private var filmList: InitMutableLiveData<HashMap<Int, Movie>> = InitMutableLiveData<HashMap<Int,Movie>>()
    private var errorData: MutableLiveData<FilmsListError> = MutableLiveData<FilmsListError>()

    val movies: LiveData<HashMap<Int,Movie>> get() = filmList
    val error: LiveData<FilmsListError> get() = errorData

    fun loadFilm() {
        viewModelScope.launch(exceptionHandler) {
            val list = movieRepository.getMovies()
            withContext(Dispatchers.Main) {
                val movies = HashMap<Int, Movie>()
                for (movie in list){
                    movies[movie.id] = movie
                }
                filmList.value = movies
            }
        }
    }

    fun setLike(id: Int, isLiked: Boolean) {
        filmList.value?.get(id)?.isFavorite = isLiked
        filmList.postValue(filmList.value)
    }

    private val exceptionHandler get() = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            showExceptionToUser()
        }
    }

    private suspend fun showExceptionToUser() =
        withContext(Dispatchers.Main) {
            errorData.value = FilmsListError.LOAD_ERROR
        }

    enum class FilmsListError {
        LOAD_ERROR, EMPTY_LIST
    }
}

