package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.Fragment;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adamcrawford.geoscavenge.hunt.HuntConstructor;

import org.json.JSONException;
import org.json.JSONObject;


public class GuessActivity extends Activity implements LocationListener {

    static Bundle extras;
    static LocationManager lManager;
    static Criteria criteria;
    static String TAG = "GA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);
        extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GuessFrag())
                    .commit();
        }
    }

    public static Float getDist(){
        Float dist = Float.valueOf(-1);
        JSONObject hunt;
        try {
            hunt = new JSONObject(extras.getString("hunt"));
            double lat = hunt.getDouble("lat");
            double lon = hunt.getDouble("lon");
            Location loc = lManager.getLastKnownLocation(lManager.getBestProvider(criteria, false));
            Location target = new Location("target");
            target.setLatitude(lat);
            target.setLongitude(lon);
            dist = loc.distanceTo(target);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, String.valueOf(dist));
        return dist;
    }

    private void getLoc(){

        Log.i(TAG, "In getloc");

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
    public void onLocationChanged(Location location) {
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
        lManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        getLoc();

        super.onResume();
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


            JSONObject hunt;
            try {
                hunt = new JSONObject(extras.getString("hunt"));
                gRemain.setText(hunt.getString("guesses"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return rootView;
        }

        @Override
        public void onClick(View view) {
            Integer i = Integer.parseInt(gRemain.getText().toString());
            if (i > 0) {
                float dist = GuessActivity.getDist();
                String measure = getString(R.string.meters);
                if (dist > 1609.34){
                    dist = (float) (dist/1609.34);
                    measure = getString(R.string.miles);
                } else if (dist == 1609.34) {
                    dist = 1;
                    measure = getString(R.string.mile);
                } else if (dist <= 5){
                    //Location found
                }

                String test = String.format("%.2f %s", dist, measure);

                if (!(currentContainer.getVisibility() == View.VISIBLE)){
                    currentContainer.setVisibility(View.VISIBLE);
                } else {
                    pastGuess.setText(currentGuess.getText());
                    if (!(pastContainer.getVisibility() == View.VISIBLE)){
                        pastContainer.setVisibility(View.VISIBLE);
                    }
                }
                currentGuess.setText(test);
                gRemain.setText(String.valueOf(--i));
            }
        }
    }
}
