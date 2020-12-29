package ua.shishkoam.fundamentals.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.databinding.FragmentMoviesDetailsBinding
import ua.shishkoam.fundamentals.presentation.recyclerview.ActorDelegateAdapter
import ua.shishkoam.fundamentals.presentation.recyclerview.LandingAnimator
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieDetailsViewModel
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieViewModelFactory
import ua.shishkoam.fundamentals.utils.ImageLoader

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment(R.layout.fragment_movies_details), DIAware {
    override val di: DI by di()

    private val args: FragmentMoviesDetailsArgs by navArgs()

    private val binding by viewBinding(FragmentMoviesDetailsBinding::bind)

    private val listAdapter = ActorDelegateAdapter()

    private val movieDetails: MovieDetailsViewModel by lazy {
        ViewModelProvider(
            this@FragmentMoviesDetails,
            MovieViewModelFactory(args.currentMovie)
        ).get(MovieDetailsViewModel::class.java)
    }

    private val movieStateObserver = Observer<Movie> { movie ->
        movie ?: return@Observer
        binding.run {
            listAdapter.items = movie.actors
            listAdapter.notifyDataSetChanged()
            ImageLoader.loadImage(poster, movie.backdrop)
            nameText.text = movie.title
            genreText.text = movie.getGenresString()
            ratingBar.rating = movie.getRatingIn5Stars()
            storyText.text = movie.overview
            ageText.text = "${movie.minimumAge}+"
            reviewsText.text =
                requireContext().getString(R.string.reviews_number, movie.numberOfRatings)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetails.movie.observe(viewLifecycleOwner, movieStateObserver)
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        initCast()
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