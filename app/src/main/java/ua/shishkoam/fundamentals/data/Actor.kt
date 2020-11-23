package ua.shishkoam.fundamentals.data

import kotlinx.serialization.Serializable

@Serializable
data class Actor(
    val name: String,
    val image: Int
) {
    override fun toString(): String = name
}