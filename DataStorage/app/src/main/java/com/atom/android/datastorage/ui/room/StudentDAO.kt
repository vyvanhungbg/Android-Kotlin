package com.atom.android.datastorage.ui.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDAO {
    @Query("SELECT * FROM student")
    fun getAll(): List<Student>

    @Query("SELECT * FROM student where id = (:id)")
    fun getById(id:Int): Student

    @Insert
    fun insertAll(vararg student: Student)

    @Query("DELETE FROM student where id = (:id)")
    fun delete(id: Int)
}