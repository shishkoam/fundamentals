package ua.shishkoam.fundamentals.data

import ua.shishkoam.fundamentals.data.dto.ActorDTO
import ua.shishkoam.fundamentals.data.dto.Configuration
import ua.shishkoam.fundamentals.domain.data.Actor

fun ActorDTO.toDomainActor(configuration: Configuration): Actor {
    val actor = Actor(id, name, order)
    profilePath?.let { path ->
        actor.imageUrl = configuration.getFullImageUrl(path)
    }
    return actor
}