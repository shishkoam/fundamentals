package ua.shishkoam.fundamentals

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ua.shishkoam.fundamentals.domain.data.Cast
import ua.shishkoam.fundamentals.domain.data.Configuration
import ua.shishkoam.fundamentals.domain.data.GenreResponse
import ua.shishkoam.fundamentals.domain.data.MoviePage

//?size=small&order=RANDOM&limit=5&format=json
interface MovieRetrofitInterface {
    @Headers("Content-Type: application/json")
    @GET("movie/now_playing?")
    suspend fun getNowPlaying() : MoviePage?

    @GET("movie/popular?")
    suspend fun getPopular(): MoviePage?

    @GET("genre/movie/list?")
    suspend fun getGenreList(): GenreResponse?

    @GET("movie/{movie_id}/credits?")
    suspend fun getMovieCredits(@Path("movie_id") id: Int): Cast?

    @GET("movie/top_rated?")
    suspend fun getTopRated(): MoviePage?

    @GET("movie/upcoming?")
    suspend fun getUpcoming(): MoviePage?

    @GET("configuration?")
    suspend fun getConfiguration(): Configuration?


}