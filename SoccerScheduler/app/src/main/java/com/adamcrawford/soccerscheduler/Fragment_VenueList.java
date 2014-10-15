package com.adamcrawford.soccerscheduler;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    Fragment_VenueList
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_VenueList extends ListFragment {

    public Fragment_VenueList(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_list, container, false);

        Button tempVenue = (Button) rootView.findViewById(R.id.tempVenue);
        tempVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_VenueDetails fvd = new Fragment_VenueDetails();
                getFragmentManager().beginTransaction().replace(R.id.container, fvd).addToBackStack(null).commit();
            }
        });

        return rootView;
    }
}
