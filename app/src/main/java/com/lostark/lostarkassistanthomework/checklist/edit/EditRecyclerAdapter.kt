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
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.objects.EditData
import java.util.*
import kotlin.collections.ArrayList

class EditRecyclerAdapter(
    private val items: ArrayList<EditData>,
    private val context: Context,
    private val homework: Homework,
    private val startDragListener: OnStartDragListener
) : RecyclerView.Adapter<EditRecyclerAdapter.ViewHolder>(),
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
        val item = items[position]
        val pos = position
        val listener = View.OnClickListener {
            val checkDialog = CheckDialog(context)
            checkDialog.setData("${item.name}의 숙제를 삭제하시겠습니까?", "삭제", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {
                    val customToast = CustomToast(App.context())
                    customToast.createToast("${item.name}의 숙제를 삭제하였습니다.", false)
                    customToast.show()
                    items.removeAt(pos)
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
                    item.icon = icon.icon
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
            bind(item, context, listener, iconListener, touchListener)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v
        var imgIcon: ImageView
        var edtName: EditText
        var edtNow: EditText
        var edtMax: EditText
        var sprEnd: Spinner
        var btnDelete: ImageButton
        var imgHandle: ImageView
        var btnMinDown: ImageButton
        var btnMinUp: ImageButton
        var btnMaxDown: ImageButton
        var btnMaxUp: ImageButton

        init {
            imgIcon = view.findViewById(R.id.imgIcon)
            edtName = view.findViewById(R.id.edtName)
            edtNow = view.findViewById(R.id.edtNow)
            edtMax = view.findViewById(R.id.edtMax)
            sprEnd = view.findViewById(R.id.sprEnd)
            btnDelete = view.findViewById(R.id.btnDelete)
            imgHandle = view.findViewById(R.id.imgHandle)
            btnMinDown = view.findViewById(R.id.btnMinDown)
            btnMinUp = view.findViewById(R.id.btnMinUp)
            btnMaxDown = view.findViewById(R.id.btnMaxDown)
            btnMaxUp = view.findViewById(R.id.btnMaxUp)
        }

        fun clearFocus() {
            edtName.clearFocus()
            edtNow.clearFocus()
            edtMax.clearFocus()
        }

        fun bind(
            item: EditData,
            context: Context,
            listener: View.OnClickListener,
            iconListener: View.OnClickListener,
            touchListener: View.OnTouchListener
        ) {


            val ends = context.resources.getStringArray(R.array.ends)
            val endAdapter = ArrayAdapter(context, R.layout.txt_item_end, ends)
            sprEnd.adapter = endAdapter
            val pos = ends.indexOf(item.end)
            if (pos != -1) {
                sprEnd.setSelection(pos)
            }

            imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            edtName.setText(item.name)
            edtNow.setText(item.now.toString())
            edtMax.setText(item.max.toString())

            btnDelete.setOnClickListener(listener)
            imgIcon.setOnClickListener(iconListener)
            imgHandle.setOnTouchListener(touchListener)
            edtName.addTextChangedListener(NameTextWatcher(item, context))
            edtNow.addTextChangedListener(NowTextWatcher(item))
            edtMax.addTextChangedListener(MaxTextWatcher(item))
            sprEnd.onItemSelectedListener = EndTextWatcher(item)

            btnMinDown.setOnClickListener {
                if (item.now > 0) {
                    item.now--
                    edtNow.setText(item.now.toString())
                }
            }
            btnMinUp.setOnClickListener {
                if (item.now < item.max) {
                    item.now++
                    edtNow.setText(item.now.toString())
                }
            }
            btnMaxDown.setOnClickListener {
                if (item.max > 0) {
                    item.max--
                    edtMax.setText(item.max.toString())
                }
            }
            btnMaxUp.setOnClickListener {
                if (item.max < 99) {
                    item.max++
                    edtMax.setText(item.max.toString())
                }
            }
        }
        /*
        fun printList(item: EditData, pos : Int) {
            println("------------------------------------------")
            println("Position : ${pos}")
            println("Name : ${item.name}")
            println("Icon : ${item.icon}")
            println("------------------------------------------")
        }
         */

        inner class NameTextWatcher(private val item: EditData, private val context: Context) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtName.text.toString().indexOf(",") != -1) {
                    edtName.setText(edtName.text.toString().replace(",", ""))
                    val toast = CustomToast(context)
                    toast.createToast("\",\"기호는 사용하실 수 없습니다.", false)
                    toast.show()
                }

                if (edtName.hasFocus()) {
                    item.name = s.toString().replace(",", "")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inner class NowTextWatcher(private val item: EditData) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtNow.hasFocus()) {
                    if (s.toString() == "") {
                        item.now = 0
                    } else {
                        var value = s.toString().toInt()
                        if (value > item.max) {
                            value = item.max
                        }
                        item.now = value
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inner class MaxTextWatcher(private val item: EditData) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtMax.hasFocus()) {
                    if (s.toString() == "") {
                        item.max = 1
                    } else {
                        var value = s.toString().toInt()
                        if (value == 0) {
                            edtMax.setText("1")
                            value = 1
                        }
                        item.max = value
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        inner class EndTextWatcher(private val item: EditData) : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val ends = App.context().resources.getStringArray(R.array.ends)
                item.end = ends[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}