package com.adamcrawford.soccerscheduler;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    VenueItem
 * Purpose: TODO Minimum 2 sentence description
 */
@ParseClassName("Venue")
public class VenueItem extends ParseObject implements Serializable {
    public static String VENUELOCATION = "venueLocation";
    public static String VENUEADDRESS = "venueAddress";
    public static String VENUEFIELDS = "venueFields";
    public static String VENUENAME = "venueName";

    public String getVenueName(){
        return getString(VENUENAME);
    }
    public void setVenueName(String name){
        put(VENUENAME, name);
    }

    public String getVenueLocation(){
        return getString(VENUELOCATION);
    }
    public void setVenueLocation(String location){
        put(VENUELOCATION, location);
    }

    public String getVenueAddress(){
        return getString(VENUEADDRESS);
    }
    public void setVenueAddress(String address){
        put(VENUEADDRESS, address);
    }

    public JSONArray getVenueFields(){
        return getJSONArray(VENUEFIELDS);
    }
    public void setVenueFields(JSONArray fields){
        put(VENUEFIELDS, fields);
    }

    public static ParseQuery<VenueItem> getQuery(){
        return ParseQuery.getQuery(VenueItem.class);
    }
}
