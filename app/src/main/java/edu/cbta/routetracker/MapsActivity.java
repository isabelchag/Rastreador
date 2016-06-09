package edu.cbta.routetracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    Location location;
    LocationRequest mLocationRequest;
    //private Location mLastLocation;
    public double saveHomeLat, saveHomeLng, saveEndLat, saveEndLng;
    ToggleButton tg;
    private static final double MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
    private static final double MILES_PER_KILOMETER = 0.621371192;
    Chronometer crono;
    long Time = 0;
    public int Radius;
    public  Location instLoc;
    LatLng miubi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /*int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck==0){
            mMap.setMyLocationEnabled(true);
        }*/



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            /*recyclerView = (RecyclerView) findViewById(R.id.rv);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);*/
            //return;
            Context contexto = getApplicationContext();
            CharSequence text = "Error, probablemente no haya conexión a Internet o no hayas otorgado permisos de GPS.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(contexto, text, duration);
            toast.show();
        } else {

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            tg = (ToggleButton) findViewById(R.id.trackingToggleButton);
            tg.setOnCheckedChangeListener(this);
            crono = (Chronometer) findViewById(R.id.chronometer);
            mapFragment.getMapAsync(this);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        }
        //GoogleMap.OnMyLocationButtonClickListener(LocationManager);
        TextView distance = (TextView) findViewById(R.id.distance);
        distance.setText("-");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFrarkeragt. This menmethod will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng hola = new LatLng(-34, 151);
        //LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng miubi = miUbicacion;
        //mMap.addMarker(new MarkerOptions().position(miUbicacion));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 16));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
            Context contexto = getApplicationContext();
            CharSequence text = "Upss, probablemente no haya conexión a Internet o no hayas otorgado permisos de GPS.";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(contexto, text, duration);
            toast.show();
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        TextView distanceTxt = (TextView) findViewById(R.id.distance);
        if(tg.isChecked()){
            mMap.clear();
            saveHomeLat = mMap.getMyLocation().getLatitude();
            saveHomeLng = mMap.getMyLocation().getLongitude();
            LatLng home = new LatLng(saveHomeLat,saveHomeLng); mMap.addMarker(new MarkerOptions().position(home).title("Inicio del recorrido").icon(BitmapDescriptorFactory.fromResource(R.mipmap.place_launcher)));
            /*TextView txt = (TextView) findViewById(R.id.mLongitudeText);
            TextView txt1 = (TextView) findViewById(R.id.mLatitudeText);
            txt.setText(String.valueOf(saveHomeLng).toString());
            txt1.setText(String.valueOf(saveHomeLat).toString());*/
            crono.setBase(SystemClock.elapsedRealtime());
            crono.start();

        }
        else{
            saveEndLat = mMap.getMyLocation().getLatitude();
            saveEndLng = mMap.getMyLocation().getLongitude();
            LatLng end = new LatLng(saveEndLat,saveEndLng);
            mMap.addMarker(new MarkerOptions().position(end).title("Fin del recorrido"));
            //int dis = getDistance(saveHomeLat,saveHomeLng,saveEndLat,saveEndLng);
            //String distancia = Distance(dis);
            //distance.setText(distancia);

            crono.stop();
            /*TextView txt = (TextView) findViewById(R.id.mLongitudeText);
            TextView txt1 = (TextView) findViewById(R.id.mLatitudeText);
            txt.setText(String.valueOf(saveEndLng).toString());
            txt1.setText(String.valueOf(saveEndLat).toString());*/
            //double distance;
            //instLoc.setLatitude(end.latitude);
            //instLoc.setLongitude(end.longitude);

            //distance = location.distanceTo(instLoc);
            //distanceTxt.setText(String.valueOf(distance));
            Location ploc = new Location("plo1");
            ploc.setLatitude(saveHomeLat);
            ploc.setLongitude(saveHomeLng);

            Location ploc2 = new Location("plo2");
            ploc2.setLatitude(saveEndLat);
            ploc2.setLongitude(saveEndLng);

            float dist = ploc.distanceTo(ploc2);
            distanceTxt.setText(String.valueOf(dist));


        }
    }

}
