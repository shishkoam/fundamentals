package ua.shishkoam.fundamentals.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class Crew(
    val adult: Boolean = false,
    val gender: Int = 0,
    val id: Int = 0,
    val known_for_department: String? = null,
    val name: String? = null,
    val original_name: String? = null,
    val popularity: Double = 0.0,
    val profile_path: String? = null,
    val credit_id: String? = null,
    val department: String? = null,
    val job: String? = null
) {
}