package ua.shishkoam.fundamentals.data

import java.io.Serializable

data class Actor (
    val name: String,
    val image: Int
) : Serializable {
    override fun toString(): String = name
}