package com.lostark.lostarkassistanthomework.checklist.rooms

import androidx.room.*

@Dao
interface HomeworkDao {
    @Query("SELECT * FROM homework")
    fun getAll(): List<Homework>

    @Insert
    fun insertAll(vararg homework: Homework)

    @Delete
    fun delete(homework: Homework)

    @Update
    fun update(vararg homework: Homework)
}