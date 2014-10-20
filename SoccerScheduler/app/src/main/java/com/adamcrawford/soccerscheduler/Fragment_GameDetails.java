package com.adamcrawford.soccerscheduler;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    Fragment_GameDetails
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_GameDetails extends Fragment implements View.OnClickListener {

    private String TAG = "FGD";
    private GameItem game;

    public Fragment_GameDetails(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_details, container, false);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        TextView detailsDateView = (TextView) rootView.findViewById(R.id.gameDetailsDate);
        TextView detailsHomeView = (TextView) rootView.findViewById(R.id.gameDetailsHome);
        TextView detailsAwayView = (TextView) rootView.findViewById(R.id.gameDetailsAway);
        TextView detailsFieldView = (TextView) rootView.findViewById(R.id.gameDetailsField);

        Button acceptAssignmentButton = (Button) rootView.findViewById(R.id.refAccept);
        acceptAssignmentButton.setOnClickListener(this);

        Button rejectAssignmentButton = (Button) rootView.findViewById(R.id.refReject);
        rejectAssignmentButton.setOnClickListener(this);

        Button editAssignmentsButton = (Button) rootView.findViewById(R.id.editAssignments);
        editAssignmentsButton.setOnClickListener(this);

        Button editGameButton = (Button) rootView.findViewById(R.id.editGame);
        editGameButton.setOnClickListener(this);

        Button deleteGameButton = (Button) rootView.findViewById(R.id.deleteGame);
        deleteGameButton.setOnClickListener(this);

        detailsDateView.setText(game.getGameDate().toString());
        detailsHomeView.setText(game.getHomeTeam());
        detailsAwayView.setText(game.getAwayTeam());
        detailsFieldView.setText(String.valueOf(game.getGameField()));

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = (GameItem) getArguments().get("game");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                getFragmentManager().popBackStack();
            }
            default: {
                return false;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refAccept:{
                Log.i(TAG, "Accept Clicked");
                break;
            }
            case R.id.refReject:{
                Log.i(TAG, "Reject Clicked");
                break;
            }
            case R.id.editAssignments:{
                Log.i(TAG, "Edit Assignments Clicked");
                break;
            }
            case R.id.editGame:{
                Log.i(TAG, "Edit Game Clicked");
                break;
            }
            case R.id.deleteGame:{
                Log.i(TAG, "Delete Game Clicked");
                break;
            }
            default:{
                break;
            }
        }
    }
}
