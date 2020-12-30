package ua.shishkoam.fundamentals

import ua.shishkoam.fundamentals.domain.OnlineLessonRepository

internal class OnlineLessonRepositoryImpl(
    private val movieRetrofitService: MovieRetrofitInterface
) : OnlineLessonRepository {
    override suspend fun sendStatus(repoName: String, timeInMillis: Int, status: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getStatus(id: Int) {
        TODO("Not yet implemented")
    }


}
