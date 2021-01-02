package ua.shishkoam.fundamentals.domain.data

import kotlinx.serialization.Serializable

/**
 * Created by polbins on 25/02/2017.
 */

@Serializable
class Configuration (
    var images: Images? = null,
    var changeKeys: List<String>? = null) {

    fun getFullImageUrl(imagePath: String): String {
        val images = this.images
        if (images?.base_url?.isNotEmpty() == true && images.poster_sizes?.isNotEmpty() == true) {
            val size = images.poster_sizes?.size ?: 0
            return if (size > 4) {
                // usually equal to 'w500'
                images.base_url + images.poster_sizes?.get(4) + imagePath
            } else {
                // back-off to hard-coded value
                images.base_url.toString() + "w500" + imagePath
            }
        }
        return ""
    }
}