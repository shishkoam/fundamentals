package ua.shishkoam.fundamentals.presentation.recyclerview

import ua.shishkoam.fundamentals.data.Movie

interface OnFilmClickListener {
    fun onFilmClick(item: Movie)
}