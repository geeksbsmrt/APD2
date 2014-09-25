package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge
 * File:    LocationSync
 */
public class LocationSync implements LocationListener {
    String TAG = "LocMgr";
    LocationManager lManager;
    Criteria criteria;
    Activity activity;
    private static LocationSync locationInstance = null;

    public static LocationSync getInstance() {
        if (locationInstance == null) {
            locationInstance = new LocationSync();
        }
        return locationInstance;
    }

    public void init(Activity act){
        LocationSync lSync = LocationSync.getInstance();
        lSync.lManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
        lSync.startSync();
    }

    public void quit(){
        LocationSync lSync = LocationSync.getInstance();
        if (lSync.lManager != null) {
            lSync.lManager.removeUpdates(lSync);
        }
    }

    public Location getLoc(){
        return lManager.getLastKnownLocation(lManager.getBestProvider(criteria, false));
    }

    private LocationSync() {
    }

    private void startSync(){

        Log.i(TAG, "In getloc");
        if(!(lManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)))
        {
            MainActivity.printToast(activity.getString(R.string.noGPS));
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS );
            activity.startActivity(myIntent);
        }

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (lManager != null){
            Log.i(TAG, "lManager exists");
            // Normal updates while activity is visible.
            lManager.requestLocationUpdates(2*1000, 0, criteria, this, null);

            // Register a receiver that listens for when a better provider than I'm using becomes available.
            String bestProvider = lManager.getBestProvider(criteria, false);
            String bestAvailableProvider = lManager.getBestProvider(criteria, true);
            if (bestProvider != null && !bestProvider.equals(bestAvailableProvider)) {
                Log.i(TAG, "better provider");
                lManager.requestLocationUpdates(bestProvider, 0, 0, bestInactiveLocationProviderListener, activity.getMainLooper());
            }
        }
    }

    protected LocationListener bestInactiveLocationProviderListener = new LocationListener() {
        public void onLocationChanged(Location l) {}
        public void onProviderDisabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "Better accuracy");
            startSync();
        }
    };

    @Override
    public void onLocationChanged(Location location) {

    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {

    }
    @Override
    public void onProviderDisabled(String s) {

    }
}
