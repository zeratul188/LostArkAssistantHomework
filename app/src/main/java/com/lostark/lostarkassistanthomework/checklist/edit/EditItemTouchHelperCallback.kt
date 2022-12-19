package com.lostark.lostarkassistanthomework.checklist.edit

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class EditItemTouchHelperCallback : ItemTouchHelper.Callback() {
    lateinit var itemMoveListener: OnItemMoveListener

    fun setOnItemMoveListener(listener: OnItemMoveListener) {
        itemMoveListener = listener
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        itemMoveListener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    interface OnItemMoveListener {
        fun onItemMove(fromPosition: Int, toPosition: Int)
    }
}