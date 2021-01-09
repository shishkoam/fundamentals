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
import kotlinx.coroutines.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.databinding.FragmentMoviesListBinding
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.ListItem
import ua.shishkoam.fundamentals.presentation.recyclerview.*
import ua.shishkoam.fundamentals.presentation.recyclerview.GridAutofitLayoutManager.Companion.AUTO_FIT
import ua.shishkoam.fundamentals.presentation.viewmodels.FilmsListViewModel
import ua.shishkoam.fundamentals.presentation.viewmodels.KodeinViewModelFactory


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMoviesList : Fragment(R.layout.fragment_movies_list), DIAware {
    override val di: DI by di()
    private val filmsListViewModel: FilmsListViewModel by lazy {
        ViewModelProvider(this, KodeinViewModelFactory(di)).get(FilmsListViewModel::class.java)
    }

    private val binding by viewBinding(FragmentMoviesListBinding::bind)

    private val filmsListStateObserver = Observer<List<ListItem>> { movies ->
        movies ?: return@Observer
        listAdapter.updateValues(movies)
        if (scrollListener == null) {
            initScrollListener(movies.size)
        }
    }

    private fun initScrollListener(size: Int) {
        scrollListener = RecyclerViewOnScrollListener(
            binding.movieList.layoutManager!!, size, 0,
            isLoading = false,
            isLastPage = false,
        ) {
            filmsListViewModel.loadMoreFilms()
        }
        binding.movieList.addOnScrollListener(scrollListener!!)
    }

    private val loadingStateObserver = Observer<FilmsListViewModel.State> { isLoading ->
        when (isLoading) {
            is FilmsListViewModel.State.Loading -> scrollListener?.isLoading = true
            is FilmsListViewModel.State.Error -> {
                scrollListener?.isLoading = false
                if (isLoading.error == RepositoryError.LOAD_ERROR) {
                    showExceptionToUser(getString(R.string.cant_load_films))
                }
            }
            is FilmsListViewModel.State.Loaded -> {
                scrollListener?.isLoading = false
                scrollListener?.isLastPage = isLoading.total <= isLoading.current
            }
            else -> scrollListener?.isLoading = false
        }
    }

    private lateinit var listAdapter: FilmDelegateAdapter
    private var scrollListener: RecyclerViewOnScrollListener? = null

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
        listAdapter = FilmDelegateAdapter(
            textShader = textShader,
            onFilmLike = { item, likedState -> filmsListViewModel.setLike(item.id, likedState) },
            onFilmClick = { movie ->
                findNavController().navigate(
                    FragmentMoviesListDirections.openMovieDetails(
                        movie
                    )
                )
            })

        binding.movieList.run {
            setHasFixedSize(true)
            layoutManager = filmRecyclerViewManager
            adapter = listAdapter
            itemAnimator = landingItemAnimator
        }

        filmsListViewModel.run {
            movies.observe(viewLifecycleOwner, filmsListStateObserver)
            isLoading.observe(viewLifecycleOwner, loadingStateObserver)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            updateMoviesList()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateMoviesList() {
        filmsListViewModel.loadFilm()
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