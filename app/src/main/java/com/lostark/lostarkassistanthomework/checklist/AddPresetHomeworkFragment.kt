package com.lostark.lostarkassistanthomework.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Preset
import com.lostark.lostarkassistanthomework.dbs.HomeworkDBAdapter
import com.lostark.lostarkassistanthomework.objects.EditData

class AddPresetHomeworkFragment(private val type: String) : Fragment() {
    private lateinit var listPresets: RecyclerView
    private lateinit var presetAdapter: PresetRecyclerAdapter

    lateinit var homeworkDBAdapter: HomeworkDBAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_add_preset, container, false)

        listPresets = view.findViewById(R.id.listPresets)

        homeworkDBAdapter = HomeworkDBAdapter(requireContext())

        val presets = ArrayList<Preset>()
        homeworkDBAdapter.open()
        val savedFrameHomeworks = homeworkDBAdapter.getItems()
        if (savedFrameHomeworks.isNotEmpty()) {
            savedFrameHomeworks.forEach { item ->
                if (item.type == type) {
                    presets.add(Preset(item.name, item.max, item.icon, item.end, false))
                }
            }
        }
        homeworkDBAdapter.close()
        presets[0].isChecked = true

        presetAdapter = PresetRecyclerAdapter(presets, requireContext())
        listPresets.adapter = presetAdapter
        listPresets.addItemDecoration(RecyclerViewDecoration(0, 10))

        return view
    }

    fun getItem(): EditData {
        return presetAdapter.getItem()
    }
}