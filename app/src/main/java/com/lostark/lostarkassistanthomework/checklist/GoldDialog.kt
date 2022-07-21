package com.lostark.lostarkassistanthomework.checklist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.GoldList
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.dbs.GoldDBAdapter

class GoldDialog(
    private val context: Context
) {
    private val dialog = Dialog(context)

    fun show(isCanceled: Boolean) {
        dialog.setContentView(R.layout.dialog_gold_list)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)

        val listView = dialog.findViewById<RecyclerView>(R.id.listView)
        val items = ArrayList<GoldList>()

        val homeworkDB = HomeworkDatabase.getInstance(context)!!
        val homeworks = homeworkDB.homeworkDao().getAll() as ArrayList
        val goldDBAdapter = GoldDBAdapter(context)

        goldDBAdapter.open()
        val golds = goldDBAdapter.getItems()
        goldDBAdapter.close()
        homeworks.forEach { homework ->
            if (homework.isGold) {
                val names = homework.weeklist.split(",")
                val nows = homework.weeknows.split(",")
                var money = 0
                var result = ""
                for (i in names.indices) {
                    golds.forEach { gold ->
                        if (gold.name == names[i]) {
                            if (homework.level >= gold.min && homework.level < gold.max && nows[i].toInt() >= gold.position) {
                                if (result != "") {
                                    result += "\n"
                                }
                                result += "${names[i]}(${gold.position}관문) : ${gold.gold} 골드"
                                money += gold.gold
                            }
                        }
                    }
                }
                items.add(GoldList(homework.name, homework.level, homework.server, homework.job, result, money))
            }
        }

        val goldAdapter = GoldListAdapter(items, context)
        listView.adapter = goldAdapter
        //listView.addItemDecoration(RecyclerViewDecoration(0, 10))

        dialog.setCanceledOnTouchOutside(isCanceled)
        dialog.setCancelable(isCanceled)
        dialog.show()
    }
}