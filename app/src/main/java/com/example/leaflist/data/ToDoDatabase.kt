package com.example.leaflist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.leaflist.data.model.ToDoData
import com.example.leaflist.utils.Constants.Companion.DATABASE_NAME

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class ToDoDatabase : RoomDatabase(){

    abstract fun toDoDao() : ToDoDao

    companion object{

        @Volatile
        var INSTANCE : ToDoDatabase? = null

        fun getDatabase(context : Context):ToDoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context : Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ToDoDatabase::class.java,
                DATABASE_NAME).build()

    }

}