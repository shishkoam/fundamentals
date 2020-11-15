package ua.shishkoam.fundamentals.data

data class Film(
    val name: String,
    val time: Int,
    val image: Int,
    var like: Boolean = false,
    var age: Int = 13,
    var rating: Double = 0.0,
    var reviewNum: Int = 0,
    val genres: String?
) {
    override fun toString(): String = name
}