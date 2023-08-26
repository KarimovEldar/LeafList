package com.example.leaflist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.leaflist.data.ToDoDatabase
import com.example.leaflist.data.model.ToDoData
import com.example.leaflist.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application){


    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository = ToDoRepository(toDoDao)

    var readDatabase: LiveData<List<ToDoData>> = repository.readDatabase
    var sortByLowPriority: LiveData<List<ToDoData>> = repository.sortByLowPriority
    var sortByHighPriority: LiveData<List<ToDoData>> = repository.sortByHighPriority

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteData(toDoData)
        }
    }

    fun deleteAllData(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllData()
        }
    }

    fun searchDatabase(query: String):LiveData<List<ToDoData>>{
        return repository.searchDatabase(query)
    }

}