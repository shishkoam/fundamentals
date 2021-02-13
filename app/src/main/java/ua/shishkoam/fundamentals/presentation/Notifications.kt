/*
 * Copyright (C) 2020 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.shishkoam.fundamentals.presentation

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import ua.shishkoam.fundamentals.MainActivity
import ua.shishkoam.fundamentals.R
import ua.shishkoam.fundamentals.domain.data.Movie

/**
 * Handles all operations related to [Notification].
 */
interface Notifications {
    fun initialize()
    fun showNotification(movie: Movie)
    fun dismissNotification(chatId: Long)
}

class AndroidNotifications(private val context: Context) : Notifications {

    companion object {
        /**
         * The notification channel for messages. This is used for showing Bubbles.
         */
        private const val CHANNEL_NEW_MESSAGES = "new_movies"

        private const val REQUEST_CONTENT = 1

        private const val MOVIE_TAG = "movie"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
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

    @WorkerThread
    override fun showNotification(movie: Movie) {
        val icon = IconCompat.createWithContentUri(movie.posterUrl)
        val contentUri = "https://ua.shishkoam.fundamentals/movie/${movie.id}".toUri()
        val person = Person.Builder()
            .setName(movie.title)
            .setIcon(icon)
            .build()

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
//            .setWhen(chat.messages.last().timestamp)
//            .addReplyAction(context, contentUri)
//            .setStyle(
//                NotificationCompat.MessagingStyle(person)
//                    .run {
//                        for (message in chat.messages) {
//                            val m = NotificationCompat.MessagingStyle.Message(
//                                message.text,
//                                message.timestamp,
//                                if (message.isIncoming) person else null
//                            ).apply {
//                                if (message.photoUri != null) {
//                                    setData(message.photoMimeType, message.photoUri)
//                                }
//                            }
//                            if (!message.isNew) {
//                                addHistoricMessage(m)
//                            } else {
//                                addMessage(m)
//                            }
//                        }
//                        this
//                    }
//                    .setGroupConversation(false)
//            )

        notificationManagerCompat.notify(MOVIE_TAG, movie.id, builder.build())
    }

    override fun dismissNotification(chatId: Long) {
        notificationManagerCompat.cancel(MOVIE_TAG, chatId.toInt())
    }
}
