package ua.shishkoam.fundamentals.recyclerview

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ua.shishkoam.fundamentals.ImageLoader
import ua.shishkoam.fundamentals.data.Actor
import ua.shishkoam.fundamentals.databinding.ViewHolderActorBinding

class ActorDelegateAdapter(actors: List<Actor>) : ListDelegationAdapter<List<Actor>>(
    actorAdapterDelegate()
) {
    init {
        items = actors
    }
}

fun actorAdapterDelegate() = adapterDelegateViewBinding<Actor, Actor, ViewHolderActorBinding>(
    { layoutInflater, root -> ViewHolderActorBinding.inflate(layoutInflater, root, false) }
) {
    bind {
        binding.nameText.text = item.name
        ImageLoader.loadImage(binding.photoImage, item.picture)
    }
}