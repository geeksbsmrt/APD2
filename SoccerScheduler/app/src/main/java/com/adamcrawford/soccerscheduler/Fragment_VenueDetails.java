package com.adamcrawford.soccerscheduler;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    Fragment_VenueDetails
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_VenueDetails extends ListFragment {

    private String TAG = "FVD";

    public Fragment_VenueDetails(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_details, container, false);

        Bundle venueBundle = getArguments();
        VenueItem venue = (VenueItem) venueBundle.get("venue");

        MainActivity.actionBar.setTitle(venue.getVenueName());

        TextView venueName = (TextView) rootView.findViewById(R.id.fvdName);
        TextView venueAddress = (TextView) rootView.findViewById(R.id.fvdAddress);
        TextView venueLoc = (TextView) rootView.findViewById(R.id.fvdLocation);
        TextView venueFields = (TextView) rootView.findViewById(R.id.numFields);

        venueName.setText(venue.getVenueName());
        venueAddress.setText(venue.getVenueAddress());
        venueLoc.setText(venue.getVenueLocation());
        venueFields.setText(String.valueOf(venue.getVenueFields().length()));

        Log.i(TAG, venue.getVenueName());

        return rootView;
    }
}
