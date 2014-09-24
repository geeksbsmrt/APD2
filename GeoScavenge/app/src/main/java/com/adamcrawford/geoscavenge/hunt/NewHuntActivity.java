package com.adamcrawford.geoscavenge.hunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.adamcrawford.geoscavenge.R;
import com.adamcrawford.geoscavenge.data.SyncService;
import com.adamcrawford.geoscavenge.hunt.endpoint.NewEndpointActivity;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;

public class NewHuntActivity extends Activity implements NewHuntFragment.OnNewHunt {

    NewHuntFragment nhf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_hunt);
        nhf = (NewHuntFragment) getFragmentManager().findFragmentById(R.id.newHuntFrag);
    }

    @Override
    public void onClick(View view) {
        Intent eIntent = new Intent(nhf.getActivity(), NewEndpointActivity.class);
        startActivityForResult(eIntent, 0);
    }

    @Override
    public void saveHunt(HuntItem hunt, String mode){
        Intent sIntent = new Intent(nhf.getActivity(), SyncService.class);
        sIntent.putExtra("type", SyncService.SyncType.PUTITEM);
        sIntent.putExtra("mode", mode);
        sIntent.putExtra("hunt", hunt);
        startService(sIntent);
        nhf.getActivity().finish();
    }
}
