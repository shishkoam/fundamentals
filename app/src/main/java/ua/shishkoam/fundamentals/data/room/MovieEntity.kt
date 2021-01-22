package ua.shishkoam.fundamentals.data.room

import androidx.room.*
import androidx.room.Entity

@Entity(
    tableName = Contract.Movies.TABLE_NAME,
    indices = [Index(Contract.Movies.COLUMN_NAME_ID)]
)
@TypeConverters(Converter::class)
data class MovieEntity (
    @PrimaryKey()
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_ID)
    val id: Long,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_TITLE)
    val title: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_ORIGINAL_LANGUAGE)
    val originalLanguage: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_ORIGINAL_TITLE)
    val originalTitle: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_OVERVIEW)
    val overview: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_RELEASE_DATE)
    val releaseDate: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_VOTE_AVERAGE)
    val voteAverage: Float,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_VOTE_COUNT)
    val voteCount: Int,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_POSTER_URL)
    val posterUrl: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_BACKDROP_URL)
    val backdropUrl: String,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_IS_FAVORITE)
    val isFavorite: Boolean,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_GENRES_NAMES)
    val genresNames: HashSet<String>,
    @ColumnInfo(name = Contract.Movies.COLUMN_NAME_ACTORS)
    var actors: ArrayList<Long>
)