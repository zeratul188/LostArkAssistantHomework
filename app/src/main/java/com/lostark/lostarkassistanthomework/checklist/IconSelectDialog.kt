package com.lostark.lostarkassistanthomework.checklist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Icon

class IconSelectDialog(private val context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    private lateinit var iconAdapter: IconRecyclerAdapter
    private val ICONS_LENGTH = 26

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun getResult(): Icon {
        return iconAdapter.getResult()
    }

    fun show(isCanceled: Boolean) {
        dialog.setContentView(R.layout.dialog_icon)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)

        val listIcons = dialog.findViewById<RecyclerView>(R.id.listIcons)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnApply = dialog.findViewById<Button>(R.id.btnApply)

        val items = ArrayList<Icon>()
        for (i in 1..ICONS_LENGTH) {
            if (i == 1) {
                items.add(Icon("hw${i}", true))
            } else {
                items.add(Icon("hw${i}", false))
            }
        }
        iconAdapter = IconRecyclerAdapter(items, context)
        listIcons.adapter = iconAdapter

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnApply.setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(isCanceled)
        dialog.setCancelable(isCanceled)
        dialog.show()
    }

    interface OnDialogClickListener {
        fun onClicked()
    }
}