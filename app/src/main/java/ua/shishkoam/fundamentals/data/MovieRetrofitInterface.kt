package ua.shishkoam.fundamentals.data

import by.kirich1409.result.RequestResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ua.shishkoam.fundamentals.data.dto.Cast
import ua.shishkoam.fundamentals.data.dto.Configuration
import ua.shishkoam.fundamentals.data.dto.GenreResponse
import ua.shishkoam.fundamentals.data.dto.MoviePage

interface MovieRetrofitInterface {
    @Headers("Content-Type: application/json")
    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int) : RequestResult<MoviePage?>

    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int): RequestResult<MoviePage?>

    @GET("genre/movie/list")
    suspend fun getGenreList(): RequestResult<GenreResponse?>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(@Path("movie_id") id: Int): RequestResult<Cast?>

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") page: Int): RequestResult<MoviePage?>

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("page") page: Int): RequestResult<MoviePage?>

    @GET("configuration")
    suspend fun getConfiguration(): RequestResult<Configuration?>


}