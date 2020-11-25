package ua.shishkoam.fundamentals.recyclerview

import android.content.Context
import android.graphics.Shader
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Film

class FilmViewHolder(
    view: View,
    listener: FilmRecyclerViewAdapter.OnItemClickListener?,
    likeListener: FilmRecyclerViewAdapter.OnItemLikeListener?,
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

    init {
        itemView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(itemView, position)
            }
        }
        likeImageView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                likeListener?.onItemLike(itemView, position)
            }
            setLike(!likedState)
        }
    }


    fun onBind(
        item: Film, isLiked: Boolean
    ) {

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
        setLike(isLiked)
    }


    private fun setLike(
        isChecked: Boolean
    ) {
        likedState = isChecked
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