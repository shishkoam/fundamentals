package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDTO(
    val adult: Boolean = false,
    val backdrop_path: String?,
    @SerialName("genre_ids")
    var genreIds: List<Int> = emptyList(),
    val id: Int,
    @SerialName("original_language")
    var originalLanguage: String? = null,
    @SerialName("original_title")
    var originalTitle: String? = null,
    var overview: String? = null,
    var popularity: Float = 0.0f,
    @SerialName("poster_path")
    var posterPath: String? = null,
    @SerialName("release_date")
    var releaseDate: String? = null,
    var title: String? = null,
    var video: Boolean = false,
    @SerialName("vote_average")
    var voteAverage: Float = 0.0f,
    @SerialName("vote_count")
    var voteCount: Int = 0,
)
