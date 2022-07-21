package com.lostark.lostarkassistanthomework.checklist.rooms

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Homework::class], version = 1, exportSchema = false)
abstract class HomeworkDatabase: RoomDatabase() {
    abstract fun homeworkDao(): HomeworkDao

    companion object {
        private var instance: HomeworkDatabase? = null

        @Synchronized
        fun getInstance(context: Context): HomeworkDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, HomeworkDatabase::class.java, "homework").allowMainThreadQueries().build()
            }
            return instance
        }
    }
}