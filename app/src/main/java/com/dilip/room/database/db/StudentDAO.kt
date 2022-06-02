package com.dilip.room.database.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDAO {

    @Insert
    suspend fun insertStudent(student: Student): Long

    @Update
    suspend fun updateStudent(student: Student): Int

    @Delete
    suspend fun deleteStudent(student: Student): Int

    @Query("DELETE FROM student_data_table")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM student_data_table")
    fun getAllStudent(): Flow<List<Student>>

}