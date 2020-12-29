package ua.shishkoam.fundamentals

import android.app.Application
import androidx.lifecycle.ViewModel
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule
import ua.shishkoam.fundamentals.data.MovieRepositoryImpl
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.presentation.viewmodels.FilmsListViewModel

class App : Application(), DIAware {
    override val di = DI.lazy {
        import(androidXModule(this@App))
        bind<MovieRepository>() with singleton { MovieRepositoryImpl(this@App) }
        bind<ViewModel>(tag = FilmsListViewModel::class.java.simpleName) with provider {
            FilmsListViewModel(instance())
        }
    }
}