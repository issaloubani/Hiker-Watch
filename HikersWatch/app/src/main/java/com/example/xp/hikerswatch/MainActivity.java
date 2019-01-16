package com.example.xp.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// this is a demo app for hikers
// it get's the location and coordinates
// Use it with caution . It bits :P
public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView textView;

    // update location and write it to the textView
    public void updateLocation(Location location) {

        String result = "";

        // getting info
        result += "Latitude : " + location.getLatitude();
        result += "\nLongitude : " + location.getLongitude();
        result += "\nAltitude : " + location.getAltitude();
        result += "\nAccuracy : " + location.getAccuracy();


        try {
            // Getting country name and area name if available
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (list != null && ((List) list).size() > 0) {

                if (list.get(0).getCountryName() != null)
                    result += "\nCountry : " + list.get(0).getCountryName();
                if (list.get(0).getLocality() != null)
                    result += "\nLocality : " + list.get(0).getCountryName();


            }
        } catch (IOException e) { // Show exception as toast to the user
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        textView.setText(result);
    }

    public void startListening() { // if permission if granted , start listening for location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // if permission is granted , start listening
            startListening();


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // var init
        textView = (TextView) findViewById(R.id.infoTextView); // main textView
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() { // listen to location
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location); // every time location change , the info changes
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        // Getting permission

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // Get permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else { // otherwise if permission if already granted
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateLocation(location); // update location on start to user location
        }
    }


}
