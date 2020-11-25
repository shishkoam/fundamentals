package ua.shishkoam.fundamentals.data

import android.os.Parcel
import android.os.Parcelable

data class Film(
    val name: String = "empty",
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "empty",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Actor),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun toString(): String = "$name $story"
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(time)
        parcel.writeInt(image)
        parcel.writeByte(if (like) 1 else 0)
        parcel.writeInt(age)
        parcel.writeDouble(rating)
        parcel.writeInt(reviewNum)
        parcel.writeString(genres)
        parcel.writeString(story)
        parcel.writeTypedList(cast)
        parcel.writeValue(imageBig)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Film> {
        override fun createFromParcel(parcel: Parcel): Film {
            return Film(parcel)
        }

        override fun newArray(size: Int): Array<Film?> {
            return arrayOfNulls(size)
        }
    }
}