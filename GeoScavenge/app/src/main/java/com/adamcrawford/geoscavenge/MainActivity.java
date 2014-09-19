package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.adamcrawford.geoscavenge.hunt.HuntConstructor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements ListFrag.OnHuntSelected {

    static String TAG = "MA";
    static Context sContext;
    public static FragmentManager sFragManager;
    public static JSONObject hunt1 = new JSONObject();
    public static JSONObject hunt2 = new JSONObject();
    public static SharedPreferences preferences;
    static HuntConstructor hunt = null;
    public static JSONArray huntArray;
    public static Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        huntArray = new JSONArray();
        try {
            hunt1.put("id", 1);
            hunt1.put("name", "Hunt1");
            hunt1.put("desc", "Hunt for buried treasure");
            hunt1.put("lat", 28.596597);
            hunt1.put("lon", -81.301316);
            hunt1.put("guesses", 50);
            hunt1.put("endDesc", "Get your degree and you will have the opportunity for endless wealth!");
            //This may not be correct.  It will be tested for Milestone 2
            hunt1.put("imgPath", String.valueOf(R.drawable.fsu));
            hunt2.put("id", 2);
            hunt2.put("name", "Hunt2");
            hunt2.put("desc", "Find the love of your life");
            hunt2.put("lat", 28.419791);
            hunt2.put("lon", 81.581187);
            hunt2.put("guesses", 100);
            hunt2.put("endDesc", "Dance with the Princes and Princesses, watch stunning fireworks, become entranced with that special someone.");
            //This may not be correct.  It will be tested for Milestone 2
            hunt2.put("imgPath", String.valueOf(R.drawable.castle));
            huntArray.put(hunt1).put(hunt2);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        sContext = this.getApplicationContext();
        sFragManager = getFragmentManager();

        Log.i(TAG, String.valueOf(preferences.getInt("currentHunt", -1)));
        //TODO Finish logic using remote data
        if ((preferences.getInt("currentHunt", -1)) > 0) {
            try {
                hunt = new HuntConstructor(huntArray.getJSONObject(preferences.getInt("currentHunt", -1) - 1));
                startHunt(hunt);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ListFrag())
                    .commit();
        }
    }

    public static void printToast(String message) {
        //set length for message to be displayed
        int duration = Toast.LENGTH_LONG;
        //create message based on input parameter then display it
        Toast error = Toast.makeText(sContext, message, duration);
        error.show();
    }

    public Boolean getStatus(Context c) {
        Log.i(TAG, "In getStatus");

        //build connectivity manager and network info
        ConnectivityManager conMan = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        //true/false based on connectivity
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void onHuntSelected(HuntConstructor hunt) {
        confirmStart(hunt);
    }

    public static void searchHunts(Integer query) {
        Log.i(TAG, "Searching Hunts");

        //TODO Search based on Query
        try {
            hunt = new HuntConstructor(huntArray.getJSONObject(query-1));
            if (hunt.huntID != query) {
                MainActivity.printToast(sContext.getString(R.string.notFound));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        confirmStart(hunt);
    }

    static void confirmStart(HuntConstructor hunt) {
        Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.DETAILS);
        Bundle args = new Bundle();

        args.putSerializable("hunt", hunt);
        dialog.setArguments(args);
        dialog.show(sFragManager, "details");
    }

    void startHunt(HuntConstructor hunt) {
        Intent gIntent = new Intent(this, GuessActivity.class);
        gIntent.putExtra("hunt", hunt);
        startActivity(gIntent);
    }

    @Override
    protected void onResume() {
        isConnected = getStatus(this);

        if (isConnected) {
            //TODO Get data from remote
            printToast(getString(R.string.connected));
        } else {
            //TODO Get Data from local device
            printToast(getString(R.string.notConnected));
        }

        super.onResume();
    }
}
