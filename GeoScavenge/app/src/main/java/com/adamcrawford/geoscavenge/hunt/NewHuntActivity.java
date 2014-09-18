package com.adamcrawford.geoscavenge.hunt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.adamcrawford.geoscavenge.R;

public class NewHuntActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hunt);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new NewHuntFragment())
                    .commit();
        }
    }

    public static class NewHuntFragment extends Fragment implements View.OnClickListener {

        ImageButton addEndButton;

        public NewHuntFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_hunt, container, false);
            setHasOptionsMenu(true);
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            addEndButton = (ImageButton) rootView.findViewById(R.id.addEndButton);
            addEndButton.setOnClickListener(this);
            return rootView;
        }
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.new_hunt, menu);
            super.onCreateOptionsMenu(menu,inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.addEndButton: {

                    return true;
                }
                default: {
                    return false;
                }
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
