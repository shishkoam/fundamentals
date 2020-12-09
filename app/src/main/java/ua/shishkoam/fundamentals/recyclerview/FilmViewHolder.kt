package ua.shishkoam.fundamentals.recyclerview

import android.content.Context
import android.graphics.Shader
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.ImageLoader
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Movie

class FilmViewHolder(
    view: View,
    private val listener: OnFilmClickListener?,
    private val likeListener: OnFilmLikeListener?,
    nameShader: Shader? = null
) : RecyclerView.ViewHolder(
    view
) {
    private val nameTextView: TextView = view.findViewById(R.id.name_text)
    private val reviewsTextView: TextView = view.findViewById(R.id.reviews_text)
    private val timeTextView: TextView = view.findViewById(R.id.time_text)
    private val genreTextView: TextView = view.findViewById(R.id.genre_text)
    private val posterImageView: ImageView = view.findViewById(R.id.photo_image)
    private val ratingView: RatingBar = view.findViewById(R.id.rating_bar)
    private val ageTextView: TextView = view.findViewById(R.id.age_text)
    private val likeImageView: ImageView = view.findViewById(R.id.like)
    private var likedState = false
    private val nameTextShader: Shader? = nameShader

    fun onBind(
        item: Movie, isLiked: Boolean
    ) {
        itemView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onFilmClick(item)
            }
        }
        likeImageView.setOnClickListener { // Triggers click upwards to the adapter on click
            likedState = !likedState
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                likeListener?.onFilmLike(item, likedState)
            }
            setLike(likedState)
        }
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
        likedState = isLiked
        setLike(likedState)
    }

    private fun setLike(
        isChecked: Boolean
    ) {
        val context = likeImageView.context ?: return
        if (isChecked) {
            likeImageView.setColorFilter(ContextCompat.getColor(context, R.color.genre_color))
        } else {
            likeImageView.clearColorFilter()
        }
    }

    override fun toString(): String {
        return super.toString() + " '" + nameTextView.text + "'"
    }

}