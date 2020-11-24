package ua.shishkoam.fundamentals.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.R

class ActorViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.name)
    val imageView: ImageView = view.findViewById(R.id.imageView)

    override fun toString(): String {
        return super.toString() + " '" + name.text + "'"
    }
}
