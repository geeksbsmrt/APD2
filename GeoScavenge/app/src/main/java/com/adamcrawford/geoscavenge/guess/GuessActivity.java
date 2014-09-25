package com.adamcrawford.geoscavenge.guess;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.adamcrawford.geoscavenge.Dialogs;
import com.adamcrawford.geoscavenge.LocationSync;
import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;


public class GuessActivity extends Activity implements GuessFrag.OnGuess {

    Bundle extras;
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
        Integer prefGuesses = preferences.getInt("guesses", -1);

        if (prefGuesses > 0 && hunt.getHuntID().equals(preferences.getString("currentHunt", ""))){
            guesses = prefGuesses;
            currentDist = preferences.getString("pastDist", "");
            pastDist = preferences.getString("currentDist", "");
            if (!(pastDist.equals(""))){
                gf.pastGuess.setText(pastDist);
                gf.pastContainer.setVisibility(View.VISIBLE);
            }
            if (!(currentDist.equals(""))){
                gf.currentGuess.setText(currentDist);
                gf.currentContainer.setVisibility(View.VISIBLE);
            }
        } else {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("currentHunt", hunt.getHuntID());
            edit.putString("type", hunt.getHuntType());
            edit.apply();
            //TODO Update based on endPoint number
            guesses = hunt.getHuntEnds().get(0).getGuesses();
        }
        LocationSync.getInstance().init(this);
        gf = (GuessFrag) getFragmentManager().findFragmentById(R.id.guessFrag);
        gf.gRemain.setText(String.valueOf(guesses));
    }

    public Float getDist(){

        Location loc = LocationSync.getInstance().getLoc();
        Location target = new Location("target");
        //TODO UPDATE FOR CURRENT END
        target.setLatitude(hunt.getHuntEnds().get(0).getEndLat());
        target.setLongitude(hunt.getHuntEnds().get(0).getEndLon());
        Float dist = loc.distanceTo(target);
        Log.i(TAG, String.valueOf(dist));
        return dist;
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
        LocationSync.getInstance().quit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocationSync.getInstance().init(this);
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
