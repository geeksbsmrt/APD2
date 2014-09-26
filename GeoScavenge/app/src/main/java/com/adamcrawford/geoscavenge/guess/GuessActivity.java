package com.adamcrawford.geoscavenge.guess;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.adamcrawford.geoscavenge.LocationSync;
import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;
import com.adamcrawford.geoscavenge.data.SyncService;
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
    Integer numEnds;
    Integer currentEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_guess);

        extras = getIntent().getExtras();
        hunt = (HuntItem) extras.get("hunt");
        numEnds = hunt.getNumEnds();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Integer prefGuesses = preferences.getInt("guesses", -1);

        if (prefGuesses > 0 && hunt.getHuntID().equals(preferences.getString("currentHunt", ""))){
            guesses = prefGuesses;
            currentDist = preferences.getString("pastDist", "");
            pastDist = preferences.getString("currentDist", "");
            currentEnd = preferences.getInt("currentEnd", -1);
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
            edit.putInt("currentEnd", 0);
            edit.apply();
            currentEnd = 0;
        }
        guesses = hunt.getHuntEnds().get(currentEnd).getGuesses();
        LocationSync.getInstance().init(this);
        gf = (GuessFrag) getFragmentManager().findFragmentById(R.id.guessFrag);
        gf.gRemain.setText(String.valueOf(guesses));
    }

    public Float getDist(){

        Location loc = LocationSync.getInstance().getCurrentLoc();
        Location target = new Location("target");
        target.setLatitude(hunt.getHuntEnds().get(currentEnd).getEndLat());
        target.setLongitude(hunt.getHuntEnds().get(currentEnd).getEndLon());
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
                //FOUND!
                Intent getImgIntent = new Intent(this, SyncService.class);
                Messenger messenger = new Messenger(MainActivity.handler);
                getImgIntent.putExtra("type", SyncService.SyncType.GETIMG);
                getImgIntent.putExtra("msg", messenger);
                getImgIntent.putExtra("hunt", hunt);
                getImgIntent.putExtra("currentEnd", currentEnd);
                startService(getImgIntent);

                guesses = -1;
                if (numEnds == 1) {
                    preferences.edit().putInt("currentHunt", -1).putString("currentDist", "").putString("pastDist", "").putInt("guesses", -1).remove("currenEnd").apply();
                } else {
                    preferences.edit().putString("currentDist", "").putString("pastDist", "").putInt("currentEnd", currentEnd++).putInt("guesses", -1).apply();
                }
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
