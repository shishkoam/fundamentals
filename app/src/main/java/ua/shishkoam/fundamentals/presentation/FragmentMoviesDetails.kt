package ua.shishkoam.fundamentals.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ua.shishkoam.fundamentals.utils.ImageLoader
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.databinding.FragmentMoviesDetailsBinding
import ua.shishkoam.fundamentals.utils.observe
import ua.shishkoam.fundamentals.presentation.recyclerview.ActorDelegateAdapter
import ua.shishkoam.fundamentals.presentation.recyclerview.LandingAnimator
import ua.shishkoam.fundamentals.presentation.viewmodels.MovieDetailsViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment(R.layout.fragment_movies_details) {

    private val args: FragmentMoviesDetailsArgs by navArgs()

    private val binding by viewBinding(FragmentMoviesDetailsBinding::bind)

    private val movieStateObserver = Observer<Movie> {
        val movie = it ?: return@Observer
        binding.run {
            initCast(movie, movieList)
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
        val movie: Movie = args.currentMovie
        val movieDetails = ViewModelProvider(
            this@FragmentMoviesDetails,
            defaultViewModelProviderFactory
        ).get(MovieDetailsViewModel::class.java)
        observe(movieDetails.movieData, movieStateObserver)
        if (movieDetails.movieData.value != movie) {
            movieDetails.movieData.value = movie
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initCast(movie: Movie, movieList: RecyclerView) {
        movie.actors.let {
            val listAdapter = ActorDelegateAdapter(movie.actors)
            val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
            movieList.run {
                setHasFixedSize(true)
                adapter = listAdapter
                itemAnimator = landingItemAnimator
            }
        }
    }
}