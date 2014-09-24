package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.list.HuntItem;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    DynamoData
 */
public class DynamoData {
    static String TAG = "DynamoData";
    static AmazonDynamoDBClient client;
    private DynamoDBMapper mapper;
    static JSONArray huntArray = new JSONArray();

    public static JSONArray getData(CognitoCachingCredentialsProvider credentialsProvider) throws IOException {
        DynamoData data = new DynamoData();
        data.init(credentialsProvider);
        data.getAllRows();
        return huntArray;
    }

    private void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient(credentialsProvider);
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client);
    }

    private void getAllRows() {
        try {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING);
            DynamoDBScanExpression scanEx = new DynamoDBScanExpression();
            PaginatedScanList result = mapper.scan(HuntItem.class, scanEx, config);
            for (Object item : result){
                huntArray.put(item);
            }
        } catch (AmazonServiceException e) {
            Log.e(TAG, e.getMessage());
        } catch (AmazonClientException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
