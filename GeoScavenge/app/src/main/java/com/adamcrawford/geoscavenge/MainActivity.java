package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.adamcrawford.geoscavenge.hunt.HuntConstructor;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements ListFrag.OnHuntSelected {

    static String TAG = "MA";
    static Context sContext;
    static FragmentManager sFragManager;
    public static JSONObject hunt1 = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sContext = this.getApplicationContext();
        sFragManager = getFragmentManager();

        try {
            hunt1.put("id", 1);
            hunt1.put("name","Hunt1");
            hunt1.put("desc", "Hunt for buried treasure");
            hunt1.put("lat", 28.596597);
            hunt1.put("lon", -81.301316);
            hunt1.put("guesses", 50);
            hunt1.put("endDesc", "Get your degree and you will have the opportunity for endless wealth!");
            //This may not be correct.  It will be tested for Milestone 2
            hunt1.put("imgPath", String.valueOf(R.drawable.fsu));
        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    public void onHuntSelected(HuntConstructor hunt) {
        startHunt(hunt);
    }

    public static void startHunt(HuntConstructor hunt) {
        Intent gIntent = new Intent(sContext, GuessActivity.class);
        gIntent.putExtra("hunt", hunt);
        MainActivity.sContext.startActivity(gIntent);
    }

    public static void searchHunts(String query) {
        Log.i(TAG, "Searching Hunts");

        //TODO Search based on Query

        Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.DETAILS);
        Bundle args = new Bundle();

        args.putString("hunt", hunt1.toString());
        dialog.setArguments(args);
        dialog.show(sFragManager, "details");
    }
}
