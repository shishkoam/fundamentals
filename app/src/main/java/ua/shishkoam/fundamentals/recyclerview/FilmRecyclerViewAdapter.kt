package ua.shishkoam.fundamentals.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Film


class FilmRecyclerViewAdapter(private var values: List<Film>) :
    RecyclerView.Adapter<FilmRecyclerViewAdapter.RecyclerViewHolder>() {

    // Define listener member variable
    private var listener: OnItemClickListener? = null

    // Define the listener interface
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    class RecyclerViewHolder(view: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(
        view
    ) {
        val name: TextView = view.findViewById(R.id.name)
        val reviews: TextView = view.findViewById(R.id.reviews)
        val time: TextView = view.findViewById(R.id.time)
        val genre: TextView = view.findViewById(R.id.genre)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val ratingView: RatingBar = view.findViewById(R.id.rating)

        init {
            itemView.setOnClickListener { // Triggers click upwards to the adapter on click
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(itemView, position)
                }
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + name.text + "'"
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return RecyclerViewHolder(itemView, listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = values[position]
        holder.name.text = item.name
        holder.reviews.text = holder.reviews.context?.getString(R.string.reviews_number, item.reviewNum)
        holder.ratingView.rating = item.rating.toFloat()
        holder.genre.text = item.genres
        holder.time.text =holder.time.context?.getString(R.string.minutes_number, item.time)
        Glide.with(holder.imageView.context.applicationContext).load(item.image)
            .error(R.mipmap.ic_launcher)
            .into(holder.imageView)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = values.size

}