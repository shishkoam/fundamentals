package ua.shishkoam.fundamentals.recyclerview

import android.widget.ImageView
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import ua.shishkoam.fundamentals.ImageLoader
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.data.Actor

class ActorDelegateAdapter(actors: List<Actor>) : ListDelegationAdapter<List<Actor>>(
    actorAdapterDelegate()
) {
    init {
        items = actors
    }
}

fun actorAdapterDelegate() = adapterDelegate<Actor, Actor>(R.layout.view_holder_actor) {
    val name: TextView = findViewById(R.id.name_text)
    val photoImage: ImageView = findViewById(R.id.photo_image)
    bind {
        name.text = item.name
        ImageLoader.loadImage(photoImage, item.photo)
    }
}
