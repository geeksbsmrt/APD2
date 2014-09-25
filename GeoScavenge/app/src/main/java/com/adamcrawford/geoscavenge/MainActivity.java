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
import com.adamcrawford.geoscavenge.guess.GuessActivity;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MainActivity extends Activity implements ListFrag.OnHuntSelected {

    static String TAG = "MA";
    static Context sContext;
    public static FragmentManager sFragManager;
    public static SharedPreferences preferences;
    public static Boolean isConnected;
    DataHandler handler;
    private ListFrag lf;
    public static Messenger msgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        lf = (ListFrag) getFragmentManager().findFragmentById(R.id.listFrag);
        handler = new DataHandler(this);
        msgr = new Messenger(handler);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sContext = this;
        isConnected = getStatus(this);

        if (isConnected) {
            //TODO FIX THIS!
            String currentHunt = preferences.getString("currentHunt", "");
            if (!currentHunt.equals("")) {
                String mode = preferences.getString("type", "noType");
                String query = preferences.getString("currentHunt", "");
                if (mode.equals("public") || mode.equals("private")){
                    searchHunts(query, mode);
                } else {
                    Log.wtf(TAG, "should not be here");
                }
            } else {
                getData();
            }
        } else {
            Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.NETWORK);
            dialog.show(getFragmentManager(), "Network");
        }

    }

    private void sendData(ArrayList<HuntItem> hunts){
        lf.newData(hunts);
    }

    public void getData(){
        Intent getDynamo = new Intent(this, SyncService.class);
        Messenger msgr = new Messenger(handler);
        getDynamo.putExtra("type", SyncService.SyncType.LISTDATA);
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

    public static void searchHunts(String query, String mode) {
        Log.i(TAG, "Searching Hunts");
        Intent searchDynamo = new Intent(sContext, SyncService.class);
        searchDynamo.putExtra("type", SyncService.SyncType.SEARCH);
        searchDynamo.putExtra("query", query);
        searchDynamo.putExtra("msgr", msgr);
        searchDynamo.putExtra("mode", mode);
        sContext.startService(searchDynamo);
    }

    void confirmStart(HuntItem hunt) {
        Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.DETAILS);
        Bundle args = new Bundle();
        args.putSerializable("hunt", hunt);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "details");
    }

    static void startHunt(HuntItem hunt) {
        Intent gIntent = new Intent(sContext, GuessActivity.class);
        gIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        gIntent.putExtra("hunt", hunt);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("currentHunt", hunt.getHuntID());
        edit.putString("type", hunt.getHuntType());
        edit.apply();
        sContext.startActivity(gIntent);
    }

    public static class DataHandler extends Handler {

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
                        ArrayList<HuntItem> returned = (ArrayList<HuntItem>) msg.obj;
                        if (msg.arg1 == RESULT_OK && returned != null) {
                            Log.i(TAG, "Data returned");
                            activity.sendData(returned);
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
                            if (preferences.getString("currentHunt", "").equals("")) {
                                activity.confirmStart(hunt);
                            } else {
                                startHunt(hunt);
                            }
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
