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
 * File:    DynamoSearch
 */
public class DynamoSearch {
    static String TAG = "DynamoSearch";
    static AmazonDynamoDBClient client;

    private static void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient(credentialsProvider);
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
    }

    public static HuntItem searchDynamo(CognitoCachingCredentialsProvider credentialsProvider, String query, String mode){
        DynamoDBMapper mapper = new DynamoDBMapper(client);;
        init(credentialsProvider);
        if (mode.equals("private")) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("private_hunts"));
            return mapper.load(HuntItem.class, query, config);
        } else if (mode.equals("public")){
            return mapper.load(HuntItem.class, query);
        } else {
            Log.wtf(TAG, "This should never happen");
            return null;
        }
    }
}
