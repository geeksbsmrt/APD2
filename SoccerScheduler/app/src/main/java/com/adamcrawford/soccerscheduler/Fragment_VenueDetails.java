package com.adamcrawford.soccerscheduler;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Date;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    Fragment_VenueDetails
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_VenueDetails extends ListFragment {

    private String TAG = "FVD";
    ParseQueryAdapter<GameItem> gameAdapter;
    Bundle venueBundle;
    VenueItem venue;

    public Fragment_VenueDetails(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_details, container, false);

        MainActivity.actionBar.setTitle(venue.getVenueName());

        TextView venueAddress = (TextView) rootView.findViewById(R.id.fvdAddress);
        TextView venueLoc = (TextView) rootView.findViewById(R.id.fvdLocation);
        TextView venueFields = (TextView) rootView.findViewById(R.id.numFields);

        venueAddress.setText(venue.getVenueAddress());
        venueLoc.setText(venue.getVenueLocation());
        venueFields.setText(String.valueOf(venue.getVenueFields().length()));

        Log.i(TAG, venue.getVenueName());

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        venueBundle = getArguments();
        venue = (VenueItem) venueBundle.get("venue");

        ParseQueryAdapter.QueryFactory<GameItem> factory = new ParseQueryAdapter.QueryFactory<GameItem>() {
            @Override
            public ParseQuery<GameItem> create() {
                ParseQuery<GameItem> query = GameItem.getQuery();
                query.whereContains(GameItem.GAMEVENUE, venue.getObjectId());
                query.whereGreaterThan(GameItem.GAMEDATE, new Date());
                query.orderByAscending(GameItem.GAMEDATE);
                return query;
            }
        };

        gameAdapter = new ParseQueryAdapter<GameItem>(getActivity(), factory) {
            @Override
            public View getItemView(GameItem game, View view, ViewGroup parent){
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.item_game, null);
                }

                TextView homeTeamView = (TextView) view.findViewById(R.id.homeTeam);
                TextView awayTeamView = (TextView) view.findViewById(R.id.awayTeam);
                TextView ageGroup = (TextView) view.findViewById(R.id.ageGroup);
                TextView gameDate = (TextView) view.findViewById(R.id.gameDate);

                homeTeamView.setText(game.getHomeTeam());
                awayTeamView.setText(game.getAwayTeam());
                ageGroup.setText(game.getAgeGroup());
                gameDate.setText(game.getGameDate().toString());

                return view;
            }
        };
        setListAdapter(gameAdapter);
    }
}
