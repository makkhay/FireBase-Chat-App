package edu.sfsu.csc780.chathub;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.Manifest;

/**
 * Created by makkhay on 3/4/18.
 */

public class LocationUtils {


    private static final String LOG_TAG = "LocationUtils";
    public static final int REQUEST_CODE = 100;
    private static final long MIN_TIME = 1;
    private static final float MIN_DISTANCE =10 ;
    private static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static int GRANTED = PackageManager.PERMISSION_GRANTED;
    private static final String[] LOCATION_PERMISSIONS =
            {FINE_LOCATION, COARSE_LOCATION};
    private static Location sLocation;
    private static LocationListener sLocationListener;


    private static Context context;
    public LocationUtils(Context context){
       this.context = context;
     }

    

    public static double getLat() {
        return (sLocation != null) ? sLocation.getLatitude() : 0.0;
    }

    public static double getLon() {
        return (sLocation != null) ? sLocation.getLongitude() : 0.0;
    }


    public static double startLocationUpdates() {



            // Acquire a reference to the system Location Manager
            LocationManager locationManager =
                    (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            if (sLocationListener == null) {
// Define a listener that responds to location updates
                sLocationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        Log.d(LOG_TAG, "lat: " + location.getLatitude()
                                + " lon: " + location.getLongitude());
                        sLocation = location;
                    }

                    public void onStatusChanged(String provider, int status, Bundle
                            extras) {
                    }

                    public void onProviderEnabled(String provider) {
                    }

                    public void onProviderDisabled(String provider) {
                    }
                };
            }

            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), FINE_LOCATION) !=
                    GRANTED && ActivityCompat.checkSelfPermission(context.getApplicationContext(),
                    COARSE_LOCATION) != GRANTED) {
                {
                    Log.d(LOG_TAG, "requesting permissions for starting");
                    ActivityCompat.requestPermissions((Activity) context, LOCATION_PERMISSIONS, REQUEST_CODE);
                }
            }
            Log.d(LOG_TAG, "requesting updates");

            Location location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                Log.d(LOG_TAG, "last known lat: " + location.getLatitude()
                        + " lon: " + location.getLongitude());
                sLocation = location;
            }

            Log.d(LOG_TAG, "requesting updates");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME,
                    MIN_DISTANCE, sLocationListener);


            return GRANTED;

        }


}
