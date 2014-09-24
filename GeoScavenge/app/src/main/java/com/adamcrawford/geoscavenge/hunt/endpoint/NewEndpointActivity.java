package com.adamcrawford.geoscavenge.hunt.endpoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.adamcrawford.geoscavenge.R;

public class NewEndpointActivity extends Activity implements NewEndpointFragment.OnNewEnd {
    NewEndpointFragment nef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_endpoint);
        nef = (NewEndpointFragment) getFragmentManager().findFragmentById(R.id.endFrag);
    }

    @Override
    public void onClick(View view) {

    }
}
