package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class Cast(
    val id: Int = 0,
    val cast: List<ActorDTO>? = emptyList(),
    val crew: List<CrewDTO>? = emptyList()
)