package ua.shishkoam.fundamentals.domain.data

import kotlinx.serialization.Serializable

@Serializable
class MoviePage {
    var page = 0
    var results: List<Movie>? = null
    var total_pages = 0
    var total_results: Int = 0

}