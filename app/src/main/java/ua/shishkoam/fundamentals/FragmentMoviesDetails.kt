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
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.shishkoam.fundamentals.data.Actor
import ua.shishkoam.fundamentals.data.Film
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
        val film: Film = args.currentMovie

        view.findViewById<Button>(R.id.back_button)?.setOnClickListener {
            findNavController().navigate(R.id.openMovieList)
        }

        initCast(film, view)
        ImageLoader.loadImage(view.findViewById<ImageView>(R.id.poster), film.imageBig,film.image )

        view.findViewById<TextView>(R.id.name_text)?.text = film.name
        view.findViewById<TextView>(R.id.genre_text)?.text = film.genres
        view.findViewById<RatingBar>(R.id.rating_bar)?.rating = film.rating.toFloat()
        view.findViewById<TextView>(R.id.story_text)?.text = film.story
        view.findViewById<TextView>(R.id.age_text)?.text = "${film.age}+"
        view.findViewById<TextView>(R.id.reviews_text)?.text =
            requireContext().getString(R.string.reviews_number, film.reviewNum)
    }

    private fun initCast(film: Film, view: View) {
        film.cast?.let {
            val listAdapter = createActorDelegationAdapter(film)
            val landingItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
//            val actorViewAdapter = ActorRecyclerViewAdapter(film.cast)
            val actorRecyclerView = view.findViewById(R.id.movie_list) as RecyclerView
            actorRecyclerView.run {
                setHasFixedSize(true)
//                adapter = actorViewAdapter
                adapter = listAdapter
                itemAnimator = landingItemAnimator
            }
        }
    }

    private fun createActorDelegationAdapter(film: Film): ListDelegationAdapter<List<Actor>> {
        fun actorAdapterDelegate() = adapterDelegate<Actor, Actor>(R.layout.view_holder_actor) {
            val name: TextView = findViewById(R.id.name_text)
            val photoImage: ImageView = findViewById(R.id.photo_image)
            bind {
                name.text = item.name
                ImageLoader.loadImage(photoImage, item.photo)
            }
        }

        val listAdapter = ListDelegationAdapter(
            actorAdapterDelegate()
        )
        listAdapter.items = film.cast
        return listAdapter
    }
}