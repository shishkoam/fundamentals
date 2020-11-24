package ua.shishkoam.fundamentals.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Actor

class ActorRecyclerViewAdapter(private var values: List<Actor>) :
    RecyclerView.Adapter<ActorViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.actor_item, parent, false)
        return ActorViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val item = values[position]
        if (holder is ActorViewHolder) {
            holder.name.text = item.name
            Glide.with(holder.imageView.context.applicationContext).load(item.image)
                .error(R.mipmap.ic_launcher)
                .into(holder.imageView)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = values.size

}