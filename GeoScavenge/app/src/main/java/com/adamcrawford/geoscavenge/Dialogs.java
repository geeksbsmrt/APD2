package com.adamcrawford.geoscavenge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.adamcrawford.geoscavenge.hunt.list.HuntItem;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge
 * File:    Dialogs
 */
public class Dialogs extends DialogFragment {

    private String TAG = "Dialogs";
    public static DialogType type;
    public enum DialogType {
        DETAILS,
        SEARCH,
        FOUND,
        NETWORK
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
                Bundle args = getArguments();
                View view = inflater.inflate(R.layout.fragment_details, null);
                TextView details = (TextView) view.findViewById(R.id.huntDetails);
                TextView ends = (TextView) view.findViewById(R.id.detailEnds);
                final HuntItem hunt = (HuntItem) args.getSerializable("hunt");
                details.setText(hunt.getHuntDesc());
                ends.setText(hunt.getNumEnds().toString());
                builder.setView(view)
                        .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               MainActivity.startHunt(hunt, getActivity().getApplicationContext());
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
                                MainActivity.searchHunts(query, "private");
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
            case FOUND: {
                Log.i(TAG, "Found");
                Bundle args = getArguments();
                final HuntItem hunt = (HuntItem) args.getSerializable("hunt");
                Integer currentEnd = Integer.parseInt(args.getString("currentEnd"));
                View view = inflater.inflate(R.layout.fragment_found, null);
                TextView endDesc = (TextView) view.findViewById(R.id.foundDesc);
//                ImageView imgView = (ImageView) view.findViewById(R.id.foundImg);
//                imgView.setImageURI(Uri.fromFile(new File(args.getString("endImg"))));
                endDesc.setText(hunt.getHuntEnds().get(currentEnd).getEndDesc());
                builder.setView(view)
                        .setTitle(R.string.locFound);
                if (hunt.getHuntEnds().size() > currentEnd+1) {
                    //Another endpoint
                    builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.startHunt(hunt, getActivity().getApplicationContext());
                        }
                    });
                } else {
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Dialogs.this.getDialog().cancel();
                        }
                    });
                }
                break;
            }
            case NETWORK: {
                builder.setView(inflater.inflate(R.layout.not_connected, null))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Dialogs.this.getDialog().cancel();
                            }
                        })
                        .setTitle(R.string.notConnected);
                break;
            }
            default: {
                break;
            }
        }
        return builder.create();
    }
}
