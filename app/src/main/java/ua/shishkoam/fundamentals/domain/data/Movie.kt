package ua.shishkoam.fundamentals.domain.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    var originalLanguage: String,
    var originalTitle: String,
    var overview: String,
    var releaseDate: String,
    var title: String,
    var voteAverage: Float = 0.0f,
    var voteCount: Int = 0,
    var posterUrl: String? = null,
    var backdropUrl: String? = null,
    var isFavorite: Boolean = false,
    var genresNames: HashSet<String> = HashSet()
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