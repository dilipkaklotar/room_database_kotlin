package com.dilip.room.database.model

import android.util.Patterns
import androidx.lifecycle.*
import com.dilip.room.database.Event
import com.dilip.room.database.db.Student
import com.dilip.room.database.db.StudentRepository
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var studentToUpdateOrDelete: Student

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val inputRollNo = MutableLiveData<String>()
    val inputMedium = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdateAndDelete(student: Student) {
        inputName.value = student.name
        inputEmail.value = student.email
        inputRollNo.value = student.roll_no
        inputMedium.value = student.medium

        isUpdateOrDelete = true
        studentToUpdateOrDelete = student
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessage.value = Event("Please enter student name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter student email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter a correct email address")
        } else if (inputRollNo.value == null) {
            statusMessage.value = Event("Please enter student rollno")
        } else if (inputMedium.value == null) {
            statusMessage.value = Event("Please enter student medium")
        } else {
            if (isUpdateOrDelete) {
                studentToUpdateOrDelete.name = inputName.value!!
                studentToUpdateOrDelete.email = inputEmail.value!!
                studentToUpdateOrDelete.roll_no = inputRollNo.value!!
                studentToUpdateOrDelete.medium = inputMedium.value!!

                updateSubscriber(studentToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                val roll_no = inputRollNo.value!!
                val medium = inputMedium.value!!
                insertStudent(Student(0, name, email, roll_no, medium))
                inputName.value = ""
                inputEmail.value = ""
                inputRollNo.value = ""
                inputMedium.value = ""
            }
        }
    }

    private fun insertStudent(student: Student) = viewModelScope.launch {
        val newRowId = repository.insert(student)
        if (newRowId > -1) {
            statusMessage.value = Event("Student Inserted Successfully $newRowId")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    fun getSavedStudent() = liveData {
        repository.student.collect {
            emit(it)
        }
    }


    private fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value = Event("$noOfRowsDeleted Student Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }


    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteSubscriber(studentToUpdateOrDelete)
        } else {
            clearAll()
        }
    }


    private fun deleteSubscriber(student: Student) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(student)
        if (noOfRowsDeleted > 0) {
            inputName.value = ""
            inputEmail.value = ""
            inputRollNo.value = ""
            inputMedium.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRowsDeleted Row Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun updateSubscriber(student: Student) = viewModelScope.launch {
        val noOfRows = repository.update(student)
        if (noOfRows > 0) {
            inputName.value = ""
            inputEmail.value = ""
            inputRollNo.value = ""
            inputMedium.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$noOfRows Row Updated Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }

    }

}