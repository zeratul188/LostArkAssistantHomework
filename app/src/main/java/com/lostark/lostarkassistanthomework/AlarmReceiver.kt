package com.lostark.lostarkassistanthomework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class AlarmReceiver: BroadcastReceiver() {
    private val homeworkDB = HomeworkDatabase.getInstance(App.context())!!
    private val familyDB = FamilyDatabase.getInstance(App.context())!!

    override fun onReceive(p0: Context?, p1: Intent?) {
        var result = ""
        val familys = familyDB.familyDao().getAll()
        val homeworks = homeworkDB.homeworkDao().getAll()

        run {
            familys.forEach { family ->
                if (family.now < family.max) {
                    result += "원정대 숙제"
                    return@run
                }
            }
        }
        homeworks.forEach { homework ->
            val nows = homework.daynows.split(",")
            val maxs = homework.daymaxs.split(",")
            for (index in nows.indices) {
                if (nows[index] < maxs[index]) {
                    if (result != "") {
                        result += ","
                    }
                    result += homework.name
                    break;
                }
            }
        }

        if (result != "" && p0 != null) {
            createNotification(result, p0)
        }
    }

    private fun createNotification(msg: String, context: Context) {
        val builder = NotificationCompat.Builder(context, "default")

        builder.setSmallIcon(R.drawable.logo)
        builder.setContentTitle("숙제를 하지 않은 캐릭터가 있습니다.")
        builder.setContentText(msg)
        //builder.setColor(Color.RED)
        builder.setAutoCancel(true)

        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT))
        }

        manager.notify(1, builder.build())
    }
}