package ro.pub.cs.systems.eim.practicaltest02v9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_activity)

        // Get the SupportMapFragment and notify when the map is ready
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Coordinates for Bucharest, Romania
        val bucharest = LatLng(44.4268, 26.1025)
        val ghelmegioaia = LatLng(	44.6167, 22.8333)

        // Add a marker in Bucharest and move the camera
        mMap.addMarker(MarkerOptions().position(bucharest).title("Marker in Bucharest"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghelmegioaia, 12f))
    }
}