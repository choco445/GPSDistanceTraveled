package com.example.gpsdistancetraveled;

import static android.os.SystemClock.elapsedRealtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private static final int PERMISSIONS_CODE = 100;
    TextView lat;
    TextView lon;

    TextView address;
    TextView distText;
    TextView timeSpent;
    TextView locOne;
    TextView locTwo;
    TextView locThree;
    Geocoder geocoder;
    Location origlocation;
    Location currentLocation;

    List<Address> addressList;
    public static ArrayList <Long> times = new ArrayList<Long>();

    public static ArrayList<String> locations = new ArrayList<>();

    //long times;
    //public static long elapsedRealtime;
    public static double dist;
    public static int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lat = findViewById(R.id.id_lat);
        lon = findViewById(R.id.id_lon);
        address = findViewById(R.id.id_address);
        distText= findViewById(R.id.id_distance);
        locOne = findViewById(R.id.id_locationOne);
        locTwo = findViewById(R.id.id_locationTwo);
        locThree = findViewById(R.id.id_locationThree);
        timeSpent = findViewById(R.id.id_timeSpent);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocClass locationListener = new LocClass();


        geocoder = new Geocoder(this, Locale.getDefault());

        origlocation = new Location("original");

        //ArrayList <Long> times = new ArrayList<Long>();


        Log.d("LAT",lat.toString());


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("LAT",lat.toString());

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSIONS_CODE);

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
     else    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, locationListener);
    }

    public class LocClass implements LocationListener
    {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d("LAT","hello");

            counter++;
            if(counter==1) {
                currentLocation = location;
            }
            else {
                origlocation = currentLocation;
                currentLocation = location;
            }


            lat.setText("Latitude: "+location.getLatitude()+"");
            lon.setText("Longitude"+location.getLongitude()+"");

            //step 6 - geocoder
            //if(addressList.size()>0 && addressList != null)
            {
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    address.setText(addressList.get(0).getAddressLine(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //double dist = 0;
            if (counter != 1) {
                dist += origlocation.distanceTo(currentLocation);
                distText.setText("Distance: " + dist + " meters");
            }

            //first save oriinal oceation
            //then save new location
            //use distanceTo method to see distance between both locations


            times.add(elapsedRealtime());
            //locations.add(String.valueOf(location));



            if(currentLocation!=origlocation) {
                if (counter == 1) {
                    //locOne.setText("Location 1: "+locations.get(0));
                    locOne.setText(addressList.get(0).getAddressLine(0));
                    timeSpent.setText("Time spent: " + times.get(0));
                } else if (counter == 2) {
                    locTwo.setText(addressList.get(0).getAddressLine(0));
                    //locTwo.setText("Location 2: "+locations.get(1));
                    timeSpent.setText("Time spent: " + times.get(1));
                } else if (counter == 3) {
                    locThree.setText(addressList.get(0).getAddressLine(0));
                    //locThree.setText("Location 3: "+locations.get(2));
                    timeSpent.setText("Time spent: " + times.get(2));
                }
            }



        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //LocationListener.super.onStatusChanged(provider, status, extras);
        }
        @Override
        public void onProviderEnabled(@NonNull String provider) {
            //LocationListener.super.onProviderEnabled(provider);
        }
        @Override
        public void onProviderDisabled(@NonNull String provider) {
            //LocationListener.super.onProviderDisabled(provider);
        }

    }

    ///////////////////////////////////////////////



    //////////////////////////////////////////////


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("LAT",lat.toString());

        if (requestCode == PERMISSIONS_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //LocClass locationListener = new LocClass();
                //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }
            return;
        }
    }




}
