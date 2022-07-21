package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase

class DayRecyclerAdapter(private val items : ArrayList<Family>, private val context: Context, private val db: FamilyDatabase) : RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_family_checklist, parent, false)
        return DayRecyclerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            //click event
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
            db.familyDao().update(item)
            notifyDataSetChanged()
        }
        holder.apply {
            bind(listener, item, context)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        lateinit var imgIcon: ImageView
        lateinit var txtName: TextView
        lateinit var txtCount: TextView
        lateinit var txtEnd: TextView
        lateinit var layoutBackground: FrameLayout
        lateinit var layoutComplete: LinearLayout
        fun bind(listener: View.OnClickListener, item: Family, context: Context) {
            imgIcon = view.findViewById(R.id.imgIcon)
            txtName = view.findViewById(R.id.txtName)
            txtCount = view.findViewById(R.id.txtCount)
            txtEnd = view.findViewById(R.id.txtEnd)
            layoutBackground = view.findViewById(R.id.layoutBackground)
            layoutComplete = view.findViewById(R.id.layoutComplete)

            imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            txtName.text = item.name
            txtCount.text = "${item.now}/${item.max}"
            txtEnd.text = item.end
            if (item.now >= item.max) {
                layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
                layoutComplete.visibility = View.VISIBLE
            } else {
                layoutBackground.setBackgroundResource(R.drawable.item_checklist_background)
                layoutComplete.visibility = View.GONE
            }

            view.setOnClickListener(listener)
        }
    }
}