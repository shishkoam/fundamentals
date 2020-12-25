package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.shishkoam.fundamentals.data.Movie

class MovieDetailsViewModel(movie: Movie) : ViewModel() {
    private val movieData: MutableLiveData<Movie> = MutableLiveData<Movie>()
    val movie : LiveData<Movie> get() = movieData
    init {
        movieData.value = movie
    }
}