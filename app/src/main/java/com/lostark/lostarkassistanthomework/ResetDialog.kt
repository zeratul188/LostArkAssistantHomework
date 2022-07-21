package com.lostark.lostarkassistanthomework

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox

class ResetDialog(private val context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    lateinit var chkWeek: CheckBox

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    fun setData() {
        dialog.setContentView(R.layout.dialog_reset)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)

        chkWeek = dialog.findViewById(R.id.chkWeek)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnOK = dialog.findViewById<Button>(R.id.btnOK)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnOK.setOnClickListener {
            onClickListener.onCLicked()
            dialog.dismiss()
        }
    }

    fun show(isCanceled: Boolean) {
        dialog.setCanceledOnTouchOutside(isCanceled)
        dialog.setCancelable(isCanceled)
        dialog.show()
    }

    interface OnDialogClickListener {
        fun onCLicked()
    }
}