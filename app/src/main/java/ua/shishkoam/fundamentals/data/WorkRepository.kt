package ua.shishkoam.fundamentals.data

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit


class WorkRepository {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
//        .setRequiresCharging(true)
        .build()
    var constrainedRequest =
        PeriodicWorkRequest.Builder(UpdateMoviesWorker::class.java, 1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
}