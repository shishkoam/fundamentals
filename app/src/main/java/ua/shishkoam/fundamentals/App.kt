package ua.shishkoam.fundamentals

import android.app.Application
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule
import retrofit2.Retrofit
import ua.shishkoam.fundamentals.data.MovieRepositoryImpl
import ua.shishkoam.fundamentals.domain.MovieRepository
import ua.shishkoam.fundamentals.presentation.viewmodels.FilmsListViewModel

class App : Application(), DIAware {
    private final val BASE_URL = "https://api.themoviedb.org/3/"

    override val di = DI.lazy {
        import(androidXModule(this@App))

        bind<Retrofit>() with singleton {
            val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(AuthApiHeaderInterceptor())
                .build()

            Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        bind<MovieRetrofitInterface>() with singleton {
            instance<Retrofit>().create(MovieRetrofitInterface::class.java)
        }



        bind<MovieRepository>() with singleton { MovieRepositoryImpl(instance()) }

        bind<ViewModel>(tag = FilmsListViewModel::class.java.simpleName) with provider {
            FilmsListViewModel(instance())
        }
    }

//    private final val API_KEY_HEADER = "api_key"
//    private final val api = "869b66870caf75518f1b359b0a5da125"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private class AuthApiHeaderInterceptor : Interceptor {
        private final val API_KEY_HEADER = "api_key"
        private final val api = "869b66870caf75518f1b359b0a5da125"

        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url
            val request = originalRequest.newBuilder()
                .url(originalHttpUrl)
                .addHeader(API_KEY_HEADER, api)
//                .addHeader("language", "en-US")
//                .addHeader("page", "1")
                .build()

            return chain.proceed(request)
        }
    }
}