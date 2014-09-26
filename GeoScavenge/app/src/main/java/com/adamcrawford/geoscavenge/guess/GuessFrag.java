package com.adamcrawford.geoscavenge.guess;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adamcrawford.geoscavenge.R;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge
 * File:    GuessFrag
 */
public class GuessFrag extends Fragment {
    OnGuess parentActivity;
    TextView gRemain;
    LinearLayout currentContainer;
    LinearLayout pastContainer;
    TextView currentGuess;
    TextView pastGuess;

    public GuessFrag() {
    }

    public interface OnGuess extends View.OnClickListener {
        void onClick(View view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnGuess) {
            try {
                parentActivity = (OnGuess) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnGuess");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_guess, container, false);
        gRemain = (TextView) rootView.findViewById(R.id.guessesRemain);
        currentContainer = (LinearLayout) rootView.findViewById(R.id.currentContainer);
        currentGuess = (TextView) rootView.findViewById(R.id.currentGuess);
        pastContainer = (LinearLayout) rootView.findViewById(R.id.pastContainer);
        pastGuess = (TextView) rootView.findViewById(R.id.pastGuess);
        ImageButton gButton = (ImageButton) rootView.findViewById(R.id.guessButton);
        gButton.setOnClickListener(parentActivity);
        return rootView;
    }
}