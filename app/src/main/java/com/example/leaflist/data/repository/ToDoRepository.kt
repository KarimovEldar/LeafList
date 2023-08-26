package com.example.leaflist.data.repository

import androidx.lifecycle.LiveData
import com.example.leaflist.data.ToDoDao
import com.example.leaflist.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val readDatabase: LiveData<List<ToDoData>> = toDoDao.readDatabase()
    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()
    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData){
        toDoDao.deleteData(toDoData)
    }

    suspend fun deleteAllData(){
        toDoDao.deleteAllData()
    }

    fun searchDatabase(query:String):LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(query)
    }


}