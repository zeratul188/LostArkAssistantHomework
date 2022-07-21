package com.lostark.lostarkassistanthomework.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Preset
import com.lostark.lostarkassistanthomework.dbs.FamilyDBAdapter
import com.lostark.lostarkassistanthomework.objects.EditData

class AddPresetFamilyFragment(private val type: String): Fragment() {
    private lateinit var listPresets: RecyclerView
    private lateinit var presetAdapter: PresetRecyclerAdapter

    lateinit var familyDBAdapter: FamilyDBAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_add_preset, container, false)

        listPresets = view.findViewById(R.id.listPresets)
        familyDBAdapter = FamilyDBAdapter(requireContext())

        val presets = ArrayList<Preset>()
        familyDBAdapter.open()
        val savedFrameFamilys = familyDBAdapter.getItems(type)
        if (savedFrameFamilys.isNotEmpty()) {
            savedFrameFamilys.forEach { item ->
                presets.add(Preset(item.name, item.max, item.icon, item.end, false))
            }
        }
        familyDBAdapter.close()
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