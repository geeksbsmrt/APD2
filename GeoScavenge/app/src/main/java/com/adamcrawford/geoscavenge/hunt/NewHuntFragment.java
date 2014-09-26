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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adamcrawford.geoscavenge.R;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt
 * File:    NewHuntFragment
 */
public class NewHuntFragment extends Fragment {
    OnNewHunt parentActivity;
    EditText huntNameView;
    EditText huntDescView;
    CheckBox huntMode;
    LinearLayout numEndsContainer;
    TextView numEnds;

    public NewHuntFragment() {
    }

    public interface OnNewHunt extends View.OnClickListener {
        @Override
        void onClick(View view);

        void saveHunt();
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
        huntNameView = (EditText) rootView.findViewById(R.id.newName);
        huntDescView = (EditText) rootView.findViewById(R.id.newDesc);
        huntMode = (CheckBox) rootView.findViewById(R.id.newPrivate);
        numEndsContainer = (LinearLayout) rootView.findViewById(R.id.addEndContainer);
        numEnds = (TextView) rootView.findViewById(R.id.addNumEnds);
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
                parentActivity.saveHunt();
            }
            default: {
                return false;
            }
        }
    }
}