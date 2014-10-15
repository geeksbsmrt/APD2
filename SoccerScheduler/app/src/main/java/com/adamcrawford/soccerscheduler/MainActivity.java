package com.adamcrawford.soccerscheduler;

import android.app.Activity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "qX6JLxI9x8AOGTu1aEtfFWtAgc165M2VtxtNFlcn", "6qX7AISTy7qhqBolZVpEhEc61jVnsZ5Ja2cSyszD");
        ParseFacebookUtils.initialize("274223656120484");
        ParseTwitterUtils.initialize("SJo5TeALa88ojYTVMTsGghrwX", "sZng3S25e3PI0skrW2EqTJ8SgCYe0NCSaPrIfDvueM55hIsoWi");
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new Fragment_MainActivity())
                    .commit();
        }
    }
}