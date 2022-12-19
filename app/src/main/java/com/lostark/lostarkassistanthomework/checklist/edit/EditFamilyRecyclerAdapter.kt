package com.lostark.lostarkassistanthomework.checklist.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.settings.CheckDialog
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.IconSelectDialog
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import java.util.*
import kotlin.collections.ArrayList

class EditFamilyRecyclerAdapter(
    private val familys: ArrayList<Family>,
    private val context: Context,
    private val startDragListener: OnStartDragListener
): RecyclerView.Adapter<EditFamilyRecyclerAdapter.ViewHolder>(),
    EditItemTouchHelperCallback.OnItemMoveListener {
    interface OnStartDragListener {
        fun onStartDrag(holder: ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit, parent, false)
        val holder = ViewHolder(view)
        holder.clearFocus()
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val family = familys[position]
        val pos = position
        val listener = View.OnClickListener {
            val checkDialog = CheckDialog(context)
            checkDialog.setData("${family.name}의 숙제를 삭제하시겠습니까?", "삭제", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {
                    val customToast = CustomToast(App.context())
                    customToast.createToast("${family.name}의 숙제를 삭제하였습니다.", false)
                    customToast.show()
                    familys.removeAt(pos)
                    notifyDataSetChanged()
                }
            })
            checkDialog.show(true)
        }
        val iconListener = View.OnClickListener {
            val iconDialog = IconSelectDialog(context)
            iconDialog.setOnClickListener(object : IconSelectDialog.OnDialogClickListener {
                override fun onClicked() {
                    val icon = iconDialog.getResult()
                    family.icon = icon.icon
                    notifyDataSetChanged()
                }
            })
            iconDialog.show(true)
        }
        val touchListener = View.OnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                startDragListener.onStartDrag(holder)
            }
            return@OnTouchListener false
        }
        holder.apply {
            bind(family, context, listener, iconListener, touchListener)
            itemView.tag = family
        }
    }

    override fun getItemCount(): Int = familys.size

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(familys, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val view = v
        val imgIcon: ImageView = view.findViewById(R.id.imgIcon)
        val edtName: EditText = view.findViewById(R.id.edtName)
        val edtNow: EditText = view.findViewById(R.id.edtNow)
        val edtMax: EditText = view.findViewById(R.id.edtMax)
        val sprEnd: Spinner = view.findViewById(R.id.sprEnd)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val imgHandle: ImageView = view.findViewById(R.id.imgHandle)
        var btnMinDown: ImageButton = view.findViewById(R.id.btnMinDown)
        var btnMinUp: ImageButton = view.findViewById(R.id.btnMinUp)
        var btnMaxDown: ImageButton = view.findViewById(R.id.btnMaxDown)
        var btnMaxUp: ImageButton = view.findViewById(R.id.btnMaxUp)

        fun clearFocus() {
            edtName.clearFocus()
            edtNow.clearFocus()
            edtMax.clearFocus()
        }

        fun bind(
            family: Family,
            context: Context,
            listener: View.OnClickListener,
            iconListener: View.OnClickListener,
            touchListener: View.OnTouchListener
        ) {
            val ends = context.resources.getStringArray(R.array.ends)
            val endAdapter = ArrayAdapter(context, R.layout.txt_item_end, ends)
            sprEnd.adapter = endAdapter
            val pos = ends.indexOf(family.end)
            if (pos != -1) {
                sprEnd.setSelection(pos)
            }

            imgIcon.setImageResource(context.resources.getIdentifier(family.icon, "drawable", context.packageName))
            edtName.setText(family.name)
            edtNow.setText(family.now.toString())
            edtMax.setText(family.max.toString())

            btnDelete.setOnClickListener(listener)
            imgIcon.setOnClickListener(iconListener)
            imgHandle.setOnTouchListener(touchListener)
            edtName.addTextChangedListener(NameTextWatcher(family))
            edtNow.addTextChangedListener(NowTextWatcher(family))
            edtMax.addTextChangedListener(MaxTextWatcher(family))
            sprEnd.onItemSelectedListener = EndTextWatcher(family)

            btnMinDown.setOnClickListener {
                if (family.now > 0) {
                    family.now--
                    edtNow.setText(family.now.toString())
                }
            }
            btnMinUp.setOnClickListener {
                if (family.now < family.max) {
                    family.now++
                    edtNow.setText(family.now.toString())
                }
            }
            btnMaxDown.setOnClickListener {
                if (family.max > 0) {
                    family.max--
                    edtMax.setText(family.max.toString())
                }
            }
            btnMaxUp.setOnClickListener {
                if (family.max < 99) {
                    family.max++
                    edtMax.setText(family.max.toString())
                }
            }
        }

        inner class NameTextWatcher(private val family: Family) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtName.hasFocus()) {
                    family.name = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inner class NowTextWatcher(private val family: Family) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtNow.hasFocus()) {
                    if (s.toString() == "") {
                        family.now = 0
                    } else {
                        var value = s.toString().toInt()
                        if (value > family.max) {
                            value = family.max
                        }
                        family.now = value
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inner class MaxTextWatcher(private val family: Family) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtMax.hasFocus()) {
                    if (s.toString() == "") {
                        family.max = 1
                    } else {
                        var value = s.toString().toInt()
                        if (value == 0) {
                            edtMax.setText("1")
                            value = 1
                        }
                        family.max = value
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inner class EndTextWatcher(private val family: Family) : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val ends = App.context().resources.getStringArray(R.array.ends)
                family.end = ends[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}