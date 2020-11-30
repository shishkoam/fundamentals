package ua.shishkoam.fundamentals

import android.content.Context
import android.content.res.Configuration
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.shishkoam.fundamentals.data.Film
import ua.shishkoam.fundamentals.dummy.DummyContent
import ua.shishkoam.fundamentals.recyclerview.FilmRecyclerViewAdapter
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager
import ua.shishkoam.fundamentals.recyclerview.GridAutofitLayoutManager.Companion.AUTO_FIT
import ua.shishkoam.fundamentals.recyclerview.LandingAnimator


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {
    private val likedFilms: HashMap<String, Boolean> = HashMap()

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
            GridAutofitLayoutManager(requireContext(), AUTO_FIT, 166f)
        } else {
            GridLayoutManager(requireContext(), 2)
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
        val films = DummyContent.films

        val listAdapter = createFilmAdapterDelegate(films, textShader)
//        val filmRecyclerViewAdapter = createFilmAdapter(films, textShader)
        val recyclerView = view.findViewById(R.id.movie_list) as RecyclerView

        val swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
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
            // Initialize a new Runnable
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun createFilmAdapterDelegate(
        films: MutableList<Film>,
        textShader: LinearGradient) : ListDelegationAdapter<List<Film>> {
        fun setLikeColor(
            isChecked: Boolean,
            likeImageView: ImageView,
            context: Context
        ) {
            if (isChecked) {
                likeImageView.setColorFilter(
                    ContextCompat.getColor(context, R.color.genre_color)
                )
            } else {
                likeImageView.clearColorFilter()
            }
        }

        fun filmAdapterDelegate() = adapterDelegate<Film, Film>(R.layout.view_holder_movie) {
            val nameTextView: TextView = findViewById(R.id.name_text)
            val reviewsTextView: TextView = findViewById(R.id.reviews_text)
            val timeTextView: TextView = findViewById(R.id.time_text)
            val genreTextView: TextView = findViewById(R.id.genre_text)
            val posterImageView: ImageView = findViewById(R.id.photo_image)
            val ratingView: RatingBar = findViewById(R.id.rating_bar)
            val ageTextView: TextView = findViewById(R.id.age_text)
            val likeImageView: ImageView = findViewById(R.id.like)
            val nameTextShader: Shader? = textShader
            itemView.setOnClickListener { // Triggers click upwards to the adapter on click
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(item)
                    findNavController().navigate(action)
                }
            }
            likeImageView.setOnClickListener { // Triggers click upwards to the adapter on click
                val likedState = likedFilms[item.name] ?: false
                likedFilms[item.name] = !likedState
                setLikeColor(!likedState, likeImageView, context)
            }
            bind {
                nameTextView.text = item.name
                val context: Context = nameTextView.context
                nameTextShader?.let {
                    nameTextView.paint.shader = nameTextShader
                }
                reviewsTextView.text = context.getString(
                    R.string.reviews_number,
                    item.reviewNum
                )
                ratingView.rating = item.rating.toFloat()
                genreTextView.text = item.genres
                timeTextView.text = context.getString(R.string.minutes_number, item.time)
                Glide.with(context.applicationContext).load(item.image)
                    .error(R.mipmap.ic_launcher)
                    .into(posterImageView)
                ageTextView.text = "${item.age}+"
                setLikeColor(likedFilms[item.name] == true, likeImageView, context)
            }
        }
        val listAdapter = ListDelegationAdapter(
            filmAdapterDelegate()
        )
        listAdapter.items = films
        return listAdapter
    }

    private fun createFilmAdapter(
        films: MutableList<Film>,
        textShader: LinearGradient
    ): FilmRecyclerViewAdapter {
        val filmRecyclerViewAdapter = FilmRecyclerViewAdapter(
            values = films, nameShader = textShader, likedFilms = likedFilms,
            onItemClickListener = object : FilmRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(itemView: View?, position: Int) {
                    val action =
                        FragmentMoviesListDirections.openMovieDetails(DummyContent.films[position])
                    findNavController().navigate(action)
                }
            },
            onItemLikeListener = object : FilmRecyclerViewAdapter.OnItemLikeListener {
                override fun onItemLike(itemView: View?, position: Int) {
                    val isLiked = likedFilms[films[position].name] ?: false
                    likedFilms[films[position].name] = !isLiked
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

        @JvmStatic
        fun newInstance(columnCount: Int) =
            FragmentMoviesList().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}