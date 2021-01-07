package ua.shishkoam.fundamentals.data.dto

import kotlinx.serialization.Serializable

/**
 * Created by polbins on 25/02/2017.
 */

@Serializable
class Configuration(
    var images: Images? = null,
    var changeKeys: List<String>? = null
)