package com.example.transitapp

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.OnSuccessListener


class StartActivity : AppCompatActivity() {
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private val REQUEST_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Verify the permissions granted
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    fun getLocation(){
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            // Permission Granted - Get location from device
            Log.i("Testing","Granted PErmission")
            fusedLocationProviderClient?.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                ?.addOnSuccessListener { location ->
                    if (location != null) {
//                        Log.i("Location", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                        // ... rest of the code
                        val latitude = location.latitude
                        val longitude = location.longitude

                        val intent = Intent(this@StartActivity, MainActivity::class.java)
                        intent.putExtra("latitude", latitude)
                        intent.putExtra("longitude", longitude)
                        startActivity(intent)
                    }
                }
        }else{
            // Permission Denied - Ask the user for permission
            askPermission()
        }
    }

    private fun askPermission() {
        // Display screen to request permission
        ActivityCompat.requestPermissions(
            this, arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }
}