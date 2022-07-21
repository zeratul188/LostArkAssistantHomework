package com.lostark.lostarkassistanthomework.checklist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.objects.EditData

class AddDialog(context: Context, type: String) : DialogFragment(){
    private lateinit var onClickListener: OnDialogClickListener

    private val selfFragment = AddSelfHomeworkFragment()
    private val presetFragment = AddPresetHomeworkFragment(type)

    lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*activity?.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)*/
        //activity?.window!!.setGravity(Gravity.CENTER)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view : View = inflater.inflate(R.layout.dialog_add_homework, container, false)

        navigationView = view.findViewById(R.id.bottomNavigationView)
        val layoutFrame = view.findViewById<FrameLayout>(R.id.layoutFrame)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)

        AddDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment)?.commit()

        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_self -> AddDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment).commit()
                R.id.action_preset -> AddDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, presetFragment).commit()
            }
            return@setOnItemSelectedListener true
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnAdd.setOnClickListener {
            onClickListener.onClicked()
        }

        return view
    }

    fun dialogDismiss() = dismiss()

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }
/*
    private val dialog = Dialog(context)



    fun show(isCanceled: Boolean) {
        dialog.setContentView(R.layout.dialog_add_homework)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)

        navigationView = dialog.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val layoutFrame = dialog.findViewById<FrameLayout>(R.id.layoutFrame)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAdd)

        fm.beginTransaction().replace(layoutFrame.id, selfFragment).commit()

        navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_self -> fm.beginTransaction().replace(layoutFrame.id, selfFragment).commit()
                R.id.action_preset -> fm.beginTransaction().replace(layoutFrame.id, presetFragment).commit()
            }
            return@setOnItemSelectedListener true
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAdd.setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(isCanceled)
        dialog.setCancelable(isCanceled)
        dialog.show()
    }
    */

    fun isNameEmpty(): Boolean = selfFragment.isEmpty()

    fun getItem(): EditData {
        return when (navigationView.selectedItemId) {
            R.id.action_self -> selfFragment.getItem()
            else -> presetFragment.getItem()
        }
    }

    interface OnDialogClickListener {
        fun onClicked()
    }
}