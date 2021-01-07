package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Images (
    @SerialName("base_url")
    var baseUrl: String? = null,
    @SerialName("secure_base_url")
    var secureBaseUrl: String? = null,
    @SerialName("backdrop_sizes")
    var backdropSizes: List<String>? = null,
    @SerialName("logo_sizes")
    var logoSizes: List<String>? = null,
    @SerialName("poster_sizes")
    var posterSizes: List<String>? = null,
    @SerialName("profile_sizes")
    var profileSizes: List<String>? = null,
    @SerialName("still_sizes")
    var stillSizes: List<String>? = null
) {
}