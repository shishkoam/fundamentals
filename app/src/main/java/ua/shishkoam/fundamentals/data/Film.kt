package ua.shishkoam.fundamentals.data
import kotlinx.serialization.Serializable

@Serializable
data class Film(
    val name: String,
    val time: Int,
    val image: Int,
    var like: Boolean = false,
    var age: Int = 13,
    var rating: Double = 0.0,
    var reviewNum: Int = 0,
    val genres: String?,
    val story: String = "",
    val cast: List<Actor>? = null,
    val imageBig: Int? = null
) {
    override fun toString(): String = name
}