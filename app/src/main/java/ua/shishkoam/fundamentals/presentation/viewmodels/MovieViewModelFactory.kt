package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.shishkoam.fundamentals.domain.CalendarRepository
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieViewModelFactory(
    private val movieInteractor: MovieInteractor,
    private val calendarRepository: CalendarRepository,
    private val movie: Movie
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(movieInteractor, calendarRepository, movie) as T?
            ?: modelClass.newInstance()
    }
}
