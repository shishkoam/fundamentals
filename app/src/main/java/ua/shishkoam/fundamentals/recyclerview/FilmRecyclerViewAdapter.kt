package ua.shishkoam.fundamentals.recyclerview

import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Film


class FilmRecyclerViewAdapter(
    private var values: List<Film>,
    private val nameShader: Shader? = null,
    private val likedFilms: Map<String, Boolean> = emptyMap(),
    private val onItemClickListener: OnItemClickListener? = null,
    private val onItemLikeListener: OnItemLikeListener? = null
) :
    RecyclerView.Adapter<FilmViewHolder>() {

    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    interface OnItemLikeListener {
        fun onItemLike(itemView: View?, position: Int)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilmViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return FilmViewHolder(itemView, onItemClickListener, onItemLikeListener, nameShader)
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