package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.HuntItem;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    DynamoSearch
 * Purpose: TODO Minimum 2 sentence description
 */
public class DynamoSearch {
    static String TAG = "DynamoSearch";
    static AmazonDynamoDBClient client;
    private static DynamoDBMapper mapper;

    private static void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient(credentialsProvider);
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client);
    }

    public static HuntItem searchDynamo(CognitoCachingCredentialsProvider credentialsProvider, Integer query, String mode){

        init(credentialsProvider);
        GetItemRequest request = new GetItemRequest();
        if (mode.equals("private")) {
            request.setTableName("private_hunts");
        } else if (mode.equals("public")){
            request.setTableName("public_hunts");
        } else {
            Log.wtf(TAG, "This should never happen");
        }
        AttributeValue value = new AttributeValue();
        value.setN(query.toString());
        request.addKeyEntry("huntID", value);

        GetItemResult result = client.getItem(request);
        Log.i(TAG, result.getItem().toString());

        return mapper.load(HuntItem.class, query);
    }
}
