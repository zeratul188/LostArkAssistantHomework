package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Checklist
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class HomeworkRecylerAdapter(
    private val items: ArrayList<Checklist>,
    private val context: Context,
    private val homework: Homework,
    private val db: HomeworkDatabase,
    private val type: String,
    private val old_holder: ChracterRecylerAdapter.ViewHolder,
    private val fragment: ChecklistFragment
) : RecyclerView.Adapter<HomeworkRecylerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeworkRecylerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_checklist, parent, false)
        return HomeworkRecylerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeworkRecylerAdapter.ViewHolder, position: Int) {
        val item = items[position]
        val longListener = View.OnLongClickListener { it ->
            val diff = item.max - item.now
            item.now = item.max
            holder.txtCount.text = "${item.now}/${item.max}"
            holder.layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
            holder.layoutComplete.visibility = View.VISIBLE

            for (i in 0 until diff) {
                when (item.name) {
                    "카오스 던전" -> {
                        if (item.now != 0) {
                            if (homework.dungeonrest >= 20) {
                                homework.dungeonrest -= 20
                                homework.dungeonlost += 20
                            }
                        } else {
                            homework.dungeonrest += homework.dungeonlost
                            homework.dungeonlost = 0
                        }
                    }
                    "가디언 토벌" -> {
                        if (item.now != 0) {
                            if (homework.bossrest >= 20) {
                                homework.bossrest -= 20
                                homework.bosslost += 20
                            }
                        } else {
                            homework.bossrest += homework.bosslost
                            homework.bosslost = 0
                        }
                    }
                    "에포나 의뢰" -> {
                        if (item.now != 0) {
                            if (homework.questrest >= 20) {
                                homework.questrest -= 20
                                homework.questlost += 20
                            }
                        } else {
                            homework.questrest += homework.questlost
                            homework.questlost = 0
                        }
                    }
                }
            }

            var names = ""
            var nows = ""
            var maxs = ""
            var icons = ""
            var ends = ""
            items.forEach { item ->
                if (names != "") names += ","
                if (nows != "") nows += ","
                if (maxs != "") maxs += ","
                if (icons != "") icons += ","
                if (ends != "") ends += ","
                names += item.name
                nows += item.now.toString()
                maxs += item.max.toString()
                icons += item.icon
                ends += item.end
            }
            if (type == "일일") {
                homework.daylist = names
                homework.daynows = nows
                homework.daymaxs = maxs
                homework.dayicons = icons
                homework.dayends = ends
            } else {
                homework.weeklist = names
                homework.weeknows = nows
                homework.weekmaxs = maxs
                homework.weekicons = icons
                homework.weekends = ends
            }
            db.homeworkDao().update(homework)
            if (type == "일일") {
                var progress = 0
                var max = 0
                items.forEach { item ->
                    progress += item.now
                    max += item.max
                }
                old_holder.syncProgress(progress, max)
            }
            fragment.syncProgress()
            notifyDataSetChanged()
            return@OnLongClickListener true
        }
        val listener = View.OnClickListener { it ->
            if (item.now < item.max) {
                item.now++;
            } else {
                item.now = 0
            }
            holder.txtCount.text = "${item.now}/${item.max}"
            if (item.now >= item.max) {
                holder.layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
                holder.layoutComplete.visibility = View.VISIBLE
            } else {
                holder.layoutBackground.setBackgroundResource(R.drawable.item_checklist_background)
                holder.layoutComplete.visibility = View.GONE
            }

            
            
            when (item.name) {
                "카오스 던전" -> {
                    if (item.now != 0) {
                        if (homework.dungeonrest >= 20) {
                            homework.dungeonrest -= 20
                            homework.dungeonlost += 20
                        }
                    } else {
                        homework.dungeonrest += homework.dungeonlost
                        homework.dungeonlost = 0
                    }
                }
                "가디언 토벌" -> {
                    if (item.now != 0) {
                        if (homework.bossrest >= 20) {
                            homework.bossrest -= 20
                            homework.bosslost += 20
                        }
                    } else {
                        homework.bossrest += homework.bosslost
                        homework.bosslost = 0
                    }
                }
                "에포나 의뢰" -> {
                    if (item.now != 0) {
                        if (homework.questrest >= 20) {
                            homework.questrest -= 20
                            homework.questlost += 20
                        }
                    } else {
                        homework.questrest += homework.questlost
                        homework.questlost = 0
                    }
                }
            }
            
            var names = ""
            var nows = ""
            var maxs = ""
            var icons = ""
            var ends = ""
            items.forEach { item -> 
                if (names != "") names += "," 
                if (nows != "") nows += ","
                if (maxs != "") maxs += ","
                if (icons != "") icons += ","
                if (ends != "") ends += ","
                names += item.name
                nows += item.now.toString()
                maxs += item.max.toString()
                icons += item.icon
                ends += item.end
            }
            if (type == "일일") {
                homework.daylist = names
                homework.daynows = nows
                homework.daymaxs = maxs
                homework.dayicons = icons
                homework.dayends = ends
            } else {
                homework.weeklist = names
                homework.weeknows = nows
                homework.weekmaxs = maxs
                homework.weekicons = icons
                homework.weekends = ends
            }
            db.homeworkDao().update(homework)
            if (type == "일일") {
                var progress = 0
                var max = 0
                items.forEach { item ->
                    progress += item.now
                    max += item.max
                }
                old_holder.syncProgress(progress, max)
            }
            fragment.syncProgress()
            notifyDataSetChanged()
        }
        holder.apply {
            bind(longListener, listener, item, context, homework)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view = v
        lateinit var imgIcon: ImageView
        lateinit var progressRest: ProgressBar
        lateinit var txtName: TextView
        lateinit var txtCount: TextView
        lateinit var txtEnd: TextView
        lateinit var layoutBackground: ConstraintLayout
        lateinit var layoutComplete: LinearLayout
        fun bind(longListener: View.OnLongClickListener, listener: View.OnClickListener, item: Checklist, context: Context, homework: Homework) {
            imgIcon = view.findViewById(R.id.imgIcon)
            progressRest = view.findViewById(R.id.progressRest)
            txtName = view.findViewById(R.id.txtName)
            txtCount = view.findViewById(R.id.txtCount)
            txtEnd = view.findViewById(R.id.txtEnd)
            layoutBackground = view.findViewById(R.id.layoutBackground)
            layoutComplete = view.findViewById(R.id.layoutComplete)
            
            imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            txtName.text = item.name
            txtCount.text = "${item.now.toString()}/${item.max.toString()}"
            txtEnd.text = item.end
            if (item.now >= item.max) {
                layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
                layoutComplete.visibility = View.VISIBLE
            } else {
                layoutBackground.setBackgroundResource(R.drawable.item_checklist_background)
                layoutComplete.visibility = View.GONE
            }

            when (item.name) {
                "카오스 던전" -> {
                    progressRest.visibility = View.VISIBLE
                    progressRest.max = 100
                    progressRest.progress = homework.dungeonrest
                }
                "가디언 토벌" -> {
                    progressRest.visibility = View.VISIBLE
                    progressRest.max = 100
                    progressRest.progress = homework.bossrest
                }
                "에포나 의뢰" -> {
                    progressRest.visibility = View.VISIBLE
                    progressRest.max = 100
                    progressRest.progress = homework.questrest
                }
                else -> {
                    progressRest.visibility = View.GONE
                }
            }

            view.setOnClickListener(listener)
            view.setOnLongClickListener(longListener)
        }
    }
}