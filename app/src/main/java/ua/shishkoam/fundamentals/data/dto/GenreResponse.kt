package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.Serializable

@Serializable
class GenreResponse {
    var genres: List<GenreDTO>? = null
}