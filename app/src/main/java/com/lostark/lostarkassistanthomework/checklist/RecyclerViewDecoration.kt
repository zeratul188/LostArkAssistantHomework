package com.lostark.lostarkassistanthomework.checklist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDecoration: RecyclerView.ItemDecoration {
    var height: Int = 0
    var width: Int = 0

    constructor(width: Int, height: Int) : super() {
        this.height = height
        this.width = width
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = height
        outRect.right = width
    }
}