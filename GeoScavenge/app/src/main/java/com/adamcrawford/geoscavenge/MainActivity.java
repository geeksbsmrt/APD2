package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.adamcrawford.geoscavenge.data.SyncService;
import com.adamcrawford.geoscavenge.hunt.HuntConstructor;
import com.adamcrawford.geoscavenge.hunt.HuntItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity implements ListFrag.OnHuntSelected {

    static String TAG = "MA";
    static Context sContext;
    public static FragmentManager sFragManager;
    public static SharedPreferences preferences;
    static HuntItem hunt = null;
    public static JSONArray huntArray = null;
    public static Boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        isConnected = getStatus(this);

        sContext = this.getApplicationContext();
        sFragManager = getFragmentManager();

        //Log.i(TAG, String.valueOf(preferences.getInt("currentHunt", -1)));
        //TODO Finish logic using remote data

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ListFrag())
                    .commit();
        }
    }

    public void getData(){
        final DataHandler handler = new DataHandler(this);
        Intent getDynamo = new Intent(this, SyncService.class);
        Messenger msgr = new Messenger(handler);
        getDynamo.putExtra("msgr", msgr);
        startService(getDynamo);
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
    public void onHuntSelected(HuntItem hunt) {
        confirmStart(hunt);
    }

    public static void searchHunts(Integer query) {
        Log.i(TAG, "Searching Hunts");

        //TODO Search based on Query
        try {
            hunt = (HuntItem) huntArray.get(query);
            if (!hunt.getHuntID().equals(query)) {
                MainActivity.printToast(sContext.getString(R.string.notFound));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        confirmStart(hunt);
    }

    static void confirmStart(HuntItem hunt) {
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

    void writeList(){
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new ListFrag())
                .commit();
    }

    @Override
    protected void onResume() {
        isConnected = getStatus(this);

        if (isConnected) {
            //TODO Fix getting from SharedPreferences
//            if ((preferences.getInt("currentHunt", -1)) > 0) {
//                try {
//                    hunt = new HuntConstructor(huntArray.getJSONObject(preferences.getInt("currentHunt", -1) - 1));
//                    startHunt(hunt);
//                } catch (JSONException e) {
//                    Log.e(TAG, e.getMessage());
//                }
//            } else {
                getData();
//            }
        } else {
            Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.NETWORK);
            dialog.show(getFragmentManager(), "Network");
        }

        super.onResume();
    }
    private static class DataHandler extends Handler {

        String TAG = "DH";
        private final WeakReference<MainActivity> mainActivityWeakReference;
        public DataHandler(MainActivity activity) {
            mainActivityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mainActivityWeakReference.get();
            if (activity != null) {
                JSONArray returned = (JSONArray) msg.obj;
                if (msg.arg1 == RESULT_OK && returned != null) {
                    Log.i(TAG, "Data returned");
                    huntArray = returned;
                    activity.writeList();
//                    activity.writeList(returned);
//                    for (int i = 0, j = returned.length(); i < j; i++){
//                        HuntItem hunt = (HuntItem) returned.get(i);
//                        Log.i(TAG, hunt.getHuntName());
//                    }
                } else {
                    Log.i(TAG, "No data");
                }
            }
        }
    }
}
