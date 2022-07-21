package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.objects.EditData

class AddFamilyDialog(context: Context, type: String): DialogFragment() {
    private lateinit var onClickListener: OnDialogClickListener

    private val selfFragment = AddSelfHomeworkFragment()
    private val presetFragment = AddPresetFamilyFragment(type)

    lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        AddFamilyDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment)?.commit()

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