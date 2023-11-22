
package com.example.transitapp.ui.home
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.transitapp.R
import com.example.transitapp.databinding.FragmentHomeBinding
import com.google.transit.realtime.GtfsRealtime
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import java.net.URL

class HomeFragment() : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private var mapView: MapView? = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var mapboxMap: MapboxMap
    private lateinit var viewAnnotationManager: ViewAnnotationManager

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

//        var intent: Intent = Intent()
//        val latitude = intent.getDoubleExtra("latitude", 0.0)
//        val longitude = intent.getDoubleExtra("longitude", 0.0)
//        val Txt = "$latitude and $longitude"

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        // Allow network operations on the main thread
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        val url = URL("https://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb")
//        val feed = url.openStream().toString()
        val feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream())

        for (entity in feed.entityList) {
            var Entity: GtfsRealtime.FeedEntity? = entity
            if (Entity != null) {
                Log.i("SUCCESS", Entity.vehicle.trip.routeId.toString())
                Log.i("SUCCESS", Entity.vehicle.position.latitude.toString())
                Log.i("SUCCESS", Entity.vehicle.position.longitude.toString())
                val latitude = entity.vehicle.position.latitude
                val longitude = entity.vehicle.position.longitude
                val point = Point.fromLngLat(longitude.toDouble(), latitude.toDouble())
                addViewAnnotation(point, Entity.vehicle.trip.routeId.toString() )

//                mapboxMap = binding.mapView.getMapboxMap().apply {
//                    // Load a map style
//                    loadStyleUri(Style.MAPBOX_STREETS) {
//                        // Get the center point of the map
//                        val center = mapboxMap.cameraState.center
//                        // Add the view annotation at the center point
//
//                    }
//                }

            }
        }
        return root
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
