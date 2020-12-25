package ua.shishkoam.fundamentals.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.data.loadMovies

class FilmsListViewModel() : ViewModel() {

    private var filmList: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()
    private var errorData: MutableLiveData<FilmsListError> = MutableLiveData<FilmsListError>()
    private val likedFilmsData: MutableLiveData<HashMap<String, Boolean>> = MutableLiveData<HashMap<String, Boolean>>()

    val movies: LiveData<List<Movie>> get() = filmList
    val error: LiveData<FilmsListError> get() = errorData
    val likedFilms: LiveData<HashMap<String, Boolean>> get() = likedFilmsData

    fun loadFilm(context: Context) {
        viewModelScope.launch(exceptionHandler) {
            val list = loadMovies(context)
            withContext(Dispatchers.Main) {
                filmList.value = list
            }
        }
    }

    fun setLike(film: String, isLiked: Boolean) {
        likedFilmsData.value?.put(film, isLiked)
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
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

