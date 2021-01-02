package ua.shishkoam.fundamentals.domain.data

import kotlinx.serialization.Serializable

@Serializable
class GenreResponse {
    var genres: List<Genre>? = null
}