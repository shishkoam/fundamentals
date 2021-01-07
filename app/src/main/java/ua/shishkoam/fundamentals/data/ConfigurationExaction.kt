package ua.shishkoam.fundamentals.data

import ua.shishkoam.fundamentals.data.dto.Configuration

fun Configuration.getFullImageUrl(imagePath: String): String {
    val images = this.images
    if (images?.baseUrl?.isNotEmpty() == true && images.posterSizes?.isNotEmpty() == true) {
        val size = images.posterSizes?.size ?: 0
        return if (size > 4) {
            // usually equal to 'w500'
            images.baseUrl + images.posterSizes?.get(4) + imagePath
        } else {
            // back-off to hard-coded value
            images.baseUrl.toString() + "w500" + imagePath
        }
    }
    return ""
}
