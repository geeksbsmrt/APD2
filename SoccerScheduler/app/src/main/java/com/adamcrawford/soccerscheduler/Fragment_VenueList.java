package com.adamcrawford.soccerscheduler;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    Fragment_VenueList
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_VenueList extends ListFragment {

    private String TAG = "FVL";

    public Fragment_VenueList(){}

    ParseQueryAdapter<VenueItem> venueAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_venue_list, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(false);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQueryAdapter.QueryFactory<VenueItem> factory = new ParseQueryAdapter.QueryFactory<VenueItem>() {
            @Override
            public ParseQuery<VenueItem> create() {
                ParseQuery<VenueItem> query = VenueItem.getQuery();
                query.orderByAscending(VenueItem.VENUENAME);
                return query;
            }
        };

        venueAdapter = new ParseQueryAdapter<VenueItem>(getActivity(), factory) {
            @Override
            public View getItemView(VenueItem venue, View view, ViewGroup parent){
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.item_venue, null);
                }
                TextView nameView = (TextView) view.findViewById(R.id.venueListName);
                TextView locView = (TextView) view.findViewById(R.id.venueListLoc);
                nameView.setText(venue.getVenueName());
                locView.setText(venue.getVenueLocation());
                return view;
            }
        };
        setListAdapter(venueAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        VenueItem venue = venueAdapter.getItem(position);
        Fragment_VenueDetails fvd = new Fragment_VenueDetails();
        Bundle venueBundle = new Bundle();
        venueBundle.putSerializable("venue", venue);
        fvd.setArguments(venueBundle);
        getFragmentManager().beginTransaction().replace(R.id.container, fvd).addToBackStack(null).commit();
    }
}
