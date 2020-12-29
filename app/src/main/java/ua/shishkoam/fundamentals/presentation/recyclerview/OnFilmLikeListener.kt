package ua.shishkoam.fundamentals.presentation.recyclerview

import ua.shishkoam.fundamentals.data.Movie


interface OnFilmLikeListener {
    fun onFilmLike(item: Movie, likedState: Boolean)
}