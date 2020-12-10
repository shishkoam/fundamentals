package ua.shishkoam.fundamentals.recyclerview

import ua.shishkoam.fundamentals.data.Movie

interface OnFilmClickListener {
    fun onFilmClick(item: Movie)
}