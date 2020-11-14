package ua.shishkoam.fundamentals.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.dummy.DummyContent


class RecyclerViewAdapter(private var values: List<DummyContent.DummyItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val rating: TextView = view.findViewById(R.id.rating)
        val time: TextView = view.findViewById(R.id.time)
        val genre: TextView = view.findViewById(R.id.genre)

        //        val like: ImageView = view.findViewById(R.id.li)
        val imageView: ImageView = view.findViewById(R.id.imageView)

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
        return RecyclerViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = values[position]
        if (holder is RecyclerViewHolder) {
            holder.name.text = item.name
            holder.rating.text = item.rating.toString()
            holder.genre.text = item.genres
            holder.time.text = item.time.toString()
            Glide.with(holder.imageView.context.applicationContext).load(item.image)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView)
//             bind data to the views of MyViewHolder1
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = values.size

}