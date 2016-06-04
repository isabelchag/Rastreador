package edu.cbta.routetracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.MapView;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LocationManager locationManager; // gives location data
    private MapView mapView; //displays a Google map
    //private MapController mapController; //manages map pan/zoom
    private Location previousLocation; // previous reported location
    //private RouteOverlay routeOverlay; // Overlay that shows route on map
    private long distanceTraveled; // total distance the user traveled
    //private BearingFrameLayout bearingFrameLayoutl; // rotates the MapView
    private boolean tracking; // whether app is curently tracking
    private long startTime; // time /(in milliseconds) when tracking starts
    private PowerManager.WakeLock wakeLock; // used to prevent devic sleep
    private boolean gpsFix; //whether we have a GPS fix for accurate data

    private static final double MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
    private static final double MILES_PER_KILOMETER = 0.621371192;

    private static final int MAP_ZOOM = 18; //Google Maps supports 1-21

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else {
            // Show rationale and request permission.
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
