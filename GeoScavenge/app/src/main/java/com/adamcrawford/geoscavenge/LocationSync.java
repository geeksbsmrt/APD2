package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

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
        lSync.activity = act;
        lSync.startSync();
    }

    public void quit(){
        LocationSync lSync = LocationSync.getInstance();
        if (lSync.lManager != null) {
            lSync.lManager.removeUpdates(lSync);
        }
    }

    public Location getCurrentLoc(){
        return lManager.getLastKnownLocation(lManager.getBestProvider(criteria, false));
    }

    public Address getLocation(Double lat, Double lon, Context c){
        if (Geocoder.isPresent()){
            Geocoder coder = new Geocoder(c);
            List<Address> address = null;

            try {
                while (address == null) {
                    address = coder.getFromLocation(lat, lon, 5);
                }
                Address location = address.get(0);
                Log.i(TAG, location.toString());
                location.getLatitude();
                location.getLongitude();
                return location;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public Address getLocationFromAddress(String strAddress, Context applicationContext) {

        if (Geocoder.isPresent()){
            Log.i(TAG, "Geocoder Present");
            Geocoder coder = new Geocoder(applicationContext);
            List<Address> address = null;

            try {
                while (address == null) {
                    address = coder.getFromLocationName(strAddress, 5);
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                return location;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e(TAG, "No Geocoder");
            return null;
        }
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
        public void onLocationChanged(Location l) {
            Log.i(TAG, "loc changed");
        }
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
