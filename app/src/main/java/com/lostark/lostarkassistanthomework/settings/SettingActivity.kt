package com.lostark.lostarkassistanthomework.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.lostark.lostarkassistanthomework.*
import com.lostark.lostarkassistanthomework.changeposition.ChangePositionActivity
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.dbs.FamilyDBAdapter
import java.util.*
import kotlin.collections.ArrayList

class SettingActivity : AppCompatActivity() {
    lateinit var swtShowGold: Switch
    lateinit var sprDayWeek: Spinner

    lateinit var btnDelete: Button
    lateinit var btnFamilyInit: Button
    lateinit var btnReset: Button
    lateinit var btnChangePosition: Button
    lateinit var btnMoney: Button

    lateinit var swtAlarm: Switch
    lateinit var sprAlarmTime: Spinner

    lateinit var sprTheme: Spinner
    lateinit var txtVersion: TextView
    lateinit var swtUpdate: Switch
    lateinit var btnUpdate: Button

    lateinit var toolBar: Toolbar

    lateinit var homeworkDB: HomeworkDatabase
    lateinit var familyDB: FamilyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("설정")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        homeworkDB = HomeworkDatabase.getInstance(App.context())!!
        familyDB = FamilyDatabase.getInstance(App.context())!!

        btnDelete = findViewById(R.id.btnDelete)
        btnFamilyInit = findViewById(R.id.btnFamilyInit)
        btnReset = findViewById(R.id.btnReset)
        btnChangePosition = findViewById(R.id.btnChangePosition)
        sprTheme = findViewById(R.id.sprTheme)
        txtVersion = findViewById(R.id.txtVersion)
        swtAlarm = findViewById(R.id.swtAlarm)
        sprAlarmTime = findViewById(R.id.sprAlarmTime)
        btnMoney = findViewById(R.id.btnMoney)
        swtUpdate = findViewById(R.id.swtUpdate)
        btnUpdate = findViewById(R.id.btnUpdate)
        swtShowGold = findViewById(R.id.swtShowGold)
        sprDayWeek = findViewById(R.id.sprDayWeek)

        val times = ArrayList<String>()
        for (i in 1..24) {
            times.add("${i}시")
        }
        val timeAdapter = ArrayAdapter(this, R.layout.txt_item_job, times)
        sprAlarmTime.adapter = timeAdapter
        val time = App.prefs.getInt("alarm_time", 21)
        sprAlarmTime.setSelection(time-1)
        val isAlarm = App.prefs.isBoolean("isalarm", false)
        swtAlarm.isChecked = isAlarm

        val dayorweek = ArrayList<String>()
        dayorweek.add("일일")
        dayorweek.add("주간")
        val dayweekAdapter = ArrayAdapter(this, R.layout.txt_item_job, dayorweek)
        sprDayWeek.adapter = dayweekAdapter
        val dw = App.prefs.getInt("dayorweek", 0)
        sprDayWeek.setSelection(dw)
        sprDayWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                App.prefs.setInt("dayorweek", position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        sprAlarmTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                App.prefs.setInt("alarm_time", p2+1)
                val manager = getSystemService(Context.ALARM_SERVICE) as? AlarmManager
                if (manager != null && swtAlarm.isChecked) {
                    cancelManager(manager)
                    alarmManager(manager, sprAlarmTime.selectedItemPosition+1)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        swtShowGold.isChecked = App.prefs.isBoolean("showgold", true)
        swtShowGold.setOnClickListener {
            App.prefs.setBoolean("showgold", swtShowGold.isChecked)
        }

        swtAlarm.setOnClickListener {
            val manager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            if (manager != null) {
                if (swtAlarm.isChecked) {
                    alarmManager(manager, sprAlarmTime.selectedItemPosition+1)
                } else {
                    cancelManager(manager)
                }
                App.prefs.setBoolean("isalarm", swtAlarm.isChecked)
            }
        }

        swtUpdate.isChecked = App.prefs.isBoolean("check_update", true)
        swtUpdate.setOnClickListener {
            App.prefs.setBoolean("check_update", swtUpdate.isChecked)
        }
        btnUpdate.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.lostark.lostarkassistanthomework")
            startActivity(intent)
        }
        txtVersion.text = BuildConfig.VERSION_NAME
        val modes = resources.getStringArray(R.array.darkmode)
        val modeAdapter = ArrayAdapter(this, R.layout.txt_item_job, modes)
        sprTheme.adapter = modeAdapter
        val mode = App.prefs.getInt("darkmode", 2)
        sprTheme.setSelection(mode)
        when (mode) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
            }
        }
        sprTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    2 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                        }
                    }
                }
                App.prefs.setInt("darkmode", position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btnChangePosition.setOnClickListener {
            val intent = Intent(this, ChangePositionActivity::class.java)
            startActivity(intent)
        }

        btnMoney.setOnClickListener {
            val intent = Intent(this, MoneyActivity::class.java)
            startActivity(intent)
        }

        btnFamilyInit.setOnClickListener {
            val checkDialog = CheckDialog(this)
            checkDialog.setData("원정대 체크리스트를 초기값으로 설정하시겠습니까?", "초기화", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {
                    val familyDB = FamilyDatabase.getInstance(App.context())!!
                    val familys: ArrayList<Family> = familyDB.familyDao().getAll() as ArrayList<Family>
                    familys.forEach { family ->
                        familyDB.familyDao().delete(family)
                    }
                    familys.clear()
                    val familyDBAdapter = FamilyDBAdapter(App.context())
                    familyDBAdapter.open()
                    val days = familyDBAdapter.getItems("일일")
                    val weeks = familyDBAdapter.getItems("주간")
                    days.forEach { day ->
                        familyDB.familyDao().insertAll(day)
                        familys.add(day)
                    }
                    weeks.forEach { week ->
                        familyDB.familyDao().insertAll(week)
                        familys.add(week)
                    }
                    familyDBAdapter.close()
                    val customToast = CustomToast(App.context())
                    customToast.createToast("원정대 체크리스트를 초기값으로 설정되었습니다.", false)
                    customToast.show()
                }
            })
            checkDialog.show(true)
        }

        btnReset.setOnClickListener {
            val resetDialog = ResetDialog(this)
            resetDialog.setData()
            resetDialog.setOnClickListener(object : ResetDialog.OnDialogClickListener {
                override fun onCLicked() {
                    reset(resetDialog.chkWeek.isChecked)
                    val customToast = CustomToast(App.context())
                    customToast.createToast("체크리스트가 초기화되었습니다.", false)
                    customToast.show()
                }
            })
            resetDialog.show(true)
        }

        btnDelete.setOnClickListener {
            val checkDialog = CheckDialog(this)
            checkDialog.setData("모든 캐릭터 데이터가 삭제됩니다. 정말로 삭제하시겠습니까?", "삭제", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {
                    var list = homeworkDB.homeworkDao().getAll()
                    list.forEach { item ->
                        homeworkDB.homeworkDao().delete(item)
                    }
                    val customToast = CustomToast(App.context())
                    customToast.createToast("모든 캐릭터 데이터가 삭제되었습니다.", false)
                    customToast.show()
                }
            })
            checkDialog.show(true)
        }
    }

    fun reset(isWeeked: Boolean) {
        var list = homeworkDB.homeworkDao().getAll()
        list.forEach { item ->
            val days = item.daylist.split(",")
            val daymaxs = item.daymaxs.split(",")
            val daynows = item.daynows.split(",")
            var daynowstr = ""
            for (i in days.indices) {
                when (days[i]) {
                    "카오스 던전" -> {
                        var diff = daymaxs[i].toInt() - daynows[i].toInt()
                        if (diff < 0) {
                            diff = 0
                        }
                        diff *= 10
                        item.dungeonrest += diff
                        if (item.dungeonrest > 100) {
                            item.dungeonrest = 100
                        }
                    }
                    "가디언 토벌" -> {
                        var diff = daymaxs[i].toInt() - daynows[i].toInt()
                        if (diff < 0) {
                            diff = 0
                        }
                        diff *= 10
                        item.bossrest += diff
                        if (item.bossrest > 100) {
                            item.bossrest = 100
                        }
                    }
                    "에포나 의뢰" -> {
                        var diff = daymaxs[i].toInt() - daynows[i].toInt()
                        if (diff < 0) {
                            diff = 0
                        }
                        diff *= 10
                        item.questrest += diff
                        if (item.questrest > 100) {
                            item.questrest = 100
                        }
                    }
                }
                if (daynowstr != "") daynowstr += ","
                daynowstr += "0"
            }
            if (isWeeked) {
                val weeklength = item.weeknows.split(",").size
                var weeknowstr = ""
                for (i in 0 until weeklength) {
                    if (weeknowstr != "") weeknowstr += ","
                    weeknowstr += "0"
                }
                item.weeknows = weeknowstr
            }
            item.daynows = daynowstr
            item.dungeonlost = 0
            item.bosslost = 0
            item.questlost = 0
            homeworkDB.homeworkDao().update(item)
        }

        val familys = familyDB.familyDao().getAll()
        familys.forEach { family ->
            if (family.type == "주간") {
                if (isWeeked) {
                    family.now = 0
                    familyDB.familyDao().update(family)
                }
            } else {
                family.now = 0
                familyDB.familyDao().update(family)
            }
        }
    }

    private fun alarmManager(manager: AlarmManager, time: Int) {
        val intent = Intent(this, AlarmReceiver::class.java)
        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.HOUR_OF_DAY) >= time) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, time)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        println("day : ${calendar.get(Calendar.DAY_OF_MONTH)}, Time : ${calendar.get(Calendar.HOUR_OF_DAY)}")

        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 24*60*60*1000, pIntent) // AlarmManager.INTERVAL_DAY
        }
    }

    private fun cancelManager(manager: AlarmManager) {
        val intent = Intent(this, ResetReceiver::class.java)
        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        manager?.cancel(pIntent)
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