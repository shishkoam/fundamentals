package ua.shishkoam.fundamentals.domain.data

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val adult:Boolean = false,
    val gender:Int = 0,
    val id:Int = 0,
    val known_for_department: String? = null,
    val name: String? = null,
    val original_name: String? = null,
    val popularity:Double = 0.0,
    val profile_path: String? = null,
    val cast_id:Int = 0,
    val character: String? = null,
    val credit_id: String? = null,
    val order:Int = 0
) {
    var profileFullImageUrl: String? = null

    @NonNull
    fun setProfileFullImageUrl(configuration: Configuration) {
        profile_path?.let {path->
            profileFullImageUrl = configuration.getFullImageUrl(path)
        }
    }
}