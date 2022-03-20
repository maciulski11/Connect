package com.example.connect.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.connect.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_share_posts.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude = 0.0
    private var longitude = 0.0

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val oulu = LatLng(51.172491, 9.57766)
        val zoomLevel = 4.175f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oulu, zoomLevel))

        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener(this)
        setMapLongClick(mMap)
        setUpMap()
    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Your location:\n$latitude \n$longitude", Toast.LENGTH_LONG)
            .show()

        latitude = location.latitude
        longitude = location.longitude

        val markerOptions = MarkerOptions()
            .position(LatLng(latitude, longitude))
            .title(getString(R.string.your_location))
            .snippet(getAddress(LatLng(latitude, longitude)))
            mMap.addMarker(markerOptions)
    }

    // Allow users to add markers on map with long click
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener {
            map.addMarker(
                MarkerOptions()
                    .position(it)
                    .title(getString(R.string.dropped_pin))
                    .snippet(getAddress(LatLng(it.latitude, it.longitude)))
            )
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        confirmInfo.visibility = View.VISIBLE
        cancelBT.setOnClickListener { confirmInfo.visibility = View.INVISIBLE }


        locationEditText.setText("${getAddress(LatLng(latitude, longitude))}")

        return false
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
                    return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null){
                lastLocation = location

                latitude = location.latitude
                longitude = location.longitude

                val currentLatLong = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }
}

