package com.example.transitapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.transitapp.R
import com.example.transitapp.databinding.FragmentDashboardBinding
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.FileWriter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var button: Button
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

        autoCompleteTextView = root.findViewById(R.id.autoComplete)

//        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, busNumbers)
//        autoCompleteTextView.setAdapter(adapter)
        // Create the adapter and set it to the AutoCompleteTextView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, busNumbers)
        autoCompleteTextView.setAdapter(adapter)
        button = binding.button
        button.setOnClickListener(){
            addBusRoute()
        }
        updateDisplayedRoutes()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun addBusRoute() {
        val selectedRoute = autoCompleteTextView.text.toString()

        if (selectedRoute.isNotBlank()) {
            saveBusRoute(selectedRoute)
            oneDisplayedRoutes()

        }
    }

    private fun saveBusRoute(route: String) {
        val fileName = "user_routes.txt"
        val busArray: Array<String> = resources.getStringArray(R.array.bus)
        val file = File(requireContext().filesDir, fileName)
        val content = context?.openFileInput(fileName)?.bufferedReader()?.useLines { lines ->
            lines.joinToString("\n")
        }

        val array1: Array<String> = content.toString().split(",").toTypedArray()
        var array: Array<String> = array1.toSet().toTypedArray()
        try {
            if (route !in array && route in busArray) {
                array += route
                val content = array.joinToString(separator = ",")
                FileOutputStream(file).use { fos ->
                    fos.write("$content".toByteArray())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun updateDisplayedRoutes(){
        val filename = "user_routes.txt"
        val content = context?.openFileInput(filename)?.bufferedReader()?.useLines { lines ->
            lines.joinToString("\n")
        }
        val array: Array<String> = content.toString().split(",").toTypedArray()
        if (array != null) {
            for(i: String in array){
                val Layout: LinearLayout = binding.linear
                val textView: TextView = TextView(requireContext())
                textView.text = i
                textView.gravity = Gravity.LEFT
                Layout.addView(textView)
                Log.i("SUCCESS",i)
            }

        }
    }
    private fun oneDisplayedRoutes(){
        val filename = "user_routes.txt"
        val content = context?.openFileInput(filename)?.bufferedReader()?.useLines { lines ->
            lines.joinToString("\n")
        }
        val array: Array<String> = content.toString().split(",").toTypedArray()
        if (array != null) {
            var i: String = array.last()
            val Layout: LinearLayout = binding.linear
            val textView: TextView = TextView(requireContext())
            textView.text = i
            textView.gravity = Gravity.LEFT
            Layout.addView(textView)
            Log.i("SUCCESS",i)

        }
    }
}