package ua.shishkoam.fundamentals.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.databinding.FragmentMoviesDetailsBinding
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.domain.RepositoryError
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.domain.data.Movie
import ua.shishkoam.fundamentals.presentation.recyclerview.ActorDelegateAdapter
import ua.shishkoam.fundamentals.presentation.recyclerview.LandingAnimator
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieByIdViewModelFactory
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieDetailsViewModel
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieViewModelFactory
import ua.shishkoam.fundamentals.utils.ImageLoader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment(R.layout.fragment_movies_details), DIAware {
    override val di: DI by di()

    private val args: FragmentMoviesDetailsArgs by navArgs()
    private val movieInteractor: MovieInteractor by instance()

    private val binding by viewBinding(FragmentMoviesDetailsBinding::bind)
    private val actorListStateObserver = Observer<List<Actor>> { actors ->
        actors ?: return@Observer
        listAdapter.items = actors
        listAdapter.notifyDataSetChanged()
    }

    private val errorStateObserver = Observer<RepositoryError> { error ->
        error ?: return@Observer
        if (error == RepositoryError.LOAD_ERROR) {
            showExceptionToUser(getString(R.string.cant_load_film_details))
        }
    }

    private fun showExceptionToUser(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private val listAdapter = ActorDelegateAdapter()

    private val movieDetails: MovieDetailsViewModel by lazy {
        if (args.currentMovieId != -1) {
            ViewModelProvider(
                this@FragmentMoviesDetails,
                MovieByIdViewModelFactory(movieInteractor, args.currentMovieId)
            ).get(MovieDetailsViewModel::class.java)
        } else {
            ViewModelProvider(
                this@FragmentMoviesDetails,
                MovieViewModelFactory(movieInteractor, args.currentMovie!!)
            ).get(MovieDetailsViewModel::class.java)
        }

    }

    private val movieStateObserver = Observer<Movie> { movie ->
        movie ?: return@Observer
        binding.run {
            ImageLoader.loadImage(poster, movie.backdropUrl)
            nameText.text = movie.title
            genreText.text = movie.getGenresString()
            ratingBar.rating = movie.getRatingIn5Stars()
            storyText.text = movie.overview
            langText.text = "${movie.originalLanguage}"
            reviewsText.text =
                requireContext().getString(R.string.reviews_number, movie.voteCount)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCast()
        movieDetails.run {
            movieLive.observe(viewLifecycleOwner, movieStateObserver)
            actors.observe(viewLifecycleOwner, actorListStateObserver)
            error.observe(viewLifecycleOwner, errorStateObserver)
        }

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initCast() {
        val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
        binding.movieList.run {
            setHasFixedSize(true)
            adapter = listAdapter
            itemAnimator = landingItemAnimator
        }
    }
}