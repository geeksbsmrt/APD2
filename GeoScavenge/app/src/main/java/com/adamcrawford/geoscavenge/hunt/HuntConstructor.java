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
    public double huntLat;
    public double huntLon;
    public String  huntImg;
    public String huntEndDesc;
    public int huntID;
    public int huntGuesses;

    public HuntConstructor (JSONObject object) {

        Log.i(TAG, object.toString());

        try {
            this.huntID = object.getInt("id");
            this.huntName = object.getString("name");
            this.huntDesc = object.getString("desc");
            this.huntLat = object.getDouble("lat");
            this.huntLon = object.getDouble("lon");
            this.huntGuesses = object.getInt("guesses");
            this.huntEndDesc = object.getString("endDesc");
            this.huntImg = object.getString("imgPath");
            Log.i(TAG, huntImg);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
