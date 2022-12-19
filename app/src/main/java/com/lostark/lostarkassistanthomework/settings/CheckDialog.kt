package com.lostark.lostarkassistanthomework.settings

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.lostark.lostarkassistanthomework.R

class CheckDialog(private val context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun setData(content: String, btnContent: String, isImportant: Boolean) {
        dialog.setContentView(R.layout.dialog_yesorno)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)

        val txtContent = dialog.findViewById<TextView>(R.id.txtContent)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnOK = dialog.findViewById<Button>(R.id.btnOK)

        txtContent.text = content
        btnOK.text = btnContent

        if (isImportant) {
            btnOK.setTextColor(context.resources.getColor(R.color.important))
        } else {
            btnOK.setTextColor(context.resources.getColor(R.color.main_font))
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnOK.setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }
    }

    fun show(isCanceled: Boolean) {
        dialog.setCanceledOnTouchOutside(isCanceled)
        dialog.setCancelable(isCanceled)
        dialog.show()
    }

    interface OnDialogClickListener {
        fun onClicked()
    }
}