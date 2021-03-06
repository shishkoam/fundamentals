package ua.shishkoam.fundamentals.presentation.viewmodels

import androidx.lifecycle.*
import by.kirich1409.result.asSuccess
import by.kirich1409.result.isSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.ListItem
import ua.shishkoam.fundamentals.domain.data.Movie
import ua.shishkoam.fundamentals.presentation.recyclerview.LoadItem
import kotlinx.coroutines.flow.collect

class FilmsListViewModel(
    private val movieInteractor: MovieInteractor
) : ViewModel() {

    private var moviesData: MutableLiveData<List<ListItem>> = MutableLiveData<List<ListItem>>()
    private var isLoadingData: MutableLiveData<State> = MutableLiveData<State>()
    private val loadItem = LoadItem()

    init {
        loadFilm()
    }

    val movies: LiveData<List<ListItem>> get() = moviesData
    val isLoading: LiveData<State> get() = isLoadingData

    fun loadFilm() {
        viewModelScope.launch(exceptionHandler) {
            setLoading(State.Loading)
            isLoadingData.value = State.None
            val result = movieInteractor.getMovies()

            withContext(Dispatchers.Main) {
                if (result.isSuccess()) {
                    result.asSuccess().value.collect{ movies ->
                        moviesData.value = movies
                    }
                    setLoading(State.Loaded(1, movieInteractor.getTotalPageNumber()))
                } else {
                    setLoading(State.Error(RepositoryError.LOAD_ERROR))
                    moviesData.value = emptyList()
                }
            }
        }
    }

    suspend fun setLoading(loading: State) = withContext(Dispatchers.Main) {
        if (loading is State.Loading && isLoadingData.value !is State.Loading) {
            val list: ArrayList<ListItem> = ArrayList()
            moviesData.value?.run {
                list.addAll(this)
            }
            list.add(loadItem)
            moviesData.value = list
        }
        isLoadingData.value = loading
    }

    fun loadMoreFilms() {
        viewModelScope.launch(exceptionHandler) {
            setLoading(State.Loading)
            val result = movieInteractor.getMoreMovies()
            withContext(Dispatchers.Main) {
                val list: ArrayList<ListItem> = ArrayList(moviesData.value!!)
                if (result.isSuccess()) {
                    setLoading(
                        State.Loaded(
                            movieInteractor.getCurrentPageNumber(),
                            movieInteractor.getTotalPageNumber()
                        )
                    )
                } else {
                    setLoading(State.Error(RepositoryError.LOAD_ERROR))
                }
                list.remove(loadItem)
                moviesData.value = list

            }
        }
    }

    fun setLike(id: Int, isLiked: Boolean) {
        moviesData.value ?: return
        val oldMovie = findMovie(id)
        val newMovie = oldMovie?.second?.copy()
        newMovie?.run {
            isFavorite = isLiked
            val newList = moviesData.value?.toMutableList()
            newList?.set(oldMovie.first, newMovie)
            moviesData.postValue(newList)
        }
    }

    private fun findMovie(id: Int): Pair<Int, Movie>? {
        moviesData.value?.run {
            for (i in 0 until size - 1) {
                val movie = get(i)
                if ((movie as? Movie)?.id == id) {
                    return Pair(i, movie)
                }
            }
        }
        return null
    }

    private val exceptionHandler
        get() = CoroutineExceptionHandler { context, throwable ->
            viewModelScope.launch {
                throwable.printStackTrace()
                throwable.stackTrace
                showExceptionToUser()
            }
        }

    private suspend fun showExceptionToUser() =
        withContext(Dispatchers.Main) {
            setLoading(State.Error(RepositoryError.LOAD_ERROR))
        }

    sealed class State {
        object None : State()
        object Loading : State()
        class Loaded(val current: Int, val total: Int) : State()
        class Error(val error: RepositoryError) : State()
    }
}

