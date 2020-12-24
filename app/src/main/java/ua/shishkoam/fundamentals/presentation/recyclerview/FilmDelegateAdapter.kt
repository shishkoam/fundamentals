package ua.shishkoam.fundamentals.presentation.recyclerview

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ua.shishkoam.fundamentals.utils.ImageLoader
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Movie
import ua.shishkoam.fundamentals.databinding.ViewHolderMovieBinding

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
    adapterDelegateViewBinding<Movie, Movie, ViewHolderMovieBinding>(
        { layoutInflater, root -> ViewHolderMovieBinding.inflate(layoutInflater, root, false) }
    ) {
        val nameTextShader: Shader? = textShader
        var likedState = false
        itemView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onFilmClickListener?.onFilmClick(item)
            }
        }
        binding.like.setOnClickListener { // Triggers click upwards to the adapter on click
            likedState = !likedState
            onItemLikeListener?.onFilmLike(item, likedState)
            setLikeColor(likedState, binding.like, context)
        }
        bind {
            binding.nameText.text = item.title
            nameTextShader?.let {
                binding.nameText.paint.shader = nameTextShader
            }
            binding.reviewsText.text = context.getString(
                R.string.reviews_number,
                item.numberOfRatings
            )
            binding.ratingBar.rating = item.getRatingIn5Stars()
            binding.genreText.text = item.getGenresString()

            binding.timeText.text = context.getString(R.string.minutes_number, item.runtime)
            ImageLoader.loadImage(binding.photoImage, item.poster)
            binding.ageText.text = "${item.minimumAge}+"
            likedState = likedFilms[item.title] == true
            setLikeColor(likedState, binding.like, context)
        }
    }