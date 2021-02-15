package ua.shishkoam.fundamentals.domain

import ua.shishkoam.fundamentals.domain.data.Movie


interface NotificationRepository {
    suspend fun updateBestMovie(movie: Movie)
}