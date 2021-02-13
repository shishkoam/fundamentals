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
import ua.shishkoam.fundamentals.domain.CalendarInteractor
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie

class MovieDetailsViewModel() : ViewModel() {
    private var actorList: MutableLiveData<List<Actor>> = MutableLiveData<List<Actor>>()
    private var errorData: MutableLiveData<RepositoryError> = MutableLiveData<RepositoryError>()
    private lateinit var movieInteractor: MovieInteractor
    private lateinit var calendarInteractor: CalendarInteractor

    constructor(
        movieInteractor: MovieInteractor,
        calendarInteractor: CalendarInteractor,
        movie: Movie
    ) : this() {
        this.movieInteractor = movieInteractor
        this.calendarInteractor = calendarInteractor
        movieData.value = movie
        loadActors(movie)
    }

    constructor(
        movieInteractor: MovieInteractor,
        calendarInteractor: CalendarInteractor,
        movieId: Int
    ) : this() {
        this.movieInteractor = movieInteractor
        this.calendarInteractor = calendarInteractor
        viewModelScope.launch() {
            movieInteractor.getMovie(movieId).asSuccess().value.collect { movie ->
                withContext(Dispatchers.Main) {
                    loadActors(movie)
                    movieData.value = movie
                }
            }
        }
    }

    val error: LiveData<RepositoryError> get() = errorData

    val actors: LiveData<List<Actor>> get() = actorList

    private val movieData: MutableLiveData<Movie> = MutableLiveData<Movie>()
    val movieLive : LiveData<Movie> get() = movieData

    fun addMovieToCalendar(year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int) {
        movieData.value ?: return
        calendarInteractor.addMovieToCalendar(movieData.value!!, year, month, day, hourOfDay, minute)
    }

    fun loadActors(movie: Movie) {
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