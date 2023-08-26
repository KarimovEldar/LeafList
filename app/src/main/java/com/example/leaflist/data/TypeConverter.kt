package com.example.leaflist.data

import androidx.room.TypeConverter
import com.example.leaflist.data.model.Priority

class TypeConverter {

    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String):Priority{
        return Priority.valueOf(priority)
    }

}