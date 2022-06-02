package com.dilip.room.database.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dilip.room.database.db.StudentRepository

class StudentViewModelFactory(
    private val repository: StudentRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StudentViewModel::class.java)){
            return StudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }

}