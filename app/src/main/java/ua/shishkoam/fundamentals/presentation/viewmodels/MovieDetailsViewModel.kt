package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.shishkoam.fundamentals.data.Movie

class MovieDetailsViewModel : ViewModel() {
    var movieData: MutableLiveData<Movie> = MutableLiveData<Movie>()
}