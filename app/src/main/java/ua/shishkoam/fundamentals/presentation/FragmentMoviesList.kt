package ua.shishkoam.fundamentals.presentation

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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.*
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.databinding.FragmentMoviesListBinding
import ua.shishkoam.fundamentals.presentation.recyclerview.*
import ua.shishkoam.fundamentals.presentation.recyclerview.GridAutofitLayoutManager.Companion.AUTO_FIT
import ua.shishkoam.fundamentals.presentation.viewmodels.FilmsListViewModel
import ua.shishkoam.fundamentals.utils.observe


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var filmsListViewModel: FilmsListViewModel? = null

    private val binding by viewBinding(FragmentMoviesListBinding::bind)

    private val filmsListStateObserver = Observer<List<Movie>> {movies ->
        movies ?: return@Observer
        listAdapter?.items = movies
        listAdapter?.notifyDataSetChanged()
    }

    private val filmsListErrorStateObserver = Observer<FilmsListViewModel.FilmsListError> { error ->
        error ?: return@Observer
        if (error == FilmsListViewModel.FilmsListError.LOAD_ERROR) {
            showExceptionToUser(getString(R.string.cant_load_films))
        }
    }

    private var listAdapter: ListDelegationAdapter<List<Movie>>? = null

    private fun showExceptionToUser(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        filmsListViewModel = ViewModelProvider(
            this@FragmentMoviesList,
            defaultViewModelProviderFactory
        ).get(FilmsListViewModel::class.java)
        listAdapter = createFilmAdapterDelegate(textShader = textShader)
        filmsListViewModel?.run {
            this@FragmentMoviesList.observe(movies, filmsListStateObserver)
            this@FragmentMoviesList.observe(error, filmsListErrorStateObserver)
            updateMoviesList()
        }
        binding.movieList.run {
            setHasFixedSize(true)
            layoutManager = filmRecyclerViewManager
            adapter = listAdapter
            itemAnimator = landingItemAnimator
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
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
    ): ListDelegationAdapter<List<Movie>> =
        FilmDelegateAdapter(films = films, textShader = textShader,
            likedFilms = filmsListViewModel?.likedFilms?.value ?: HashMap(),
            onFilmClickListener = object : OnFilmClickListener {
                override fun onFilmClick(item: Movie) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(item)
                    findNavController().navigate(action)
                }
            },
            onFilmLikeListener = object : OnFilmLikeListener {
                override fun onFilmLike(item: Movie, likedState: Boolean) {
                    filmsListViewModel?.setLike(item.title, likedState)
                }
            })

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