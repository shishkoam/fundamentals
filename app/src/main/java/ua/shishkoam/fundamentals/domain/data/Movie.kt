package ua.shishkoam.fundamentals.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    var originalLanguage: String? = null,
    var originalTitle: String? = null,
    var overview: String? = null,
    var releaseDate: String? = null,
    var title: String? = null,
    var voteAverage: Float = 0.0f,
    var voteCount: Int = 0,
    var posterUrl: String? = null,
    var backdropUrl: String? = null,
    var isFavorite: Boolean = false,
    var genresNames: HashSet<String>? = HashSet()
) : Parcelable, ListItem {

    fun getRatingIn5Stars(): Float {
        return voteAverage / 2
    }

    fun getGenresString(): String {
        return if (genresNames.isNullOrEmpty()) {
            ""
        } else {
            val sb = java.lang.StringBuilder()
            for (genre in genresNames!!) {
                sb.append(genre).append(", ")
            }
            sb.substring(0, sb.length - 2)
        }
    }
}