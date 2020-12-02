package ua.shishkoam.fundamentals.recyclerview

import ua.shishkoam.fundamentals.data.Film

interface OnFilmLikeListener {
    fun onFilmLike(item: Film, likedState: Boolean)
}