package com.lostark.lostarkassistanthomework.changeposition

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.edit.EditItemTouchHelperCallback
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import java.util.*
import kotlin.collections.ArrayList

class ChangePositionAdapter(
    private val items: ArrayList<Homework>,
    private val context: Context,
    private val startDragListener: OnStartDragListener
): RecyclerView.Adapter<ChangePositionAdapter.ViewHolder>(), EditItemTouchHelperCallback.OnItemMoveListener {
    
    interface OnStartDragListener {
        fun onStartDrag(holder: ViewHolder)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_position, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnTouchListener { v, event -> 
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                startDragListener.onStartDrag(holder)
            }
            return@OnTouchListener false
        }
        holder.apply { 
            bind(item, context, listener)
            itemView.tag = item
            
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
    
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v
         val imgJob: ImageView = view.findViewById(R.id.imgJob)
         val txtServer: TextView = view.findViewById(R.id.txtServer)
         val txtName: TextView = view.findViewById(R.id.txtName)
         val txtLevel: TextView = view.findViewById(R.id.txtLevel)
         val txtJob: TextView = view.findViewById(R.id.txtJob)
         val imgHandle: ImageView = view.findViewById(R.id.imgHandle)

        fun bind(item: Homework, context: Context, listener: View.OnTouchListener) {
            val jobs = context.resources.getStringArray(R.array.job)
            val pos = jobs.indexOf(item.job)+1;
            imgJob.setImageResource(context.resources.getIdentifier("jbi${pos}", "drawable", context.packageName))
            txtName.text = item.name
            txtServer.text = item.server
            txtLevel.text = item.level.toString()
            txtJob.text = item.job
            
            imgHandle.setOnTouchListener(listener)
        }
    }
}