package com.adamcrawford.geoscavenge.hunt;

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
import android.widget.ImageButton;

import com.adamcrawford.geoscavenge.R;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt
 * File:    NewHuntFragment
 * Purpose: TODO Minimum 2 sentence description
 */
public class NewHuntFragment extends Fragment {
    OnNewHunt parentActivity;

    public NewHuntFragment() {
    }

    public interface OnNewHunt extends View.OnClickListener {
        @Override
        void onClick(View view);

        void saveHunt(HuntItem hunt, String mode);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnNewHunt) {
            try {
                parentActivity = (OnNewHunt) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnGuess");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_new_hunt, container, false);
        setHasOptionsMenu(true);
        ImageButton addEndButton = (ImageButton) rootView.findViewById(R.id.addEndButton);
        addEndButton.setOnClickListener(parentActivity);
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

        switch (item.getItemId()) {
            case android.R.id.home: {
                //ask user if they want to save
            }
            case R.id.action_save: {
                //TODO Save hunt via parentActivity.saveHunt(hunt, mode)
            }
            default: {
                return false;
            }
        }
    }
}