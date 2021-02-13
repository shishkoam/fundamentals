package ua.shishkoam.fundamentals.data

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.CalendarContract
import ua.shishkoam.fundamentals.domain.CalendarInteractor
import ua.shishkoam.fundamentals.domain.data.Movie
import java.util.*

class CalendarInteractorImpl(val applicationContext: Context): CalendarInteractor {
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
        val calID: Long = 3 // Make sure to which calender you want to add event
        var startMillis: Long = 0
        var endMillis: Long = 0
        startMillis = scheduleDate.timeInMillis
        endMillis = startMillis + 2 * 60 * 60 * 1000

        val values = ContentValues()
        values.put(CalendarContract.Events.DTSTART, startMillis)
        values.put(CalendarContract.Events.DTEND, endMillis)
        values.put(CalendarContract.Events.TITLE, movie.title)
        values.put(CalendarContract.Events.DESCRIPTION, movie.overview)
        values.put(CalendarContract.Events.CALENDAR_ID, calID)
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        values.put(CalendarContract.Events.HAS_ALARM, 1)
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
}