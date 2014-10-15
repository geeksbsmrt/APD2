package com.adamcrawford.soccerscheduler;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ui.ParseLoginBuilder;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    Fragment_MainActivity
 * Purpose: TODO Minimum 2 sentence description
 */
public class Fragment_MainActivity extends Fragment {

    public Fragment_MainActivity(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button tempEnter = (Button) rootView.findViewById(R.id.tempEnter);
        Button tempLogin = (Button) rootView.findViewById(R.id.tempLogin);

        tempEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_VenueList fvl = new Fragment_VenueList();
                getFragmentManager().beginTransaction().replace(R.id.container, fvl).addToBackStack(null).commit();
            }
        });

        tempLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseLoginBuilder builder = new ParseLoginBuilder(getActivity());
                startActivityForResult(builder.build(), 0);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
