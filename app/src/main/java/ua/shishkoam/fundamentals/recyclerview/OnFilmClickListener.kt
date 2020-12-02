package ua.shishkoam.fundamentals.recyclerview

import ua.shishkoam.fundamentals.data.Film

interface OnFilmClickListener {
    fun onFilmClick(item: Film)
}