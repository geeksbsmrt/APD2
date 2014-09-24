package com.adamcrawford.geoscavenge.guess;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.adamcrawford.geoscavenge.Dialogs;
import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;


public class GuessActivity extends Activity implements LocationListener, GuessFrag.OnGuess {

    Bundle extras;
    LocationManager lManager;
    Criteria criteria;
    String TAG = "GA";
    HuntItem hunt;
    Integer guesses;
    SharedPreferences preferences;
    String currentDist;
    String pastDist;
    GuessFrag gf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_guess);

        extras = getIntent().getExtras();
        hunt = (HuntItem) extras.get("hunt");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        lManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        Integer prefGuesses = preferences.getInt("guesses", -1);
        currentDist = preferences.getString("pastDist", "");
        pastDist = preferences.getString("currentDist", "");
        if (prefGuesses > 0){
            guesses = prefGuesses;
        } else {
            guesses = hunt.getGuesses();
        }
        getLoc();
        gf = (GuessFrag) getFragmentManager().findFragmentById(R.id.guessFrag);
        gf.gRemain.setText(String.valueOf(guesses));
        if (!(pastDist.equals(""))){
            gf.pastGuess.setText(pastDist);
            gf.pastContainer.setVisibility(View.VISIBLE);
        }
        if (!(currentDist.equals(""))){
            gf.currentGuess.setText(currentDist);
            gf.currentContainer.setVisibility(View.VISIBLE);
        }
    }

    public Float getDist(){

        Location loc = lManager.getLastKnownLocation(lManager.getBestProvider(criteria, false));
        Location target = new Location("target");
        target.setLatitude(hunt.getHuntLat());
        target.setLongitude(hunt.getHuntLon());
        Float dist = loc.distanceTo(target);
        Log.i(TAG, String.valueOf(dist));
        return dist;
    }

    private void getLoc(){

        Log.i(TAG, "In getloc");
        if(!(lManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)))
        {
            MainActivity.printToast(getString(R.string.noGPS));
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
            startActivity(myIntent);
        }

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        if (lManager != null){

            Log.i(TAG, "lManager exists");

            // Normal updates while activity is visible.
            lManager.requestLocationUpdates(2*1000, 0, criteria, this, null);
            //lManager.requestLocationUpdates("gps", 2*1000, 0, this);

            // Register a receiver that listens for when a better provider than I'm using becomes available.
            String bestProvider = lManager.getBestProvider(criteria, false);
            String bestAvailableProvider = lManager.getBestProvider(criteria, true);
            if (bestProvider != null && !bestProvider.equals(bestAvailableProvider)) {
                Log.i(TAG, "better provider");
                lManager.requestLocationUpdates(bestProvider, 0, 0, bestInactiveLocationProviderListener, getMainLooper());
            }
        }
    }

    protected LocationListener bestInactiveLocationProviderListener = new LocationListener() {
        public void onLocationChanged(Location l) {}
        public void onProviderDisabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "Better accuracy");
            getLoc();
        }
    };

    @Override
    public void onLocationChanged(Location l) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i(TAG, "Status Changed");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i(TAG, "Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i(TAG, "Disabled");
    }

    @Override
    public void onClick(View view) {

        if (guesses > 0) {
            float dist = getDist();
            String measure = getString(R.string.meters);
            if (dist > 1609.34){
                dist = (float) (dist/1609.34);
                measure = getString(R.string.miles);
            } else if (dist == 1609.34) {
                dist = 1;
                measure = getString(R.string.mile);
            } else if (dist <= 5){
                guesses = -1;
                preferences.edit().putInt("currentHunt", -1).putString("currentDist", "").putString("pastDist", "").apply();
                Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.FOUND);
                dialog.show(MainActivity.sFragManager, "found");
                finish();
            }

            String currentText = String.format("%.2f %s", dist, measure);

            if (!(gf.currentContainer.getVisibility() == View.VISIBLE)){
                gf.currentContainer.setVisibility(View.VISIBLE);
            } else {
                gf.pastGuess.setText(gf.currentGuess.getText());
                pastDist = gf.currentGuess.getText().toString();
                if (!(gf.pastContainer.getVisibility() == View.VISIBLE)){
                    gf.pastContainer.setVisibility(View.VISIBLE);
                }
            }
            gf.currentGuess.setText(currentText);
            currentDist = currentText;
            gf.gRemain.setText(String.valueOf(--guesses));
        } else {
            guesses = -1;
            pastDist = "";
            currentDist = "";
        }
    }

    @Override
    protected void onPause() {
        if (lManager != null) {
            lManager.removeUpdates(this);
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        getLoc();
        super.onResume();
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor edit = MainActivity.preferences.edit();
        edit.putInt("guesses", guesses);
        edit.putString("pastDist", pastDist);
        edit.putString("currentDist", currentDist);
        edit.apply();
        super.onStop();
    }
}
