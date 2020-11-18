package ua.shishkoam.fundamentals.recyclerview

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val textShader: Shader?

        init {
            itemView.setOnClickListener { // Triggers click upwards to the adapter on click
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(itemView, position)
                }
            }
            val context: Context = name.context
            val first = ContextCompat.getColor(context, R.color.text_color_gradient_1);
            val second = ContextCompat.getColor(context, R.color.text_color_gradient_2);
            val third = ContextCompat.getColor(context, R.color.text_color_gradient_3);
            textShader = LinearGradient(
                0f,
                0f,
                0f,
                50f,
                intArrayOf(first, second, third),
                floatArrayOf(0f, 0.5f, 1f),
                TileMode.CLAMP
            )

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
        val context: Context = holder.name.context
//        val first = ContextCompat.getColor(context, R.color.text_color_gradient_1);
//        val second = ContextCompat.getColor(context, R.color.text_color_gradient_2);
//        val third = ContextCompat.getColor(context, R.color.text_color_gradient_3);
//        val textShader: Shader = LinearGradient(
//            0f,
//            0f,
//            0f,
//            50f,
//            intArrayOf(first, second, third),
//            floatArrayOf(0f, 1f),
//            TileMode.CLAMP
//        )
        holder.name.paint.shader = holder.textShader
        holder.reviews.text = context.getString(
            R.string.reviews_number,
            item.reviewNum
        )
        holder.ratingView.rating = item.rating.toFloat()
        holder.genre.text = item.genres
        holder.time.text =context.getString(R.string.minutes_number, item.time)
        Glide.with(context.applicationContext).load(item.image)
            .error(R.mipmap.ic_launcher)
            .into(holder.imageView)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = values.size

}