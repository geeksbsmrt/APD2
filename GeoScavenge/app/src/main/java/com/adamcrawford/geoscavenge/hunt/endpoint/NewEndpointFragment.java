package com.adamcrawford.geoscavenge.hunt.endpoint;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.adamcrawford.geoscavenge.R;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt.endpoint
 * File:    NewEndpointFragment
 * Purpose: TODO Minimum 2 sentence description
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
                //ask user if they want to save
            }
            case R.id.action_save: {
                getActivity().finish();
            }
            default: {
                return false;
            }
        }
    }
}