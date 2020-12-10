package ua.shishkoam.fundamentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.recyclerview.ActorDelegateAdapter
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment() {

    private val args: FragmentMoviesDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film: Movie = args.currentMovie

        view.findViewById<Button>(R.id.back_button)?.setOnClickListener {
            findNavController().navigate(R.id.openMovieList)
        }

        initCast(film, view)
        ImageLoader.loadImage(view.findViewById<ImageView>(R.id.poster), film.backdrop)

        view.findViewById<TextView>(R.id.name_text)?.text = film.title
        view.findViewById<TextView>(R.id.genre_text)?.text = film.getGenresString()
        view.findViewById<RatingBar>(R.id.rating_bar)?.rating = film.getRatingIn5Stars()
        view.findViewById<TextView>(R.id.story_text)?.text = film.overview
        view.findViewById<TextView>(R.id.age_text)?.text = "${film.minimumAge}+"
        view.findViewById<TextView>(R.id.reviews_text)?.text =
            requireContext().getString(R.string.reviews_number, film.numberOfRatings)
    }

    private fun initCast(film: Movie, view: View) {
        film.actors?.let {
            val listAdapter = ActorDelegateAdapter(film.actors)
            val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
//            val actorViewAdapter = ActorRecyclerViewAdapter(film.cast)
            view.findViewById<RecyclerView>(R.id.movie_list).run {
                setHasFixedSize(true)
//                adapter = actorViewAdapter
                adapter = listAdapter
                itemAnimator = landingItemAnimator
            }
        }
    }
}