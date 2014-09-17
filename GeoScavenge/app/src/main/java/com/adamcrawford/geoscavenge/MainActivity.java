package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.adamcrawford.geoscavenge.hunt.HuntConstructor;


public class MainActivity extends Activity implements ListFrag.OnHuntSelected {

    static String TAG = "MA";
    static Context sContext;
    static FragmentManager sFragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sContext = this.getApplicationContext();
        sFragManager = getFragmentManager();

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
        Bundle test = new Bundle();
        test.putSerializable("hunt", hunt);
        sContext.startActivity(gIntent, test);
    }

    public static void searchHunts(String query) {
        Log.i(TAG, "Searching Hunts");

        //TODO Search based on Query

        Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.DETAILS);
        Bundle args = new Bundle();

        args.putSerializable("hunt", "test1");
        dialog.setArguments(args);
        dialog.show(sFragManager, "details");
    }
}
