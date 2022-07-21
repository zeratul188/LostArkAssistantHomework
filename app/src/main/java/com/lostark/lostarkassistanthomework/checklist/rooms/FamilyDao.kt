package com.lostark.lostarkassistanthomework.checklist.rooms

import androidx.room.*

@Dao
interface FamilyDao {
    @Query("SELECT * FROM family")
    fun getAll(): List<Family>

    @Insert
    fun insertAll(vararg family: Family)

    @Delete
    fun delete(family: Family)

    @Update
    fun update(vararg family: Family)
}