package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieDetailsViewModel(private val movieRepository: MovieRepository, movie: Movie) : ViewModel() {
    private var actorList: MutableLiveData<List<Actor>> = MutableLiveData<List<Actor>>()

    init {
        loadActors(movie)
    }

    val actors: LiveData<List<Actor>> get() = actorList

    private val movieData: MutableLiveData<Movie> = MutableLiveData<Movie>()
    val movie : LiveData<Movie> get() = movieData
    init {
        movieData.value = movie
    }

    fun loadActors(movie: Movie) {
        viewModelScope.launch() {
            val list = movieRepository.getActors(movie.id)
            withContext(Dispatchers.Main) {
                actorList.value = list
            }
        }
    }
}