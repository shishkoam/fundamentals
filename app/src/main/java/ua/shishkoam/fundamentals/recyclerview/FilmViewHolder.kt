package ua.shishkoam.fundamentals.recyclerview

import android.graphics.Shader
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.R

class FilmViewHolder(
    view: View,
    listener: FilmRecyclerViewAdapter.OnItemClickListener?,
    likeListener: FilmRecyclerViewAdapter.OnItemLikeListener?,
    nameShader: Shader? = null
) : RecyclerView.ViewHolder(
    view
) {
    val name: TextView = view.findViewById(R.id.name)
    val reviews: TextView = view.findViewById(R.id.reviews)
    val time: TextView = view.findViewById(R.id.time)
    val genre: TextView = view.findViewById(R.id.genre)
    val imageView: ImageView = view.findViewById(R.id.imageView)
    val ratingView: RatingBar = view.findViewById(R.id.rating)
    val age: TextView = view.findViewById(R.id.age)
    val like: ImageView = view.findViewById(R.id.like)
    private var liked = false
    val textShader: Shader? = nameShader

    init {
        itemView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener?.onItemClick(itemView, position)
            }
        }
        like.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                likeListener?.onItemLike(itemView, position)
            }
            setLike(!liked)
        }
    }


    fun setLike(
        isChecked: Boolean
    ) {
        liked = isChecked
        val context = like.context ?: return
        if (isChecked) {
            like.setColorFilter(ContextCompat.getColor(context, R.color.genre_color))
        } else {
            like.clearColorFilter()
        }
    }

    override fun toString(): String {
        return super.toString() + " '" + name.text + "'"
    }

}