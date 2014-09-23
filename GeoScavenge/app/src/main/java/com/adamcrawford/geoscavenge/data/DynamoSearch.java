package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.HuntItem;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    DynamoSearch
 * Purpose: TODO Minimum 2 sentence description
 */
public class DynamoSearch {
    static String TAG = "Dynamo";
    static AmazonDynamoDBClient client;
    private DynamoDBMapper mapper;

    private void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient(credentialsProvider);
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client);
    }

    public static HuntItem searchDynamo(CognitoCachingCredentialsProvider credentialsProvider){

        return null;
    }
}
