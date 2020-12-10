package ua.shishkoam.fundamentals

import android.content.res.Configuration
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.*
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.recyclerview.*
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager.Companion.AUTO_FIT


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
private const val TAG = "MovieList"

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var filmsListViewModel: FilmsListViewModel? = null

    private val filmsListStateObserver = Observer<List<Movie>> {
        val movies = it ?: return@Observer
        listAdapter?.items = movies
        listAdapter?.notifyDataSetChanged()
    }

    private val filmsListErrorStateObserver = Observer<FilmsListViewModel.FilmsListError> {
        val error = it ?: return@Observer
        if (error == FilmsListViewModel.FilmsListError.LOAD_ERROR) {
            showExceptionToUser(getString(R.string.cant_load_films))
        }
    }

    private val likedFilms: HashMap<String, Boolean> = HashMap()
    private var listAdapter: ListDelegationAdapter<List<Movie>>? = null

    private fun showExceptionToUser(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val savedData =
                CollectionUtils.fromBundleBooleanMap(savedInstanceState.getBundle("likes"))
            savedData?.let {
                likedFilms.putAll(savedData)
            }
        }
        val orientation = this.resources.configuration.orientation
        val filmRecyclerViewManager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridAutofitLayoutManager(requireContext(), AUTO_FIT, RECOMMENDED_FILM_WIDTH)
        } else {
            GridLayoutManager(requireContext(), VERTICAL_COLUMN_COUNT)
        }

        val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()

        val firstColor = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_1)
        val secondColor = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_2)
        val thirdColor = ContextCompat.getColor(requireContext(), R.color.text_color_gradient_3)
        val textShader = LinearGradient(
            0f,
            0f,
            0f,
            50f,
            intArrayOf(firstColor, secondColor, thirdColor),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )

        listAdapter = createFilmAdapterDelegate(textShader = textShader)
//        val filmRecyclerViewAdapter = createFilmAdapter(films, textShader)
        val recyclerView = view.findViewById<RecyclerView>(R.id.movie_list)

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        filmsListViewModel = ViewModelProvider(
            this@FragmentMoviesList,
            defaultViewModelProviderFactory
        ).get(FilmsListViewModel::class.java)
        filmsListViewModel?.run {
            this@FragmentMoviesList.observe(filmList, filmsListStateObserver)
            this@FragmentMoviesList.observe(error, filmsListErrorStateObserver)
            if (filmList.value.isNullOrEmpty()) {
                updateMoviesList()
            }
        }

        recyclerView.run {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = filmRecyclerViewManager

//            adapter = filmRecyclerViewAdapter
            adapter = listAdapter
            itemAnimator = landingItemAnimator


        }
        swipeRefreshLayout.setOnRefreshListener {
            updateMoviesList()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateMoviesList() {
        filmsListViewModel?.loadFilm(requireContext())
    }

    private fun createFilmAdapterDelegate(
        films: MutableList<Movie>? = null,
        textShader: LinearGradient? = null
    ): ListDelegationAdapter<List<Movie>> {
        return FilmDelegateAdapter(films = films, textShader = textShader,
            likedFilms = likedFilms,
            onFilmClickListener = object : OnFilmClickListener {
                override fun onFilmClick(item: Movie) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(item)
                    findNavController().navigate(action)
                }
            },
            onFilmLikeListener = object : OnFilmLikeListener {
                override fun onFilmLike(item: Movie, likedState: Boolean) {
                    likedFilms[item.title] = likedState
                }
            })
    }

    private fun createFilmAdapter(
        films: MutableList<Movie>,
        textShader: LinearGradient
    ): FilmRecyclerViewAdapter {
        val filmRecyclerViewAdapter = FilmRecyclerViewAdapter(
            values = films, nameShader = textShader, likedFilms = likedFilms,
            onFilmClickListener = object : OnFilmClickListener {
                override fun onFilmClick(item: Movie) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(item)
                    findNavController().navigate(action)
                }
            },
            onFilmLikeListener = object : OnFilmLikeListener {
                override fun onFilmLike(item: Movie, likedState: Boolean) {
                    likedFilms[item.title] = likedState
                }
            }
        )
        return filmRecyclerViewAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("likes", CollectionUtils.toBundleBooleanMap(likedFilms))
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val RECOMMENDED_FILM_WIDTH = 166f
        const val VERTICAL_COLUMN_COUNT = 2

        @JvmStatic
        fun newInstance(columnCount: Int) =
            FragmentMoviesList().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}