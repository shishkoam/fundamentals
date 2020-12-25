package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instanceOrNull
import ua.shishkoam.fundamentals.data.Movie

class MovieViewModelFactory (private val movie: Movie) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movie) as T?
            ?: modelClass.newInstance()
    }
}
