package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDTO(
    val adult: Boolean = false,
    val gender: Int = 0,
    val id: Int = 0,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    val name: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    val popularity: Double = 0.0,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("cast_id")
    val castId: Int = 0,
    val character: String? = null,
    @SerialName("credit_id")
    val creditId: String? = null,
    val order: Int = 0
)