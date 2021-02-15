package ua.shishkoam.fundamentals.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import ua.shishkoam.fundamentals.domain.CalendarRepository
import ua.shishkoam.fundamentals.domain.data.Movie
import java.util.*

class CalendarRepositoryImpl(private val applicationContext: Context): CalendarRepository {
    companion object {
        private const val TWO_HOURS = 2 * 60 * 60 * 1000
    }

    override fun addMovieToCalendar(
        movie: Movie,
        year: Int,
        month: Int,
        day: Int,
        hourOfDay: Int,
        minute: Int
    ) : Boolean {
        val cr = applicationContext.contentResolver
        val scheduleDate = Calendar.getInstance()
        scheduleDate.set(year, month, day, hourOfDay, minute)
        val calID = getCalendarId(applicationContext)
        val startMillis: Long = scheduleDate.timeInMillis
        val endMillis: Long = startMillis + TWO_HOURS

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, movie.title)
            put(CalendarContract.Events.DESCRIPTION, movie.overview)
            put(CalendarContract.Events.CALENDAR_ID, calID)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.HAS_ALARM, 1)
        }
        val uri: Uri? = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventID: Long = uri?.lastPathSegment?.toLong() ?: -1
        return eventID.toInt() != -1

//        send user to calendar
//        val cal = Calendar.getInstance()
//        val intent = Intent(Intent.ACTION_EDIT)
//        intent.type = "vnd.android.cursor.item/event"
//        intent.putExtra("beginTime", cal.timeInMillis)
//        intent.putExtra("title", "A Test Event from android app")
//        startActivity(intent)
    }

    private fun getCalendarId(context: Context): Long? {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )

        var calCursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + " = 1",
            null,
            CalendarContract.Calendars._ID + " ASC"
        )

        if (calCursor != null && calCursor.count <= 0) {
            calCursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                CalendarContract.Calendars.VISIBLE + " =1",
                null,
                CalendarContract.Calendars._ID + " ASC"
            )
        }

        if (calCursor != null && calCursor.moveToFirst()) {
            val calID = calCursor.getString(calCursor.getColumnIndex(projection[0]))
                .also {
                    calCursor.close()
                }

            return calID.toLong()
        }

        calCursor?.close()
        return null
    }
}