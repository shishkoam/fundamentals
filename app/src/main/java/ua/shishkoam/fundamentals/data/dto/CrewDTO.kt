package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrewDTO(
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
    @SerialName("credit_id")
    val creditId: String? = null,
    val department: String? = null,
    val job: String? = null
)