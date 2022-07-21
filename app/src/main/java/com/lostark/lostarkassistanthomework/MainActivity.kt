package com.lostark.lostarkassistanthomework

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.checklist.ChecklistFragment
import com.lostark.lostarkassistanthomework.gold.GoldFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var toolBar : Toolbar
    lateinit var layoutFrame : FrameLayout
    lateinit var bottomNavigationView : BottomNavigationView

    private val checklistFragment = ChecklistFragment()
    private val goldFragment = GoldFragment()

    private var backkeyPressedTime: Long = 0
    private lateinit var customToast: CustomToast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("LAA 체크리스트")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)

        customToast = CustomToast(this)

        layoutFrame = findViewById(R.id.layoutFrame)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, checklistFragment).commit()
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_checklist -> supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, checklistFragment).commit()
                R.id.action_gold -> supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, goldFragment).commit()
            }
            return@setOnItemSelectedListener true
        }

        val manager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (manager != null) {
            //cancelManager(manager)
            resetManager(manager)
            val time = App.prefs.getInt("alarm_time", 21)
            val isAlarm = App.prefs.isBoolean("isalarm", false)
            if (isAlarm) {
                alarmManager(manager, time)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checklistFragment.resume()
        val mode = App.prefs.getInt("darkmode", 2)
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.action_refresh -> {
                checklistFragment.syncData()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun resetManager(manager: AlarmManager) {
        val intent = Intent(this, ResetReceiver::class.java)
        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 6) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (manager != null) {
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pIntent)
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
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pIntent)
        }
    }

    fun cancelManager(manager: AlarmManager) {
        val intent = Intent(this, ResetReceiver::class.java)
        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        manager?.cancel(pIntent)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if (System.currentTimeMillis() > backkeyPressedTime+2000) {
            backkeyPressedTime = System.currentTimeMillis()
            customToast.createToast("\'뒤로\' 버튼을 한번 더 누르면 앱이 종료됩니다.", false)
            customToast.show()
        } else {
            finish()
        }
    }
}