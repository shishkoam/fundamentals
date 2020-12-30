package ua.shishkoam.fundamentals.domain

internal interface OnlineLessonRepository {

    suspend fun sendStatus(repoName: String, timeInMillis: Int, status: Int)

    suspend fun getStatus(id: Int)
}
