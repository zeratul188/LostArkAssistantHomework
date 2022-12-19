package com.lostark.lostarkassistanthomework.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.objects.InputPreset

class SelfFragment : Fragment() {
    lateinit var edtLevel: EditText
    lateinit var sprServers: Spinner
    lateinit var sprJobs: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_self, container, false)

        edtLevel = view.findViewById(R.id.edtLevel)
        sprServers = view.findViewById(R.id.sprServers)
        sprJobs = view.findViewById(R.id.sprJobs)

        val jobs = requireActivity().resources.getStringArray(R.array.job)
        val jobAdapter = ArrayAdapter(requireContext(), R.layout.txt_item_job, jobs)
        sprJobs.adapter = jobAdapter

        val servers = requireActivity().resources.getStringArray(R.array.servers)
        val serverAdapter = ArrayAdapter(requireContext(), R.layout.txt_item_job, servers)
        sprServers.adapter = serverAdapter

        return view
    }

    fun getPreset(): InputPreset {
        var level = 0.0
        if (edtLevel.text.toString() != "") {
            level = edtLevel.text.toString().toDouble()
        }
        val job = sprJobs.selectedItem.toString()
        val server = sprServers.selectedItem.toString()
        return InputPreset(level, job, server)
    }
}