package com.lostark.lostarkassistanthomework.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework

class GoldRecylerAdapter(
    private val list: ArrayList<Homework>,
    private val context: Context,
    private val txtContent: TextView
) : RecyclerView.Adapter<GoldRecylerAdapter.ViewHolder>() {
    var goldcount = 0

    fun syncGold() {
        list.forEach { item ->
            if (item.isGold) {
                goldcount++
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chracter_list, parent, false)
        return ViewHolder(view, list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val listener = View.OnClickListener { it ->
            if (item.isGold) {
                item.isGold = false
                holder.chkCheck.isChecked = false
                goldcount--
                holder.layoutBackground.setBackgroundResource(R.drawable.unselected_style)
            } else {
                if (goldcount >= 6) {
                    val toast = CustomToast(context)
                    toast.createToast("이미 지정할 수 있는 횟수를 초과하였습니다.", false)
                    toast.show()
                    return@OnClickListener
                }
                item.isGold = true
                holder.chkCheck.isChecked = true
                goldcount++
                holder.layoutBackground.setBackgroundResource(R.drawable.selected_style)
            }
            txtContent.text = "최대 6개 캐릭터 중 ${goldcount}개 캐릭터 지정됨"
        }
        holder.apply {
            bind(listener, item, context)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(v: View, private val list: ArrayList<Homework>): RecyclerView.ViewHolder(v) {
        private var view: View = v
        lateinit var imgJob: ImageView
        lateinit var txtName: TextView
        lateinit var txtServer: TextView
        lateinit var txtLevel: TextView
        lateinit var txtJob: TextView
        lateinit var chkCheck: CheckBox
        lateinit var layoutBackground: ConstraintLayout

        fun bind(listener: View.OnClickListener, item: Homework, context: Context) {
            imgJob = view.findViewById(R.id.imgJob)
            txtName = view.findViewById(R.id.txtName)
            txtServer = view.findViewById(R.id.txtServer)
            txtLevel = view.findViewById(R.id.txtLevel)
            txtJob = view.findViewById(R.id.txtJob)
            chkCheck = view.findViewById(R.id.chkCheck)
            layoutBackground = view.findViewById(R.id.layoutBackground)

            val jobs = context.resources.getStringArray(R.array.job)
            val pos = jobs.indexOf(item.job)+1;
            imgJob.setImageResource(context.resources.getIdentifier("jbi${pos}", "drawable", context.packageName))
            txtName.text = item.name
            txtServer.text = item.server
            txtLevel.text = "Lv.${item.level.toString()}"
            txtJob.text = item.job
            chkCheck.isChecked = item.isGold
            if (chkCheck.isChecked) {
                layoutBackground.setBackgroundResource(R.drawable.selected_style)
            } else {
                layoutBackground.setBackgroundResource(R.drawable.unselected_style)
            }

            chkCheck.setOnClickListener(listener)
            view.setOnClickListener(listener)
        }
    }
}