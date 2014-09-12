package com.adamcrawford.geoscavenge;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
            hunt1.put("name","Hunt1");
            hunt1.put("desc", "Hunt for buried treasure");
            hunt2.put("name", "Hunt2");
            hunt2.put("desc", "Find the love of your life");
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
    }}
