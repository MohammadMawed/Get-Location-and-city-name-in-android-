package com.mawed.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView cityTextView, longitudeTextView, latitudeTextView;
    Button getLocationButton;
    LocationManager locationManager;

    final int requestCode = 1 ;
    int Distance = 10;
    int UpdateTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = findViewById(R.id.textView3);
        longitudeTextView = findViewById(R.id.textView);
        latitudeTextView = findViewById(R.id.textView2);
        getLocationButton = findViewById(R.id.button);

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    public void getLocation(){
        locationManager = (LocationManager) this.getSystemService(MainActivity.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions ( MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, requestCode );
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UpdateTime, Distance, new LocationListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onLocationChanged(Location location) {

                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                String longitudeString = String.valueOf(longitude);
                String latitudeSting = String.valueOf(latitude);

                longitudeTextView.setText(longitudeString);
                latitudeTextView.setText(latitudeSting);

                Geocoder gcd = new Geocoder ( getBaseContext (), Locale.getDefault () );
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation ( location.getLatitude (),
                            location.getLongitude (), 1 );
                    if (addresses.size () > 0) {
                        String cityName = addresses.get ( 0 ).getLocality ();
                        cityTextView.setText("City: " + cityName);
                    }
                } catch (IOException e) {
                    e.printStackTrace ();
                }
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
        });
    }
}