package com.adamcrawford.geoscavenge.hunt;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt
 * File:    HuntConstructor
 * Purpose: TODO Minimum 2 sentence description
 */
public class HuntConstructor implements Serializable {

    String TAG = "HC";

    public String huntName;
    public String huntDesc;

    public HuntConstructor (JSONObject object) {

        Log.i(TAG, object.toString());

        try {
            this.huntName = object.getString("name");
            this.huntDesc = object.getString("desc");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
