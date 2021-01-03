package ua.shishkoam.fundamentals.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class Cast (
    val id: Int = 0,
    val cast: List<Actor>? = emptyList(),
    val crew: List<Crew>? = emptyList()){
}