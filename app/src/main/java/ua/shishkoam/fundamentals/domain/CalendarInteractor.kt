package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.domain.data.Movie

interface CalendarInteractor {
    fun addMovieToCalendar(
        movie: Movie,
        year: Int,
        month: Int,
        day: Int,
        hourOfDay: Int,
        minute: Int
    )
}