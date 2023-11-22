package com.example.transitapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.transitapp.R
import com.example.transitapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val busNumbers = resources.getStringArray(R.array.bus)

        val autoCompleteTextView: AutoCompleteTextView = root.findViewById(R.id.autoComplete)

//        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, busNumbers)
//        autoCompleteTextView.setAdapter(adapter)
        // Create the adapter and set it to the AutoCompleteTextView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, busNumbers)
        autoCompleteTextView.setAdapter(adapter)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}