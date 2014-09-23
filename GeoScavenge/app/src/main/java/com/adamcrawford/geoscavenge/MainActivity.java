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
import com.adamcrawford.geoscavenge.hunt.HuntItem;

import org.json.JSONArray;

import java.lang.ref.WeakReference;


public class MainActivity extends Activity implements ListFrag.OnHuntSelected {

    static String TAG = "MA";
    static Context sContext;
    public static FragmentManager sFragManager;
    public static SharedPreferences preferences;
    public static JSONArray huntArray = null;
    public static Boolean isConnected;
    DataHandler handler;
    static DataHandler sHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new DataHandler(this);
        sHandler = new DataHandler(this);

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
        Intent getDynamo = new Intent(this, SyncService.class);
        Messenger msgr = new Messenger(handler);
        getDynamo.putExtra("type", SyncService.SearchType.LISTDATA);
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
        Intent searchDynamo = new Intent(sContext, SyncService.class);
        Messenger msgr = new Messenger(sHandler);
        searchDynamo.putExtra("type", SyncService.SearchType.SEARCH);
        searchDynamo.putExtra("query", query);
        searchDynamo.putExtra("msgr", msgr);
        sContext.startService(searchDynamo);
    }

    static void confirmStart(HuntItem hunt) {
        Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.DETAILS);
        Bundle args = new Bundle();

        args.putSerializable("hunt", hunt);
        dialog.setArguments(args);
        dialog.show(sFragManager, "details");
    }

    static void startHunt(HuntItem hunt) {
        Intent gIntent = new Intent(sContext, GuessActivity.class);
        gIntent.putExtra("hunt", hunt);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("currentHunt", hunt.getHuntID());
        Log.i(TAG, String .valueOf(hunt.getHuntID()));
        edit.apply();
        sContext.startActivity(gIntent);
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
                getData();
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
                switch (msg.arg2){
                    case 0: {
                        JSONArray returned = (JSONArray) msg.obj;
                        if (msg.arg1 == RESULT_OK && returned != null) {
                            Log.i(TAG, "Data returned");
                            huntArray = returned;
                            activity.writeList();
                            break;
                        } else {
                            Log.i(TAG, "No data");
                            printToast(activity.getString(R.string.notFound));
                            break;
                        }
                    }
                    case 1: {
                        if (msg.arg1 == RESULT_OK && msg.obj != null) {
                            HuntItem hunt = (HuntItem) msg.obj;
                            confirmStart(hunt);
                            break;
                        } else {
                            printToast(activity.getString(R.string.notFound));
                            break;
                        }
                    }
                    default:{
                        break;
                    }
                }
            }
        }
    }
}
