package com.lostark.lostarkassistanthomework.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.RecyclerViewDecoration
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class MoneyActivity : AppCompatActivity() {
    lateinit var txtContent: TextView
    lateinit var listView: RecyclerView
    lateinit var btnApply: Button
    lateinit var toolBar: Toolbar

    private var homeworks = ArrayList<Homework>()
    private lateinit var goldRecylerAdapter: GoldRecylerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("골드 획득 캐릭터 지정")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txtContent = findViewById(R.id.txtContent)
        listView = findViewById(R.id.listView)
        btnApply = findViewById(R.id.btnApply)

        val homeworkDB = HomeworkDatabase.getInstance(this)!!
        homeworks = homeworkDB.homeworkDao().getAll() as ArrayList<Homework>
        homeworks.sort()
        goldRecylerAdapter = GoldRecylerAdapter(homeworks, this, txtContent)
        goldRecylerAdapter.syncGold()
        listView.adapter = goldRecylerAdapter
        listView.addItemDecoration(RecyclerViewDecoration(0, 10))

        var count = 0
        homeworks.forEach { homework ->
            if (homework.isGold) {
                count++
            }
        }
        txtContent.text = "최대 6개 캐릭터 중 ${count}개 캐릭터 지정됨"

        btnApply.setOnClickListener {
            homeworks.forEach { homework ->
                homeworkDB.homeworkDao().update(homework)
            }
            val toast = CustomToast(this)
            toast.createToast("골드 지정 캐릭터를 설정하였습니다.", false)
            toast.show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}