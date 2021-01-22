package ua.shishkoam.fundamentals.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Query("SELECT * FROM movies")
//    @Query("SELECT * FROM movies ORDER BY _id ASC")
    suspend fun getAll(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: ActorEntity)

    @Query("SELECT * FROM movies WHERE _id == :id")
    suspend fun getById(id: Long) : MovieEntity

    @Query("SELECT * FROM actors WHERE _id == :id")
    suspend fun getActorById(id: Long): ActorEntity

    @Query("SELECT * FROM actors WHERE _id == :id")
    suspend fun getActorsByIds(id: ArrayList<Long>): List<ActorEntity>

    @Query("DELETE FROM movies WHERE _id == :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(_id) FROM movies")
    fun getMoviesCount(): LiveData<Int>
}