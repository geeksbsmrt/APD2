package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.HuntItem;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    DynamoData
 * Purpose: TODO Minimum 2 sentence description
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

    private HuntItem load(int id) {
        DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT);
        return mapper.load(HuntItem.class, id, config);
    }

    private void getAllRows() {

        ScanRequest scanRequest = new ScanRequest().withTableName("public_hunts");

        try {
            ScanResult scanResponse = client.scan(scanRequest);

            Log.i(TAG, scanResponse.getCount().toString());

            for (int i = 0, j = scanResponse.getCount(); i < j ; i++) {
                HuntItem hunt = load(i);
                Log.e(TAG, hunt.getHuntID().toString());
                Log.e(TAG, hunt.getHuntName());
                huntArray.put(hunt);
            }

        } catch (AmazonServiceException e) {
            Log.e(TAG, e.getMessage());
        } catch (AmazonClientException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
