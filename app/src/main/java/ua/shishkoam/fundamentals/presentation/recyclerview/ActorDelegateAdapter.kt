package ua.shishkoam.fundamentals.presentation.recyclerview

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ua.shishkoam.fundamentals.databinding.ViewHolderActorBinding
import ua.shishkoam.fundamentals.domain.data.Actor
import ua.shishkoam.fundamentals.utils.ImageLoader

class ActorDelegateAdapter(actors: List<Actor> = emptyList()) : ListDelegationAdapter<List<Actor>>(
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
        ImageLoader.loadImage(binding.photoImage, item.imageUrl)
    }
}