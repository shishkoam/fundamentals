package ua.shishkoam.fundamentals

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.data.loadMovies

class FilmsListViewModel : ViewModel() {
    var filmList: MutableLiveData<List<Movie>> = MutableLiveData<List<Movie>>()
    var error: MutableLiveData<FilmsListError> = MutableLiveData<FilmsListError>()

    fun loadFilm(context: Context) {
        viewModelScope.launch {
            val list = loadMovies(context)
            withContext(Dispatchers.Main) {
                filmList.value = list
            }
        }
    }
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        viewModelScope.launch {
            showExceptionToUser()
        }
    }

    private suspend fun showExceptionToUser() =
        withContext(Dispatchers.Main) {
            error.value = FilmsListError.LOAD_ERROR
        }

    enum class FilmsListError {
        LOAD_ERROR
    }
}

