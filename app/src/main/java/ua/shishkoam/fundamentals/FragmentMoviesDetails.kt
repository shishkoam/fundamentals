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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ua.shishkoam.fundamentals.data.Film
import ua.shishkoam.fundamentals.recyclerview.ActorRecyclerViewAdapter
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FragmentMoviesDetails : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val obj: Film?
        val json = arguments?.getString("current_film")
        obj = if (!json.isNullOrEmpty()) {
            Json.decodeFromString<Film>(json)
        } else {
            null
        }
        (view.findViewById(R.id.back) as Button?)?.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        obj?.let {
            initCast(obj, view)
            Glide.with(requireContext()).load(obj.imageBig)
                .error(obj.image)
                .into((view.findViewById(R.id.poster) as ImageView))
            (view.findViewById(R.id.name) as TextView?)?.text = obj.name
            (view.findViewById(R.id.genre) as TextView?)?.text = obj.genres
            (view.findViewById(R.id.rating) as RatingBar?)?.rating = obj.rating.toFloat()
            (view.findViewById(R.id.story) as TextView?)?.text = obj.story
            (view.findViewById(R.id.age) as TextView?)?.text = "${obj.age}+"
            (view.findViewById(R.id.reviews) as TextView?)?.text =
                requireContext().getString(R.string.reviews_number, obj.reviewNum)
        }
    }

    private fun initCast(obj: Film, view: View) {
        obj.cast?.let {
            val defaultItemAnimator: RecyclerView.ItemAnimator = LandingAnimator()
            val viewAdapter = ActorRecyclerViewAdapter(obj.cast)
            val recyclerView = view.findViewById(R.id.list) as RecyclerView
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = viewAdapter
                itemAnimator = defaultItemAnimator
            }
        }
    }
}