package com.example.leaflist.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.leaflist.utils.Constants.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class ToDoData (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val priority: Priority,
    val description : String
): Parcelable