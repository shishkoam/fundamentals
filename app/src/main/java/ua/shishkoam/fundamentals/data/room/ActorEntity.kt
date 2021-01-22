package ua.shishkoam.fundamentals.data.room

import androidx.room.*

@Entity(
    tableName = Contract.Actors.TABLE_NAME,
    indices = [Index(Contract.Actors.COLUMN_NAME_ID)]
)
data class ActorEntity (
    @PrimaryKey()
    @ColumnInfo(name = Contract.Actors.COLUMN_NAME_ID)
    val id: Long,
    @ColumnInfo(name = Contract.Actors.COLUMN_NAME_NAME)
    val name: String,
    @ColumnInfo(name = Contract.Actors.COLUMN_NAME_ORDER)
    val order: Int,
    @ColumnInfo(name = Contract.Actors.COLUMN_NAME_IMAGE_URL)
    val imageUrl: String
)