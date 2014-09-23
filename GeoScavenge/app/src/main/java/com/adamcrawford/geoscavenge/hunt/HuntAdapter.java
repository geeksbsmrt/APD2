package com.adamcrawford.geoscavenge.hunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adamcrawford.geoscavenge.R;

import java.util.ArrayList;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt
 * File:    HuntAdapter
 * Purpose: TODO Minimum 2 sentence description
 */
public class HuntAdapter extends ArrayAdapter<HuntItem> {

    private Context context;
    private ArrayList<HuntItem> objects;

    public HuntAdapter(Context context, int resource, ArrayList<HuntItem> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        HuntItem hunt = objects.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hunt, null);
            holder = new ViewHolder();
            holder.huntNameView = (TextView) convertView.findViewById(R.id.huntName);
            holder.huntDescView = (TextView) convertView.findViewById(R.id.huntDesc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.huntNameView.setText(hunt.getHuntName());
        holder.huntDescView.setText(hunt.getHuntDesc());

        return convertView;
    }

    static class ViewHolder {
        TextView huntNameView;
        TextView huntDescView;
    }
}
