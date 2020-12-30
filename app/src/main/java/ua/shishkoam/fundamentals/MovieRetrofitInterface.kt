package ua.shishkoam.fundamentals

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import ua.shishkoam.fundamentals.data.Configuration
import ua.shishkoam.fundamentals.data.MoviePage

//?size=small&order=RANDOM&limit=5&format=json
interface MovieRetrofitInterface {
    @Headers("Content-Type: application/json")
    @GET("movie/now_playong?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getNowPlayong() : MoviePage

    @GET("movie/popular?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getPopular(): MoviePage

    @GET("movie/top_rated?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getTopRated(): MoviePage

    @GET("movie/upcoming?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getUpcoming(): MoviePage

    @GET("configuration?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getConfiguration(): Configuration

//    @GET("movie/movie?language=en-US&page=1") //"{movie_id} "
//    suspend fun getMovie()
//
//    @GET("movie/person") //"{person_id} "
//    suspend fun getPerson()
//
//    @GET("search/movie")
//    suspend fun getSearchMovies(): List<Movie>

}