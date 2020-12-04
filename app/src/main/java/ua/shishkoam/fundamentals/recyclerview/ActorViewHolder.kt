package ua.shishkoam.fundamentals.recyclerview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ua.shishkoam.fundamentals.ImageLoader
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Actor

class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nameText: TextView = view.findViewById(R.id.name_text)
    private val photoImage: ImageView = view.findViewById(R.id.photo_image)

    override fun toString(): String {
        return super.toString() + " '" + nameText.text + "'"
    }

    fun onBind(actor: Actor) {
        nameText.text = actor.name
        ImageLoader.loadImage(photoImage, actor.photo)
    }
}
