package ua.shishkoam.fundamentals.domain.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val adult: Boolean = false,
    val backdrop_path: String,
    var genre_ids: List<Int> = emptyList(),
    val id: Int,
    var original_language: String? = null,
    var original_title: String? = null,
    var overview: String? = null,
    var popularity: Float = 0.0f,
    var poster_path: String? = null,
    var release_date: String? = null,
    var title: String? = null,
    var video: Boolean = false,
    var vote_average: Float = 0.0f,
    var vote_count: Int = 0,
) : Parcelable {
    var isFavorite: Boolean = false
    var genresNames: HashSet<String>? = HashSet()
    var posterFullImageUrl: String? = null
    var backdropFullImageUrl: String? = null

    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.createIntList(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readFloat(),
        parcel.readInt()
    )

    fun getRatingIn5Stars(): Float {
        return vote_average / 2
    }

    fun setGenresNames(genresMap: HashMap<Int, String>) {
        genresNames?.clear()
        for (genreId in genre_ids) {
            genresNames?.add(genresMap[genreId] ?: "")
        }
        genresNames?.remove("")
    }

    fun getGenresNames() : String {
        if (genresNames.isNullOrEmpty()) {
            return ""
        }
        genresNames?.let{ names ->
            val sb = java.lang.StringBuilder()
            for (genre in names) {
                sb.append(genre).append(", ")
            }
            return sb.substring(0, sb.length-2)
        }
        return ""
    }

    fun clone(): Movie {
        val newMovie = copy()
        newMovie.posterFullImageUrl = posterFullImageUrl
        newMovie.backdropFullImageUrl = backdropFullImageUrl
        newMovie.genresNames = genresNames
        return newMovie
    }

    @NonNull
    fun setPosterFullImageUrl(configuration: Configuration) {
        val imagePath: String = if (poster_path.isNullOrEmpty()) {
            backdrop_path
        } else {
            poster_path!!
        }
        posterFullImageUrl = configuration.getFullImageUrl(imagePath)
    }

    @NonNull
    fun setBackdropFullImageUrl(configuration: Configuration) {
        val imagePath: String = backdrop_path
        backdropFullImageUrl = configuration.getFullImageUrl(imagePath)
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(backdrop_path)
        parcel.writeIntList(genre_ids)
        parcel.writeString(original_language)
        parcel.writeString(original_title)
        parcel.writeString(overview)
        parcel.writeValue(popularity)
        parcel.writeString(poster_path)
        parcel.writeString(release_date)
        parcel.writeString(title)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeFloat(vote_average)
        parcel.writeInt(vote_count)
    }
}

fun Parcel.writeIntList(input: List<Int>) {
    writeInt(input.size) // Save number of elements.
    input.forEach(this::writeInt) // Save each element.
}

fun Parcel.createIntList(): List<Int> {
    val size = readInt()
    val output = ArrayList<Int>(size)
    for (i in 0 until size) {
        output.add(readInt())
    }
    return output
}