package com.lostark.lostarkassistanthomework.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.lostark.lostarkassistanthomework.*
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.dbs.HomeworkDBAdapter
import com.lostark.lostarkassistanthomework.objects.Chracter

class AddActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var btnAdd: Button
    lateinit var toolBar: Toolbar
    lateinit var rgAuto: RadioGroup
    lateinit var layoutFrame: FrameLayout

    var chracters = ArrayList<Chracter>()
    val homeworkDBAdapter: HomeworkDBAdapter = HomeworkDBAdapter(App.context())
    lateinit var homeworkDB: HomeworkDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("캐릭터 추가")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        rgAuto = findViewById(R.id.rgAuto)
        layoutFrame = findViewById(R.id.layoutFrame)

        homeworkDB = HomeworkDatabase.getInstance(this)!!

        val autoFragment = AutoFragment()
        autoFragment.setEditName(edtName)
        val selfFragment = SelfFragment()

        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, autoFragment).commit()
        rgAuto.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.rdoAuto -> {
                    supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, autoFragment).commit()
                }
                R.id.rdoSelf -> {
                    supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment).commit()
                }
            }
        }

        btnAdd.setOnClickListener {
            when (rgAuto.checkedRadioButtonId) {
                R.id.rdoAuto -> {
                    var overlapCount = 0
                    chracters = autoFragment.chracters
                    var isNotChecked = true
                    run {
                        chracters.forEach { item ->
                            if (item.isChecked) {
                                isNotChecked = false
                                return@run
                            }
                        }
                    }
                    if (chracters.isEmpty() || isNotChecked) {
                        val customToast = CustomToast(this)
                        customToast.createToast("선택한 캐릭터가 없습니다.", false)
                        customToast.show()
                        return@setOnClickListener
                    }
                    var gold_count = 0
                    val lists = homeworkDB.homeworkDao().getAll()
                    lists.forEach { item ->
                        if (item.isGold) {
                            gold_count++
                        }
                    }
                    chracters.forEach { item ->
                        if (item.isChecked) {
                            var daylist = ""
                            var daynows = ""
                            var daymaxs = ""
                            var dayicons = ""
                            var dayends = ""
                            var weeklist = ""
                            var weeknows = ""
                            var weekmaxs = ""
                            var weekicons = ""
                            var weekends = ""
                            homeworkDBAdapter.open()
                            val savedFrameHomeworks = homeworkDBAdapter.getItems()
                            if (savedFrameHomeworks.isNotEmpty()) {
                                savedFrameHomeworks.forEach { frameHomework ->
                                    if (frameHomework.type == "일일") {
                                        if (item.level >= frameHomework.min) {
                                            if (daylist != "") daylist += ","
                                            if (daynows != "") daynows += ","
                                            if (daymaxs != "") daymaxs += ","
                                            if (dayicons != "") dayicons += ","
                                            if (dayends != "") dayends += ","
                                            daylist += frameHomework.name
                                            daynows += "0"
                                            daymaxs += frameHomework.max
                                            dayicons += frameHomework.icon
                                            dayends += frameHomework.end
                                        }
                                    } else {
                                        if (item.level >= frameHomework.min) {
                                            if (item.level >= 1490 && frameHomework.name == "비아키스") {
                                                return@forEach
                                            }
                                            if (item.level >= 1580 && frameHomework.name == "발탄") {
                                                return@forEach
                                            }
                                            if (weeklist != "") weeklist += ","
                                            if (weeknows != "") weeknows += ","
                                            if (weekmaxs != "") weekmaxs += ","
                                            if (weekicons != "") weekicons += ","
                                            if (weekends != "") weekends += ","
                                            weeklist += frameHomework.name
                                            weeknows += "0"
                                            var max = 0
                                            if (frameHomework.name == "아브렐슈드") {
                                                if (item.level >= 1520) {
                                                    max = 6
                                                } else if (item.level >= 1500) {
                                                    max = 4
                                                } else if (item.level >= 1490) {
                                                    max = 2
                                                }
                                                weekmaxs += max
                                            } else {
                                                weekmaxs += frameHomework.max
                                            }
                                            weekicons += frameHomework.icon
                                            weekends += frameHomework.end
                                        }
                                    }
                                }
                            }
                            homeworkDBAdapter.close()
                            val datas = homeworkDB.homeworkDao().getAll()
                            var isOverlap = false
                            datas.forEach { data ->
                                if (data.name == item.name) {
                                    isOverlap = true
                                }
                            }
                            if (!isOverlap) {
                                var isGold = false
                                if (gold_count < 6) {
                                    isGold = true
                                    gold_count++
                                }
                                val homework = Homework(0, item.name, item.level, item.server, item.job, daylist, daynows, daymaxs, dayicons, dayends, weeklist, weeknows, weekmaxs, weekicons, weekends, 0, 0, 0, 0, 0, 0, true, 9999, isGold)
                                homeworkDB.homeworkDao().insertAll(homework)
                            } else {
                                overlapCount++
                            }
                        }
                    }
                    if (overlapCount > 0) {
                        val toast = CustomToast(this)
                        toast.createToast("${overlapCount}개의 캐릭터가 이미 존재해서 중복된 캐릭터는 추가되지 않았습니다.", false)
                        toast.show()
                    }
                    finish()
                }
                R.id.rdoSelf -> {
                    if (edtName.text.toString() == "") {
                        val toast = CustomToast(App.context())
                        toast.createToast("캐릭터 이름을 입력해주세요.", false)
                        toast.show()
                        return@setOnClickListener
                    }
                    if (isOverlap(edtName.text.toString())) {
                        val toast = CustomToast(App.context())
                        toast.createToast("이미 ${edtName.text.toString()}의 캐릭터가 추가되어 있습니다.", false)
                        toast.show()
                        return@setOnClickListener
                    }
                    val preset = selfFragment.getPreset()
                    var daylist = ""
                    var daynows = ""
                    var daymaxs = ""
                    var dayicons = ""
                    var dayends = ""
                    var weeklist = ""
                    var weeknows = ""
                    var weekmaxs = ""
                    var weekicons = ""
                    var weekends = ""
                    homeworkDBAdapter.open()
                    val savedFrameHomeworks = homeworkDBAdapter.getItems()
                    if (!savedFrameHomeworks.isEmpty()) {
                        savedFrameHomeworks.forEach { frameHomework ->
                            if (frameHomework.type == "일일") {
                                if (preset.level >= frameHomework.min) {
                                    if (daylist != "") daylist += ","
                                    if (daynows != "") daynows += ","
                                    if (daymaxs != "") daymaxs += ","
                                    if (dayicons != "") dayicons += ","
                                    if (dayends != "") dayends += ","
                                    daylist += frameHomework.name
                                    daynows += "0"
                                    daymaxs += frameHomework.max
                                    dayicons += frameHomework.icon
                                    dayends += frameHomework.end
                                }
                            } else {
                                if (preset.level >= frameHomework.min) {
                                    if (preset.level >= 1490 && frameHomework.name == "비아키스") {
                                        return@forEach
                                    }
                                    if (preset.level >= 1580 && frameHomework.name == "발탄") {
                                        return@forEach
                                    }
                                    if (weeklist != "") weeklist += ","
                                    if (weeknows != "") weeknows += ","
                                    if (weekmaxs != "") weekmaxs += ","
                                    if (weekicons != "") weekicons += ","
                                    if (weekends != "") weekends += ","
                                    weeklist += frameHomework.name
                                    weeknows += "0"
                                    var max = 0
                                    if (frameHomework.name == "아브렐슈드") {
                                        if (preset.level >= 1520) {
                                            max = 6
                                        } else if (preset.level >= 1500) {
                                            max = 4
                                        } else if (preset.level >= 1490) {
                                            max = 2
                                        }
                                        weekmaxs += max
                                    } else {
                                        weekmaxs += frameHomework.max
                                    }
                                    weekicons += frameHomework.icon
                                    weekends += frameHomework.end
                                }
                            }
                        }
                    }
                    homeworkDBAdapter.close()
                    val datas = homeworkDB.homeworkDao().getAll()
                    var gold_count = 0
                    datas.forEach { data ->
                        if (data.isGold) {
                            gold_count++
                        }
                    }
                    var isGold = false
                    if (gold_count < 6) {
                        isGold = true
                    }
                    val homework = Homework(0, edtName.text.toString(), preset.level, preset.server, preset.job, daylist, daynows, daymaxs, dayicons, dayends, weeklist, weeknows, weekmaxs, weekicons, weekends, 0, 0, 0, 0, 0, 0, false, 9999, isGold)
                    homeworkDB.homeworkDao().insertAll(homework)
                    finish()
                }
            }
        }

    }

    fun isOverlap(name: String): Boolean {
        var isOverlap = false
        val datas = homeworkDB.homeworkDao().getAll()
        datas.forEach { data ->
            if (data.name == name) {
                isOverlap = true
            }
        }
        return isOverlap
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

