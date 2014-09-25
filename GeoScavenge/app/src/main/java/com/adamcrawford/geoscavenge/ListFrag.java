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

import com.adamcrawford.geoscavenge.hunt.NewHuntActivity;
import com.adamcrawford.geoscavenge.hunt.list.HuntAdapter;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge
 * File:    ListFrag
 */
public class ListFrag extends ListFragment {

    static String TAG = "LF";
    private OnHuntSelected parentActivity;

    public ListFrag() {
    }

    public interface OnHuntSelected {
        void onHuntSelected(HuntItem hunt);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnHuntSelected) {
            try {
                parentActivity = (OnHuntSelected) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnHuntSelected");
            }
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View listView = inflater.inflate(R.layout.activity_main, container, false);
        setHasOptionsMenu(true);
        return listView;
    }

    public void newData(ArrayList<HuntItem> returned){
        if (returned != null ){
            Log.i(TAG, "building adapter");
            HuntAdapter adapter = new HuntAdapter(MainActivity.sContext, R.layout.item_hunt, returned);
            adapter.notifyDataSetChanged();
            Log.i(TAG,"Setting Adapter");
            setListAdapter(adapter);
        }
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
        HuntItem hunt = (HuntItem) list.getItemAtPosition(i);
        parentActivity.onHuntSelected(hunt);
    }
}
