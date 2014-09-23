package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adamcrawford.geoscavenge.hunt.HuntItem;


public class GuessActivity extends Activity implements LocationListener {

    static Bundle extras;
    static LocationManager lManager;
    static Criteria criteria;
    static String TAG = "GA";
    static HuntItem hunt;
    static Integer guesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        extras = getIntent().getExtras();
        hunt = (HuntItem) extras.get("hunt");

        lManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //TODO Update this if SharedPreferences is different
        guesses = hunt.getGuesses();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GuessFrag())
                    .commit();
        }
        getLoc();
    }

    public static Float getDist(){

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
        edit.apply();
        super.onStop();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GuessFrag extends Fragment implements View.OnClickListener {

        TextView gRemain;
        LinearLayout currentContainer;
        LinearLayout pastContainer;
        TextView currentGuess;
        TextView pastGuess;

        public GuessFrag() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_guess, container, false);
            gRemain = (TextView) rootView.findViewById(R.id.guessesRemain);
            currentContainer = (LinearLayout) rootView.findViewById(R.id.currentContainer);
            currentGuess = (TextView) rootView.findViewById(R.id.currentGuess);
            pastContainer = (LinearLayout) rootView.findViewById(R.id.pastContainer);
            pastGuess = (TextView) rootView.findViewById(R.id.pastGuess);
            ImageButton gButton = (ImageButton) rootView.findViewById(R.id.guessButton);
            gButton.setOnClickListener(this);

            Log.i(TAG, String.valueOf(hunt.getHuntID()));
            Log.i(TAG, String.valueOf((MainActivity.preferences.getInt("currentHunt", -1))));

            //TODO Get information from SharedPrefs about hunt when returning to it
            gRemain.setText(String.valueOf(guesses));

            return rootView;
        }

        @Override
        public void onClick(View view) {

            if (guesses > 0) {
                float dist = GuessActivity.getDist();
                String measure = getString(R.string.meters);
                if (dist > 1609.34){
                    dist = (float) (dist/1609.34);
                    measure = getString(R.string.miles);
                } else if (dist == 1609.34) {
                    dist = 1;
                    measure = getString(R.string.mile);
                } else if (dist <= 5){
                    guesses = -1;
                    MainActivity.preferences.edit().putInt("currentHunt", -1).apply();
                    Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.FOUND);
                    dialog.show(MainActivity.sFragManager, "found");
                    getActivity().finish();
                }

                String currentText = String.format("%.2f %s", dist, measure);

                if (!(currentContainer.getVisibility() == View.VISIBLE)){
                    currentContainer.setVisibility(View.VISIBLE);
                } else {
                    pastGuess.setText(currentGuess.getText());
                    if (!(pastContainer.getVisibility() == View.VISIBLE)){
                        pastContainer.setVisibility(View.VISIBLE);
                    }
                }
                currentGuess.setText(currentText);
                gRemain.setText(String.valueOf(--guesses));
            }
        }
    }
}
