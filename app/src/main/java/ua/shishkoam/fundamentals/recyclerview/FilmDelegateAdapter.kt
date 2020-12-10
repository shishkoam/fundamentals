package ua.shishkoam.fundamentals.recyclerview

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.shishkoam.fundamentals.ImageLoader
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Movie

class FilmDelegateAdapter(
    films: MutableList<Movie>?,
    textShader: LinearGradient?,
    likedFilms: HashMap<String, Boolean> = HashMap(),
    onFilmClickListener: OnFilmClickListener? = null,
    onFilmLikeListener: OnFilmLikeListener? = null
) : ListDelegationAdapter<List<Movie>>(
    filmAdapterDelegate(textShader, likedFilms, onFilmClickListener, onFilmLikeListener)
) {
    init {
        films?.let {
            items = films
        }
    }
}

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

fun filmAdapterDelegate(
    textShader: LinearGradient?,
    likedFilms: HashMap<String, Boolean>,
    onFilmClickListener: OnFilmClickListener?,
    onItemLikeListener: OnFilmLikeListener?
) =
    adapterDelegate<Movie, Movie>(R.layout.view_holder_movie) {
        val nameTextView: TextView = findViewById(R.id.name_text)
        val reviewsTextView: TextView = findViewById(R.id.reviews_text)
        val timeTextView: TextView = findViewById(R.id.time_text)
        val genreTextView: TextView = findViewById(R.id.genre_text)
        val posterImageView: ImageView = findViewById(R.id.photo_image)
        val ratingView: RatingBar = findViewById(R.id.rating_bar)
        val ageTextView: TextView = findViewById(R.id.age_text)
        val likeImageView: ImageView = findViewById(R.id.like)
        val nameTextShader: Shader? = textShader
        var likedState = false
        itemView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onFilmClickListener?.onFilmClick(item)
            }
        }
        likeImageView.setOnClickListener { // Triggers click upwards to the adapter on click
            likedState = !likedState
            onItemLikeListener?.onFilmLike(item, likedState)
            setLikeColor(likedState, likeImageView, context)
        }
        bind {
            nameTextView.text = item.title
            val context: Context = nameTextView.context
            nameTextShader?.let {
                nameTextView.paint.shader = nameTextShader
            }
            reviewsTextView.text = context.getString(
                R.string.reviews_number,
                item.numberOfRatings
            )
            ratingView.rating = item.getRatingIn5Stars()
            genreTextView.text = item.getGenresString()

            timeTextView.text = context.getString(R.string.minutes_number, item.runtime)
            ImageLoader.loadImage(posterImageView, item.poster)
            ageTextView.text = "${item.minimumAge}+"
            likedState = likedFilms[item.title] == true
            setLikeColor(likedState, likeImageView, context)
        }
    }