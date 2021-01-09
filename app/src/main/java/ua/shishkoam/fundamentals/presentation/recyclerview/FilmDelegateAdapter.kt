package ua.shishkoam.fundamentals.presentation.recyclerview

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.databinding.ItemLoadingBinding
import ua.shishkoam.fundamentals.databinding.ViewHolderMovieBinding
import ua.shishkoam.fundamentals.domain.data.ListItem
import ua.shishkoam.fundamentals.domain.data.Movie
import ua.shishkoam.fundamentals.utils.ImageLoader

class FilmDelegateAdapter(
    films: List<Movie> = emptyList(),
    val textShader: LinearGradient?,
    onFilmLike: ((item: Movie, likedState: Boolean) -> Unit)? = null,
    onFilmClick: ((movie: Movie) -> Unit)? = null
) : ListDelegationAdapter<List<ListItem>>(
    filmAdapterDelegate(textShader, onFilmLike, onFilmClick),
    loadAdapterDelegate()
) {
    init {
        films.let {
            items = films
        }
    }

    fun updateValues(movies: List<ListItem>){
        val diffUtil = RecyclerDiffUtil(items, movies)
        val diffResult = DiffUtil.calculateDiff(diffUtil, true)
        items = movies
        diffResult.dispatchUpdatesTo(this)
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
    onFilmLike: ((item: Movie, likedState: Boolean) -> Unit)? = null,
    onFilmClick: ((movie: Movie) -> Unit)? = null
) =
    adapterDelegateViewBinding<Movie, ListItem, ViewHolderMovieBinding>(
        { layoutInflater, root -> ViewHolderMovieBinding.inflate(layoutInflater, root, false) }
    ) {
        val nameTextShader: Shader? = textShader
        var likedState = false
        itemView.setOnClickListener { // Triggers click upwards to the adapter on click
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onFilmClick?.invoke(item)
            }
        }
        binding.like.setOnClickListener { // Triggers click upwards to the adapter on click
            likedState = !likedState
            onFilmLike?.invoke(item, likedState)
        }
        bind {
            binding.nameText.text = item.title
            nameTextShader?.let {
                binding.nameText.paint.shader = nameTextShader
            }
            binding.reviewsText.text = context.getString(
                R.string.reviews_number,
                item.voteCount
            )
            binding.ratingBar.rating = item.getRatingIn5Stars()
            binding.genreText.text = item.getGenresString()
//            binding.ageText.text = "$age+"

//            binding.timeText.text = context.getString(R.string.minutes_number, item.runtime)
            ImageLoader.loadImage(binding.photoImage, item.posterUrl)
            likedState = item.isFavorite
            setLikeColor(likedState, binding.like, context)
        }
    }

fun loadAdapterDelegate(
) =
    adapterDelegateViewBinding<LoadItem, ListItem, ItemLoadingBinding>(
        { layoutInflater, root -> ItemLoadingBinding.inflate(layoutInflater, root, false) }
    ) {


    }