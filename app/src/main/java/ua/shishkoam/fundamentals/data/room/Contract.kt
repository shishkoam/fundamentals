package ua.shishkoam.fundamentals.data.room

import android.provider.BaseColumns

object Contract {

    const val DATABASE_NAME = "movies.db"

    object Movies {
        const val TABLE_NAME = "movies"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_ORIGINAL_LANGUAGE = "originalLanguage"
        const val COLUMN_NAME_ORIGINAL_TITLE = "originalTitle"
        const val COLUMN_NAME_OVERVIEW = "overview"
        const val COLUMN_NAME_RELEASE_DATE = "releaseDate"
        const val COLUMN_NAME_VOTE_AVERAGE = "voteAverage"
        const val COLUMN_NAME_VOTE_COUNT = "voteCount"
        const val COLUMN_NAME_POSTER_URL = "posterUrl"
        const val COLUMN_NAME_BACKDROP_URL = "backdropUrl"
        const val COLUMN_NAME_IS_FAVORITE = "isFavorite"
        const val COLUMN_NAME_GENRES_NAMES = "genresNames"
        const val COLUMN_NAME_ACTORS = "actors"
    }

    object Actors {
        const val TABLE_NAME = "actors"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_ORDER = "order"
        const val COLUMN_NAME_IMAGE_URL = "imageUrl"
    }
}