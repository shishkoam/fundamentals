package ua.shishkoam.fundamentals.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converter {
    @TypeConverter
    fun toGenresNames(value: String?): HashSet<String>? {
        val listType: Type = object : TypeToken<HashSet<String>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromGenresNames(genresNames: HashSet<String>?): String? {
        val gson = Gson()
        return gson.toJson(genresNames)
    }

    @TypeConverter
    fun toActors(value: String?): ArrayList<Long>? {
        val listType: Type = object : TypeToken<ArrayList<Long>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromActors(actors: ArrayList<Long>?): String? {
        val gson = Gson()
        return gson.toJson(actors)
    }
}