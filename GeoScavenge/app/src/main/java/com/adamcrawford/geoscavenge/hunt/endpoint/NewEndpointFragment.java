package com.adamcrawford.geoscavenge.hunt.endpoint;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt.endpoint
 * File:    NewEndpointFragment
 */
public class NewEndpointFragment extends Fragment {
    OnNewEnd parentActivity;
    ImageButton currentLoc;
    ImageButton searchLoc;
    ImageButton addPic;
    EditText endLat;
    EditText endLon;
    EditText endDesc;
    EditText endGuesses;

    public NewEndpointFragment() {
    }

    public interface OnNewEnd extends View.OnClickListener {
        @Override
        void onClick(View view);

        void saveEnd();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnNewEnd) {
            try {
                parentActivity = (OnNewEnd) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnGuess");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_endpoint, container, false);
        setHasOptionsMenu(true);
        currentLoc = (ImageButton) rootView.findViewById(R.id.getCurrent);
        currentLoc.setOnClickListener(parentActivity);
        searchLoc = (ImageButton) rootView.findViewById(R.id.searchAddress);
        searchLoc.setOnClickListener(parentActivity);
        addPic = (ImageButton) rootView.findViewById(R.id.addPic);
        addPic.setOnClickListener(parentActivity);
        endDesc = (EditText) rootView.findViewById(R.id.endDesc);
        endLat = (EditText) rootView.findViewById(R.id.endLat);
        endLon = (EditText) rootView.findViewById(R.id.endLon);
        endGuesses = (EditText) rootView.findViewById(R.id.endGuesses);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                builder.setTitle(getString(R.string.save));
                builder.setMessage(getString(R.string.askSave));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (endDesc.getText() != null && endLat.getText() != null && endLon.getText() != null && endGuesses.getText() != null) {
                            parentActivity.saveEnd();
                        } else {
                            MainActivity.printToast(getString(R.string.required));
                        }
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });
            }
            case R.id.action_save: {
                if (endDesc.getText() != null && endLat.getText() != null && endLon.getText() != null && endGuesses.getText() != null) {
                    parentActivity.saveEnd();
                } else {
                    MainActivity.printToast(getString(R.string.required));
                }
            }
            default: {
                return false;
            }
        }
    }
}