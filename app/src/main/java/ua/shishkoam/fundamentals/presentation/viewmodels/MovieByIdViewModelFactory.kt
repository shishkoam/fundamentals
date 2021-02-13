package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.shishkoam.fundamentals.domain.MovieInteractor

class MovieByIdViewModelFactory (
    private val movieInteractor: MovieInteractor, private val movieId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movieInteractor, movieId) as T?
            ?: modelClass.newInstance()
    }
}