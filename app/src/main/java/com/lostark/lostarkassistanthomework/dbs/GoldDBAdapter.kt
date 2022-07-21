package com.lostark.lostarkassistanthomework.dbs

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.lostark.lostarkassistanthomework.checklist.objects.Gold
import com.lostark.lostarkassistanthomework.dbs.sys.LoadDBAdapter
import java.sql.SQLException

class GoldDBAdapter {
    val tag: String = "GoldDBAdapter"
    val table_name: String = "GOLDS"

    var context: Context
    lateinit var db: SQLiteDatabase
    var loadDBAdapter: LoadDBAdapter

    constructor(context: Context) {
        this.context = context
        loadDBAdapter = LoadDBAdapter("golds", context)
    }

    fun open(): GoldDBAdapter {
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

    fun getItems(): ArrayList<Gold> {
        val list: ArrayList<Gold> = ArrayList()
        try {
            val sql = "SELECT * FROM $table_name"

            val cursor: Cursor = db.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    var name: String = cursor.getString(1)
                    var min: Int = cursor.getInt(2)
                    var max: Int = cursor.getInt(3)
                    var position: Int = cursor.getInt(4)
                    var gold: Int = cursor.getInt(5)

                    val golds = Gold(name, min, max, position, gold)
                    list.add(golds)
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
    }
}