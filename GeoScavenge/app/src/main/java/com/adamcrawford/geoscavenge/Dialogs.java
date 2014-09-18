package com.adamcrawford.geoscavenge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge
 * File:    Dialogs
 * Purpose: TODO Minimum 2 sentence description
 */
public class Dialogs extends DialogFragment {

    private String TAG = "Dialogs";
    public static DialogType type;
    public enum DialogType {
        DETAILS,
        SEARCH
    }

    public Dialogs() {}

    public static Dialogs newInstance(DialogType dType){
        type = dType;
        return new Dialogs();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        switch (type){
            case DETAILS: {
                final Bundle args = getArguments();
                View view = inflater.inflate(R.layout.fragment_details, null);
                TextView details = (TextView) view.findViewById(R.id.huntDetails);
                TextView guesses = (TextView) view.findViewById(R.id.detailGuesses);
                try {
                    JSONObject hunt = new JSONObject(args.getString("hunt"));
                    details.setText(hunt.getString("desc"));
                    guesses.setText(hunt.getString("guesses") + " " + getString(R.string.guesses));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                builder.setView(view)
                        .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               Intent gIntent = new Intent(getActivity() ,GuessActivity.class);
                               gIntent.putExtra("hunt", args.getString("hunt"));
                               startActivity(gIntent);
                           }
                       })
                       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               Dialogs.this.getDialog().cancel();
                           }
                       })
                       .setTitle(R.string.huntDetails);
                break;
            }
            case SEARCH: {
                Log.i(TAG, "In Search");
                builder.setView(inflater.inflate(R.layout.fragment_private_entrance, null))
                        .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Dialog dialog = Dialogs.this.getDialog();
                                EditText input = (EditText) dialog.findViewById(R.id.searchInput);
                                String query = input.getText().toString();
                                MainActivity.searchHunts(query);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Dialogs.this.getDialog().cancel();
                            }
                        }).setTitle(R.string.action_search);
                break;
            }
            default: {
                break;
            }
        }
        return builder.create();
    }
}
