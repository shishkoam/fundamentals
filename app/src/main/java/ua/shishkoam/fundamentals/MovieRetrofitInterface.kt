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
    @GET("movie/now_playong?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getNowPlayong() : MoviePage

    @GET("movie/popular?api_key=869b66870caf75518f1b359b0a5da125&language=en-US&page=1")
    suspend fun getPopular(): MoviePage

    @GET("genre/movie/list?api_key=869b66870caf75518f1b359b0a5da125&language=en-US")
    suspend fun getGenreList(): GenreResponse

    @GET("movie/{movie_id}/credits?api_key=869b66870caf75518f1b359b0a5da125&language=en-US")
    suspend fun getMovieCredits(@Path("movie_id") id: Int): Cast

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