package com.adamcrawford.geoscavenge;

import android.app.ListFragment;
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

    private ArrayList<HuntConstructor> huntList;

    public ListFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        huntList = new ArrayList<HuntConstructor>();

        //This will be replaced by Network Data in future release
        try {

            Log.i(TAG, "building data");
            JSONArray huntArray = new JSONArray();
            JSONObject hunt1 = new JSONObject();
            JSONObject hunt2 = new JSONObject();
            hunt1.put("id", 1);
            hunt1.put("name","Hunt1");
            hunt1.put("desc", "Hunt for buried treasure");
            hunt1.put("lat", 28.596597);
            hunt1.put("lon", -81.301316);
            hunt1.put("guesses", 50);
            hunt1.put("endDesc", "Get your degree and you will have the opportunity for endless wealth!");
            //This may not be correct.  It will be tested for Milestone 2
            hunt1.put("imgPath", String.valueOf(R.drawable.fsu));
            hunt2.put("id", 2);
            hunt2.put("name", "Hunt2");
            hunt2.put("desc", "Find the love of your life");
            hunt2.put("lat", 28.419791);
            hunt2.put("lon", 81.581187);
            hunt2.put("guesses", 100);
            hunt2.put("endDesc", "Dance with the Princes and Princesses, watch stunning fireworks, become entranced with that special someone.");
            //This may not be correct.  It will be tested for Milestone 2
            hunt2.put("imgPath", String.valueOf(R.drawable.castle));
            huntArray.put(hunt1).put(hunt2);
            huntJSON.put("hunts", huntArray);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
