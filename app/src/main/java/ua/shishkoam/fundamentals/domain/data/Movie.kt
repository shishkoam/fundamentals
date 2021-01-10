package ua.shishkoam.fundamentals.domain.data

import android.os.Parcel
import android.os.Parcelable

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

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readStringSet()
    ) {
    }

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

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalTitle)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(title)
        parcel.writeFloat(voteAverage)
        parcel.writeInt(voteCount)
        parcel.writeString(posterUrl)
        parcel.writeString(backdropUrl)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeStringSet(genresNames)
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}

fun Parcel.writeStringSet(input: Set<String>?) {
    writeInt(input?.size ?: 0) // Save number of elements.
    input?.forEach(this::writeString) // Save each element.
}

fun Parcel.readStringSet(): HashSet<String> {
    val size = readInt()
    val output = HashSet<String>(size)
    for (i in 0 until size) {
        output.add(readString() ?: "")
    }
    return output
}