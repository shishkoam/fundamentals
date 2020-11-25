package ua.shishkoam.fundamentals.data

import android.os.Parcel
import android.os.Parcelable

data class Actor (
    val name: String,
    val photo: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "empty",
        parcel.readInt()
    )

    override fun toString(): String = name

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Actor> {
        override fun createFromParcel(parcel: Parcel): Actor {
            return Actor(parcel)
        }

        override fun newArray(size: Int): Array<Actor?> {
            return arrayOfNulls(size)
        }
    }
}