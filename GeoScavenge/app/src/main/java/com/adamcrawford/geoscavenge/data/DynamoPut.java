package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.list.HuntItem;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    DynamoPut
 * Purpose: TODO Minimum 2 sentence description
 */
public class DynamoPut {
    static String TAG = "DynamoPut";
    static AmazonDynamoDBClient client;
    static DynamoDBMapper mapper;

    private static void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient(credentialsProvider);
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client);
    }

    public static void putItem(CognitoCachingCredentialsProvider credentialsProvider, HuntItem hunt, String mode){
        init(credentialsProvider);
        if (mode.equals("private")) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("private_hunts"));
            mapper.save(hunt, config);
        } else if (mode.equals("public")){
            mapper.save(hunt);
        } else {
            Log.wtf(TAG, "This should never happen");
        }
    }
}
