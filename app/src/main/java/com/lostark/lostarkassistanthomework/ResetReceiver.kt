package com.lostark.lostarkassistanthomework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import java.util.*
import kotlin.system.exitProcess

class ResetReceiver: BroadcastReceiver() {
    private val homeworkDB = HomeworkDatabase.getInstance(App.context())!!
    private val familyDB = FamilyDatabase.getInstance(App.context())!!

    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance()
        val week = calendar.get(Calendar.DAY_OF_WEEK)
        var isWeeked = false
        if (week == 4) {
            isWeeked = true
        }

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

        val packageManager = context?.packageManager
        val itt = context?.let { packageManager?.getLaunchIntentForPackage(it.packageName) }
        val componentName = itt?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context?.startActivity(mainIntent)
        exitProcess(0)

        val customToast = context?.let { CustomToast(it) }
        if (customToast != null) {
            if (isWeeked) {
                customToast.createToast("주간, 일일 체크리스트가 초기화되었습니다.", false)
            } else {
                customToast.createToast("일일 체크리스트가 초기화되었습니다.", false)
            }
            customToast.show()
        }
    }
}