package com.lostark.lostarkassistanthomework.changeposition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.edit.EditItemTouchHelperCallback
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class ChangePositionActivity : AppCompatActivity(), ChangePositionAdapter.OnStartDragListener {
    lateinit var toolBar: Toolbar
    lateinit var listView: RecyclerView
    lateinit var btnApply: Button
    lateinit var chkLevel: CheckBox

    private var homeworks = ArrayList<Homework>()
    private lateinit var helper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_position)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("캐릭터 순서 변경")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.listView)
        btnApply = findViewById(R.id.btnApply)
        chkLevel = findViewById(R.id.chkLevel)

        val homeworkDB = HomeworkDatabase.getInstance(this)!!
        homeworks = homeworkDB.homeworkDao().getAll() as ArrayList<Homework>
        homeworks.sort()
        val adapter = ChangePositionAdapter(homeworks, this, this)
        val callback = EditItemTouchHelperCallback()
        callback.setOnItemMoveListener(adapter)
        helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(listView)
        listView.adapter = adapter

        val isLevel = App.prefs.isBoolean("change", true)
        chkLevel.isChecked = isLevel
        if (chkLevel.isChecked) {
            listView.visibility = View.INVISIBLE
        } else {
            listView.visibility = View.VISIBLE
        }

        chkLevel.setOnClickListener {
            if (chkLevel.isChecked) {
                listView.visibility = View.INVISIBLE
            } else {
                listView.visibility = View.VISIBLE
            }
        }

        btnApply.setOnClickListener {
            val customToast = CustomToast(this)
            if (chkLevel.isChecked) {
                App.prefs.setBoolean("change", true)
                customToast.createToast("레벨 순으로 정렬하였습니다.", false)
            } else {
                for (i in homeworks.indices) {
                    homeworks[i].position = i
                    homeworkDB.homeworkDao().update(homeworks[i])
                }
                customToast.createToast("캐릭터들을 정렬하였습니다.", false)
                App.prefs.setBoolean("change", false)
            }
            customToast.show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStartDrag(holder: ChangePositionAdapter.ViewHolder) {
        helper.startDrag(holder)
    }
}