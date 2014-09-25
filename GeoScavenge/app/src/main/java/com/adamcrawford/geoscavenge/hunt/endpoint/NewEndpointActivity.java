package com.adamcrawford.geoscavenge.hunt.endpoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.adamcrawford.geoscavenge.LocationSync;
import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;

public class NewEndpointActivity extends Activity implements NewEndpointFragment.OnNewEnd {
    NewEndpointFragment nef;
    String TAG = "NEA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_endpoint);
        nef = (NewEndpointFragment) getFragmentManager().findFragmentById(R.id.endFrag);
    }

    @Override
    public void onClick(View view) {
        final LocationSync lSync = LocationSync.getInstance();
        lSync.init(this);
        switch (view.getId()){
            case R.id.getCurrent:{
                nef.endLon.setText(String.valueOf(lSync.getLoc().getLongitude()/1E6));
                nef.endLat.setText(String.valueOf(lSync.getLoc().getLatitude()/1E6));
                break;
            }
            case R.id.searchAddress:{
                if (Geocoder.isPresent()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final LayoutInflater inflater = this.getLayoutInflater();
                    View v = inflater.inflate(R.layout.fragment_private_entrance, null);
                    final EditText eT = (EditText) v.findViewById(R.id.searchInput);
                    eT.setHint(R.string.addressHint);
                    eT.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                    builder.setView(v)
                            .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String query = eT.getText().toString();
                                    Address location = lSync.getLocationFromAddress(query, getApplicationContext());
                                    if (location != null) {
                                        nef.endLat.setText(String.valueOf(location.getLatitude()));
                                        nef.endLon.setText(String.valueOf(location.getLongitude()));
                                    } else {
                                        MainActivity.printToast(getString(R.string.notAvail));
                                    }
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    lSync.quit();
                                    dialogInterface.cancel();
                                }
                            }).setTitle(R.string.address);
                    builder.show();
                } else {
                    MainActivity.printToast(getString(R.string.notAvail));
                }
                break;
            }
            case R.id.addPic:{

                break;
            }
            default:{
                break;
            }
        }

    }
}
