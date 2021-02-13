package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.kirich1409.result.asSuccess
import by.kirich1409.result.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieDetailsViewModel() : ViewModel() {
    private var actorList: MutableLiveData<List<Actor>> = MutableLiveData<List<Actor>>()
    private var errorData: MutableLiveData<RepositoryError> = MutableLiveData<RepositoryError>()
    private var movieInteractor: MovieInteractor? = null
    private var movie: Movie? = null

    constructor(movieInteractor: MovieInteractor, movie: Movie) : this() {
        this.movieInteractor = movieInteractor
        loadActors(movie)
    }

    constructor(movieInteractor: MovieInteractor, movieId: Int) : this() {
        this.movieInteractor = movieInteractor
        viewModelScope.launch() {
            movieInteractor.getMovie(movieId).asSuccess().value.collect{ movie ->
                withContext(Dispatchers.Main) {
                    loadActors(movie)
                }
            }
        }
    }

    val error: LiveData<RepositoryError> get() = errorData

    val actors: LiveData<List<Actor>> get() = actorList

    private val movieData: MutableLiveData<Movie> = MutableLiveData<Movie>()
    val movieLive : LiveData<Movie> get() = movieData
    init {
        movieData.value = movie
    }

    fun loadActors(movie: Movie) {
        movieInteractor ?: return
        this.movie = movie
        viewModelScope.launch() {
            val result = movieInteractor!!.getActors(movie.id)
            withContext(Dispatchers.Main) {
                if (result.isSuccess()) {
                    actorList.value = result.asSuccess().value
                } else {
                    errorData.value = RepositoryError.LOAD_ERROR
                }
            }
        }
    }
}