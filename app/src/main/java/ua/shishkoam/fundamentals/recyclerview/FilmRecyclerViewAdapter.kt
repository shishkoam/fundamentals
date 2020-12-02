package ua.shishkoam.fundamentals.recyclerview

import android.graphics.Shader
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Film


class FilmRecyclerViewAdapter(
    private var values: List<Film>,
    private val nameShader: Shader? = null,
    private val likedFilms: Map<String, Boolean> = emptyMap(),
    private val onFilmClickListener: OnFilmClickListener? = null,
    private val onFilmLikeListener: OnFilmLikeListener? = null
) :
    RecyclerView.Adapter<FilmViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_movie, parent, false)
        return FilmViewHolder(itemView, onFilmClickListener, onFilmLikeListener, nameShader)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = values[position]
        holder.onBind(item, likedFilms[item.name] == true)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = values.size

}