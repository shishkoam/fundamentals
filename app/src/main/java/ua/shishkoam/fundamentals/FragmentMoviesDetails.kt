package ua.shishkoam.fundamentals

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.databinding.FragmentMoviesDetailsBinding
import ua.shishkoam.fundamentals.recyclerview.ActorDelegateAdapter
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment(R.layout.fragment_movies_details) {

    private val args: FragmentMoviesDetailsArgs by navArgs()

    private val binding by viewBinding(FragmentMoviesDetailsBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film: Movie = args.currentMovie
        binding.run {
            backButton.setOnClickListener {
                findNavController().navigate(R.id.openMovieList)
            }

            initCast(film, movieList)
            ImageLoader.loadImage(poster, film.backdrop)

            nameText.text = film.title
            genreText.text = film.getGenresString()
            ratingBar.rating = film.getRatingIn5Stars()
            storyText.text = film.overview
            ageText.text = "${film.minimumAge}+"
            reviewsText.text =
                requireContext().getString(R.string.reviews_number, film.numberOfRatings)
        }
    }

    private fun initCast(film: Movie, movieList: RecyclerView) {
        film.actors.let {
            val listAdapter = ActorDelegateAdapter(film.actors)
            val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
//            val actorViewAdapter = ActorRecyclerViewAdapter(film.cast)
            movieList.run {
                setHasFixedSize(true)
//                adapter = actorViewAdapter
                adapter = listAdapter
                itemAnimator = landingItemAnimator
            }
        }
    }
}