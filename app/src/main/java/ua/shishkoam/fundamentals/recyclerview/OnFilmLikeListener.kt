package ua.shishkoam.fundamentals.recyclerview

import ua.shishkoam.fundamentals.data.Movie


interface OnFilmLikeListener {
    fun onFilmLike(item: Movie, likedState: Boolean)
}