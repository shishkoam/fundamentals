package ua.shishkoam.fundamentals.domain.data

data class Actor(
    val id:Int = 0,
    val name: String? = null,
    val order:Int = 0,
    var imageUrl: String? = null
)