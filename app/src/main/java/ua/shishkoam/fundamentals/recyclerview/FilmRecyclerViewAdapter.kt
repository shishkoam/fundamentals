package ua.shishkoam.fundamentals.recyclerview

import android.content.Context
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Film


class FilmRecyclerViewAdapter(
    private var values: List<Film>,
    private val nameShader: Shader? = null,
    private val likes: Map<String, Boolean> = emptyMap(),
    private val listener: OnItemClickListener? = null,
    private val likeListener: OnItemLikeListener? = null
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
        return FilmViewHolder(itemView, listener, likeListener, nameShader)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = values[position]
        holder.name.text = item.name
        val context: Context = holder.name.context
        holder.textShader?.let {
            holder.name.paint.shader = holder.textShader
        }
        holder.reviews.text = context.getString(
            R.string.reviews_number,
            item.reviewNum
        )
        holder.ratingView.rating = item.rating.toFloat()
        holder.genre.text = item.genres
        holder.time.text = context.getString(R.string.minutes_number, item.time)
        Glide.with(context.applicationContext).load(item.image)
            .error(R.mipmap.ic_launcher)
            .into(holder.imageView)
        holder.age.text = "${item.age}+"
        holder.setLike(likes[item.name] == true)
    }




    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = values.size

}