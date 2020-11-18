package ua.shishkoam.fundamentals.data

data class Actor(
    val name: String,
    val image: Int
) {
    override fun toString(): String = name
}