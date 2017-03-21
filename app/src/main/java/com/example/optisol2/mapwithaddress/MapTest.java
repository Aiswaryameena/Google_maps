package com.example.optisol2.mapwithaddress;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapTest extends Activity implements OnMapReadyCallback {

    Location currentLocation;
    Double currentLatitude;
    Double currentLongitude;
    String locationToBeSend;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng markOnMap;
    private GoogleMap map;
    String location;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map))

                .getMapAsync((OnMapReadyCallback) this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                updateLocation(location);

                getAddress();

            }


            public void onStatusChanged(String provider, int status,

                                        Bundle extras) {

            }


            public void onProviderEnabled(String provider) {

            }


            public void onProviderDisabled(String provider) {

            }

        };


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(

                LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);


        }


    void getAddress() {

        try {

            Geocoder gcd = new Geocoder(MapTest.this, Locale.getDefault());

            List<Address> addresses = gcd.getFromLocation(currentLatitude,

                    currentLongitude, 100);

            StringBuilder result = new StringBuilder();


            if (addresses.size() > 0) {


                Address address = addresses.get(1);

                int maxIndex = address.getMaxAddressLineIndex();

                for (int x = 0; x <= maxIndex; x++) {

                    result.append(address.getAddressLine(x));

                    result.append(",");

                }


            }

            location = result.toString();

            showLocationOnMap(currentLatitude, currentLongitude, location);

        } catch (IOException ex) {

            Toast.makeText(MapTest.this, ex.getMessage(),

                    Toast.LENGTH_LONG).show();


        }

    }


    void updateLocation(Location location) {

        currentLocation = location;

        currentLatitude = currentLocation.getLatitude();

        currentLongitude = currentLocation.getLongitude();


    }


    void showLocationOnMap(double latitude, double longitude, String location) {

        markOnMap = new LatLng(latitude, longitude);


        Marker mark = map

                .addMarker(new MarkerOptions()

                        .position(markOnMap)

                        .title(location)

                        .snippet("You are here")

                        .icon(BitmapDescriptorFactory

                                .fromResource(R.drawable.map_icon)));


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(markOnMap, 15));


        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }


    @Override

    public void onDestroy() {

        try {


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListener);



            super.onDestroy();

        }



        catch (Exception e) {



        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
