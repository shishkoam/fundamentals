package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MoviePage {
    var page = 0
    var results: List<MovieDTO>? = null
    @SerialName("total_pages")
    var totalPages = 0
    @SerialName("total_results")
    var totalResults: Int = 0

}