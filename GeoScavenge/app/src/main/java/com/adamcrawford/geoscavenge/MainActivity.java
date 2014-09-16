package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends Activity {

    static Context sContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sContext = this.getApplicationContext();

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
}
