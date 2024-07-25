package jdw.example.dwsimplemusicplayer.utils

import androidx.room.TypeConverter

class LongListConverter {
    @TypeConverter
    fun fromString(value: String): List<Long> {
        if (value == "") return emptyList()
        return value.split(",").map { it.toLong() }
    }

    @TypeConverter
    fun toString(list: List<Long>): String {
        return list.joinToString(",")
    }
}