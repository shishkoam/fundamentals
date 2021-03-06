/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.shishkoam.fundamentals.data

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.bumptech.glide.Glide
import ua.shishkoam.fundamentals.MainActivity
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.domain.NotificationRepository
import ua.shishkoam.fundamentals.domain.data.Movie

private const val USER_PREFERENCES_NAME = "user_preferences"

/**
 * Class that handles saving and retrieving user preferences
 */
class NotificationRepositoryImpl(private val context: Context) : NotificationRepository {

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        if (Build.VERSION.SDK_INT >= 26 && notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(
                    CHANNEL_NEW_MESSAGES,
                    NotificationManagerCompat.IMPORTANCE_HIGH
                )
                    .setName(context.getString(R.string.channel_new_movie))
                    .setDescription(context.getString(R.string.channel_new_movie_description))
                    .build()
            )
        }
    }

    companion object {
        /**
         * The notification channel for messages. This is used for showing Bubbles.
         */
        private const val CHANNEL_NEW_MESSAGES = "new_movies"

        private const val REQUEST_CONTENT = 1
        private const val MOVIE_TAG = "movie"

        private const val MOVIE_PREF_TAG = "movie_id"
        private const val MOVIE_BASE_URI = "https://ua.shishkoam.fundamentals/movie/"
    }

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(
            name = USER_PREFERENCES_NAME
        )

    override suspend fun updateBestMovie(movie: Movie) {
        dataStore.edit { preferences ->
            val prev = preferences[PreferencesKeys.MOVIE_ID]
            if (prev != movie.id) {
                preferences[PreferencesKeys.MOVIE_ID] = movie.id
                showNotification(movie)
            }
        }
    }

    private object PreferencesKeys {
        val MOVIE_ID = intPreferencesKey(MOVIE_PREF_TAG)
    }

    @WorkerThread
    fun showNotification(movie: Movie) {
        val contentUri = "$MOVIE_BASE_URI${movie.id}".toUri()

        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MESSAGES)
            .setContentTitle(movie.title)
            .setContentText(movie.overview)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            )
        val futureTarget = Glide.with(context)
            .asBitmap()
            .load(movie.posterUrl)
            .submit()

        val bitmap = futureTarget.get()
        builder.setLargeIcon(bitmap)
        Glide.with(context).clear(futureTarget)
        notificationManagerCompat.notify(MOVIE_TAG, movie.id, builder.build())
    }

    fun dismissNotification(chatId: Long) {
//        notificationManagerCompat.cancel(CHAT_TAG, chatId.toInt())
    }
}
