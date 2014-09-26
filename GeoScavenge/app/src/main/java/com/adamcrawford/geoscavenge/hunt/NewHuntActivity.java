package com.adamcrawford.geoscavenge.hunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.view.View;

import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;
import com.adamcrawford.geoscavenge.data.SyncService;
import com.adamcrawford.geoscavenge.hunt.endpoint.EndItem;
import com.adamcrawford.geoscavenge.hunt.endpoint.NewEndpointActivity;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;

import java.util.ArrayList;
import java.util.Random;

public class NewHuntActivity extends Activity implements NewHuntFragment.OnNewHunt {

    NewHuntFragment nhf;
    ArrayList<EndItem> endList;
    String TAG = "NHA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_hunt);
        nhf = (NewHuntFragment) getFragmentManager().findFragmentById(R.id.newHuntFrag);
        endList = new ArrayList<EndItem>();
    }

    @Override
    public void onClick(View view) {
        Intent eIntent = new Intent(nhf.getActivity(), NewEndpointActivity.class);
        startActivityForResult(eIntent, 0);
    }

    @Override
    public void saveHunt(){
        if(endList.size() > 0){
            HuntItem hunt = new HuntItem();
            hunt.setHuntID(String.valueOf(new Random().nextInt(Integer.MAX_VALUE) + 1));
            hunt.setHuntDesc(nhf.huntDescView.getText().toString());
            hunt.setHuntName(nhf.huntNameView.getText().toString());
            hunt.setHuntEnds(endList);
            hunt.setNumEnds(endList.size());
            if (nhf.huntMode.isChecked()) {
                hunt.setHuntType("private");
            } else {
                hunt.setHuntType("public");
            }
            Intent sIntent = new Intent(nhf.getActivity(), SyncService.class);
            sIntent.putExtra("type", SyncService.SyncType.PUTITEM);
            sIntent.putExtra("mode", hunt.getHuntType());
            sIntent.putExtra("hunt", hunt);
            startService(sIntent);
            if (hunt.getHuntType().equals("private")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.huntID));
                builder.setMessage(getString(R.string.keep) +"\n\n"+ hunt.getHuntID());
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nhf.getActivity().finish();
                    }
                });
                builder.show();
            } else {
                nhf.getActivity().finish();
            }
        } else {
            MainActivity.printToast(getString(R.string.noEnds));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0){
                Bundle extras = data.getExtras();
                EndItem end = (EndItem) extras.get("item");
                endList.add(end);
                nhf.numEnds.setText(String.valueOf(endList.size()));
                if (!nhf.numEnds.getText().equals("")){
                    nhf.numEndsContainer.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void finish() {
        Intent getDynamo = new Intent(this, SyncService.class);
        Messenger msgr = new Messenger(MainActivity.handler);
        getDynamo.putExtra("type", SyncService.SyncType.LISTDATA);
        getDynamo.putExtra("msgr", msgr);
        startService(getDynamo);
        super.finish();
    }
}
