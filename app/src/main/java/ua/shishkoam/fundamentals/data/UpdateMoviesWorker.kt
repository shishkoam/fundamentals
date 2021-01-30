package ua.shishkoam.fundamentals.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance
import ua.shishkoam.fundamentals.domain.MovieInteractor
import ua.shishkoam.fundamentals.utils.ConnectionChecker

class UpdateMoviesWorker (context: Context, params: WorkerParameters)
    : Worker(context, params), DIAware {
    override val di by di() { context }
    private val movieInteractor: MovieInteractor by instance()

    override fun doWork(): Result {
        for (attempt in 0..10){
            Thread.sleep(1000)
            Log.v("UpdateMoviesWorker", "attemp $attempt")
            if(ConnectionChecker.isOnline()){
                GlobalScope.launch {
                    movieInteractor.getMovies()
                    Log.v("UpdateMoviesWorker", "worker working")
                }
                return Result.success()
            }
        }
        return Result.failure()
    }
}