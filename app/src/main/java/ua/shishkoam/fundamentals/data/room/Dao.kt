package ua.shishkoam.fundamentals.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {
    @Query("SELECT * FROM movies")
//    @Query("SELECT * FROM movies ORDER BY _id ASC")
    suspend fun getAll(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: ActorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: ArrayList<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors: ArrayList<ActorEntity>)

    @Query("SELECT * FROM movies WHERE _id == :id")
    suspend fun getById(id: Long): MovieEntity

    @Query("SELECT * FROM actors WHERE _id == :id")
    suspend fun getActorById(id: Long): ActorEntity

    @Query("SELECT * FROM actors WHERE _id == :id")
    suspend fun getActorsByIds(id: ArrayList<Long>): List<ActorEntity>

    @Query("DELETE FROM movies WHERE _id == :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(_id) FROM movies")
    fun getMoviesCount(): LiveData<Int>
}