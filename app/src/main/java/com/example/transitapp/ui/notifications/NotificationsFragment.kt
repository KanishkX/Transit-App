package com.example.transitapp.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.transitapp.databinding.FragmentNotificationsBinding
import com.google.transit.realtime.GtfsRealtime
import com.mapbox.geojson.Point
import org.w3c.dom.Text
import java.net.URL

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val url = URL("https://gtfs.halifax.ca/realtime/Alert/Alerts.pb")
//        val feed = url.openStream().toString()
        val feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream())

        for (entity in feed.entityList) {
            var Entity: GtfsRealtime.FeedEntity? = entity
            if (Entity != null) {
                Log.i("SUCCESS", Entity.toString())
                val Layout: LinearLayout = binding.linear
                val textView: TextView = TextView(requireContext())
                textView.setText(Entity.alert.descriptionText.translationList[0].text.toString())
                Layout.addView(textView)

            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}