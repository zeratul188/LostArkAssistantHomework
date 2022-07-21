package com.lostark.lostarkassistanthomework.dbs

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.lostark.lostarkassistanthomework.checklist.objects.FrameHomework
import com.lostark.lostarkassistanthomework.dbs.sys.LoadDBAdapter
import java.sql.SQLException

class HomeworkDBAdapter {
    val tag: String = "HomeworkDBAdapter"
    val table_name: String = "homeworks"

    var context: Context
    var loadDBAdapter: LoadDBAdapter
    lateinit var db: SQLiteDatabase

    constructor(context: Context) {
        this.context = context
        loadDBAdapter = LoadDBAdapter("chracter", context)
    }

    fun open(): HomeworkDBAdapter {
        try {
            loadDBAdapter.open()
            loadDBAdapter.close()
            db = loadDBAdapter.readableDatabase
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return this
    }

    fun close() {
        loadDBAdapter.close()
    }

    fun getItems(): ArrayList<FrameHomework> {
        val list: ArrayList<FrameHomework> = ArrayList()
        try {
            val sql = "SELECT * FROM ${table_name}"

            val cursor: Cursor = db.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var name: String = cursor.getString(1)
                    var icon: String = cursor.getString(2)
                    var max: Int = cursor.getInt(3)
                    var type: String = cursor.getString(4)
                    var end: String = cursor.getString(5)
                    var min: Int = cursor.getInt(6)

                    val frameHomework = FrameHomework(name, icon, max, type, end, min)
                    list.add(frameHomework)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
    }
}