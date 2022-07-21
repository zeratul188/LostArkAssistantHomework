package com.lostark.lostarkassistanthomework.dbs

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.dbs.sys.LoadDBAdapter
import java.sql.SQLException

class FamilyDBAdapter {
    val tag: String = "FamilyDBAdapter"
    val table_name: String = "FAMILY"

    var context: Context
    lateinit var db: SQLiteDatabase
    var loadDBAdapter: LoadDBAdapter

    constructor(context: Context) {
        this.context = context
        loadDBAdapter = LoadDBAdapter("family", context)
    }

    fun open(): FamilyDBAdapter {
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

    fun getItems(type: String): ArrayList<Family> {
        val list: ArrayList<Family> = ArrayList()
        try {
            val sql: String = "SELECT * FROM $table_name"

            val cursor: Cursor = db.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(5) == type) {
                        var name: String = cursor.getString(1)
                        var max: Int = cursor.getInt(2)
                        var end: String = cursor.getString(3)
                        var icon_str: String = cursor.getString(4)
                        var icon: String = icon_str

                        val checklist = Family(0, name, icon, 0, max, end, 0, type)
                        list.add(checklist)
                    }

                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
    }
}