package ua.shishkoam.fundamentals.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, ActorEntity::class], version = 1)
abstract class DataBase : RoomDatabase() {

    abstract val dao: Dao

    companion object {

        fun create(applicationContext: Context): DataBase = Room.databaseBuilder(
            applicationContext,
            DataBase::class.java,
            Contract.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}