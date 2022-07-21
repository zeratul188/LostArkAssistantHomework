package com.lostark.lostarkassistanthomework.checklist.rooms

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Family::class], version = 1, exportSchema = false)
abstract class FamilyDatabase: RoomDatabase() {
    abstract fun familyDao(): FamilyDao

    companion object {
        private var instance: FamilyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FamilyDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, FamilyDatabase::class.java, "family").allowMainThreadQueries().build()
            }
            return instance
        }
    }
}