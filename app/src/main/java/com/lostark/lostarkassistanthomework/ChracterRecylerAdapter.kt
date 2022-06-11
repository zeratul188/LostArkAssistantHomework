package com.lostark.lostarkassistanthomework

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.objects.Chracter

class ChracterRecylerAdapter(private val list: ArrayList<Chracter>, private val context: Context) :
    RecyclerView.Adapter<ChracterRecylerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chracter_list, parent, false)
        return ChracterRecylerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val listener = View.OnClickListener { it ->
            if (item.isChecked) {
                item.isChecked = false
                holder.chkCheck.isChecked = false
            } else {
                item.isChecked = true
                holder.chkCheck.isChecked = true
            }
            notifyDataSetChanged()
        }
        holder.apply {
            bind(listener, item, context)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        lateinit var imgJob: ImageView
        lateinit var txtName: TextView
        lateinit var txtServer: TextView
        lateinit var txtLevel: TextView
        lateinit var txtJob: TextView
        lateinit var chkCheck: CheckBox
        fun bind(listener: View.OnClickListener, item: Chracter, context: Context) {
            imgJob = view.findViewById(R.id.imgJob)
            txtName = view.findViewById(R.id.txtName)
            txtServer = view.findViewById(R.id.txtServer)
            txtLevel = view.findViewById(R.id.txtLevel)
            txtJob = view.findViewById(R.id.txtJob)
            chkCheck = view.findViewById(R.id.chkCheck)

            val jobs = context.resources.getStringArray(R.array.job)
            val pos = jobs.indexOf(item.job)+1;
            imgJob.setImageResource(context.resources.getIdentifier("jbi${pos}", "drawable", context.packageName))
            txtName.text = item.name
            txtServer.text = item.server
            txtLevel.text = "Lv.${item.level.toString()}"
            txtJob.text = item.job
            chkCheck.isChecked = item.isChecked

            chkCheck.setOnClickListener {
                if (item.isChecked) {
                    item.isChecked = false
                    chkCheck.isChecked = false
                } else {
                    item.isChecked = true
                    chkCheck.isChecked = true
                }
            }

            view.setOnClickListener(listener)
        }
    }
}