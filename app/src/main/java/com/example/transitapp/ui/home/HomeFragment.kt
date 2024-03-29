
package com.example.transitapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.transitapp.R
import com.example.transitapp.databinding.FragmentHomeBinding
import com.google.transit.realtime.GtfsRealtime
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.io.File
import java.net.URL


class HomeFragment() : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private var mapView: MapView? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var mapboxMap: MapboxMap
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 10000L // 20 seconds
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val latitude = arguments?.getDouble("latitude", 0.0) ?: 0.0
        val longitude = arguments?.getDouble("longitude", 0.0) ?: 0.0

        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // MapView initialization
        mapView = root.findViewById(R.id.mapView)
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)
        viewAnnotationManager = _binding!!.mapView.viewAnnotationManager
        fetchAndRefreshBusPositions()
//        var intent: Intent = Intent()
//        val latitude = intent.getDoubleExtra("latitude", 0.0)
//        val longitude = intent.getDoubleExtra("longitude", 0.0)
//        val Txt = "$latitude and $longitude"

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        // Schedule periodic updates
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewAnnotationManager.removeAllViewAnnotations()
                fetchAndRefreshBusPositions()
                handler.postDelayed(this, refreshInterval)
            }
        }, refreshInterval)

        return root
    }
    public fun fetchAndRefreshBusPositions(){
        // Allow network operations on the main thread
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        val url = URL("https://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb")
//        val feed = url.openStream().toString()
        val feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream())

        for (entity in feed.entityList) {
            var Entity: GtfsRealtime.FeedEntity? = entity
            if (Entity != null) {
                //Routes text Files
                val fileName = "user_routes.txt"
                val content = context?.openFileInput(fileName)?.bufferedReader()?.useLines { lines ->
                    lines.joinToString("\n")
                }
                //Converting to an array
                val array1: Array<String> = content.toString().split(",").toTypedArray()

                Log.i("SUCCESS", Entity.vehicle.trip.routeId.toString())
                Log.i("SUCCESS", Entity.vehicle.position.latitude.toString())
                Log.i("SUCCESS", Entity.vehicle.position.longitude.toString())

                val latitude = entity.vehicle.position.latitude
                val longitude = entity.vehicle.position.longitude
                val point = Point.fromLngLat(longitude.toDouble(), latitude.toDouble())
                val routeId = Entity.vehicle.trip.routeId.toString()
                for (i: String in array1) {
                    if (i == routeId) {
                        addViewAnnotation(point, routeId)
                        val unwrappedDrawable = AppCompatResources.getDrawable(
                            requireContext(), R.drawable.rounded_corner_view
                        )
                        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                        DrawableCompat.setTint(wrappedDrawable, Color.RED)
                    } else {
                        addViewAnnotation(point, routeId)
                        val unwrappedDrawable = AppCompatResources.getDrawable(
                            requireContext(), R.drawable.rounded_corner_view
                        )
                        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                        DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun addViewAnnotation(point: Point, Id:String) {
        // Define the view annotation
        val viewAnnotation = viewAnnotationManager.addViewAnnotation(
            // Specify the layout resource id
            resId = R.layout.annotationlayout,
            // Set any view annotation options
            options = viewAnnotationOptions {
                geometry(point)
            }
        )
        var text: TextView = viewAnnotation.findViewById(R.id.annotation)
        text.setText(Id)

    }

}
