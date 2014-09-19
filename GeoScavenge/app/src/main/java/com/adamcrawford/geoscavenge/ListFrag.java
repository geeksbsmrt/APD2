package com.adamcrawford.geoscavenge;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.adamcrawford.geoscavenge.hunt.HuntAdapter;
import com.adamcrawford.geoscavenge.hunt.HuntConstructor;
import com.adamcrawford.geoscavenge.hunt.NewHuntActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge
 * File:    ListFrag
 * Purpose: TODO Minimum 2 sentence description
 */
public class ListFrag extends ListFragment {

    String TAG = "LF";

    JSONObject huntJSON = new JSONObject();

    private OnHuntSelected parentActivity;

    public ListFrag() {
    }

    public interface OnHuntSelected {
        void onHuntSelected(HuntConstructor hunt);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnHuntSelected) {
            try {
                parentActivity = (OnHuntSelected) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnToonSelected");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<HuntConstructor> huntList = new ArrayList<HuntConstructor>();

        //This will be replaced by Network Data in future release
        try {
            Log.i(TAG, "building data");
            huntJSON.put("hunts", MainActivity.huntArray);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            Log.i(TAG, "Building list");
            Log.i(TAG, huntJSON.toString());
            JSONArray dataArray = huntJSON.getJSONArray("hunts");

            for (int i = 0, j = dataArray.length(); i < j; i++) {
                JSONObject hunt = (JSONObject) dataArray.get(i);
                HuntConstructor hc = new HuntConstructor(hunt);
                Log.i(TAG, hc.toString());
                huntList.add(hc);
                Log.i(TAG, hc.toString());
            }

            Log.i(TAG, huntList.toString());

            Log.i(TAG, "building adapter");
            HuntAdapter adapter = new HuntAdapter(getActivity(), R.layout.item_hunt, huntList);
            adapter.notifyDataSetChanged();
            Log.i(TAG,"Setting Adapter");
            setListAdapter(adapter);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        return listView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add: {
                //To Add Activity
                Log.i(TAG, "Launching Add Activity");
                Intent nIntent = new Intent(getActivity(), NewHuntActivity.class);
                startActivity(nIntent);
                return true;
            }

            case R.id.action_search: {
                //search
                Log.i(TAG, "Search Action Item pressed");
                Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.SEARCH);
                dialog.show(getFragmentManager(), "search");
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onListItemClick(ListView list, View view, int i, long id) {
        HuntConstructor hunt = (HuntConstructor) list.getItemAtPosition(i);
        parentActivity.onHuntSelected(hunt);
    }
}
