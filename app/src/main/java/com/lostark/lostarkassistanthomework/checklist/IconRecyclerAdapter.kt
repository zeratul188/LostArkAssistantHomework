package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Icon

class IconRecyclerAdapter(
    private val items: ArrayList<Icon>,
    private val context: Context
) : RecyclerView.Adapter<IconRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {
            for (i in items.indices) {
                items[i].isChecked = i == position
            }
            notifyDataSetChanged()
        }
        holder.apply {
            bind(item, context, listener)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    fun getResult(): Icon {
        items.forEach { item ->
            if (item.isChecked) {
                return item
            }
        }
        return items[0]
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v
        lateinit var imgIcon: ImageView
        lateinit var layoutBackground: LinearLayout

        fun bind(item: Icon, context: Context, listener: View.OnClickListener) {
            imgIcon = view.findViewById(R.id.imgIcon)
            layoutBackground = view.findViewById(R.id.layoutBackground)

            imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            if (item.isChecked) {
                layoutBackground.setBackgroundResource(R.drawable.selected_item_style)
            } else {
                layoutBackground.setBackgroundResource(R.drawable.unselected_item_style)
            }

            imgIcon.setOnClickListener(listener)
        }
    }
}