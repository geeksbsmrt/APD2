package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.endpoint.EndItem;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<HuntItem> getData(CognitoCachingCredentialsProvider credentialsProvider) throws IOException {
        DynamoData data = new DynamoData();
        data.init(credentialsProvider);
        return data.getAllHunts();
    }

    public static HuntItem searchDynamo(CognitoCachingCredentialsProvider credentialsProvider, String query, String mode){
        DynamoData data = new DynamoData();
        data.init(credentialsProvider);
        return data.searchHunts(query, mode);
    }

    public static void putItem(CognitoCachingCredentialsProvider credentialsProvider, HuntItem hunt, String mode){
        DynamoData data = new DynamoData();
        data.init(credentialsProvider);
        data.sendItem(hunt, mode);
    }

    private void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient(credentialsProvider);
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client);
    }

    private void sendItem(HuntItem hunt, String mode){
        if (mode.equals("private")) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("private_hunts"));
            mapper.save(hunt, config);
        } else if (mode.equals("public")){
            mapper.save(hunt);
        } else {
            Log.wtf(TAG, "This should never happen");
        }
    }

    private ArrayList<HuntItem> getAllHunts() {
        ArrayList<HuntItem> array = new ArrayList<HuntItem>();
        try {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING);
            DynamoDBScanExpression scanEx = new DynamoDBScanExpression();
            PaginatedScanList result = mapper.scan(HuntItem.class, scanEx, config);
            for (Object item : result){
                HuntItem hunt = (HuntItem) item;
                ArrayList<EndItem> ends = getEnds(hunt.getHuntID());
                hunt.setHuntEnds(ends);
                hunt.setNumEnds(ends.size());
                hunt.setHuntType("public");
                array.add(hunt);
            }
            return array;
        } catch (AmazonServiceException e) {
            Log.e(TAG, e.getMessage());
        } catch (AmazonClientException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    private HuntItem searchHunts(String query, String mode){
        HuntItem hunt;
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        if (mode.equals("private")) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("private_hunts"));
            hunt = mapper.load(HuntItem.class, query, config);
            hunt.setHuntType("private");
        } else if (mode.equals("public")){
            hunt = mapper.load(HuntItem.class, query);
            hunt.setHuntType("public");
        } else {
            Log.wtf(TAG, "This should never happen");
            return null;
        }
        ArrayList<EndItem> ends = getEnds(hunt.getHuntID());
        hunt.setHuntEnds(ends);
        hunt.setNumEnds(ends.size());
        return hunt;
    }

    private ArrayList<EndItem> getEnds(String huntID) {
        ArrayList<EndItem> ends = new ArrayList<EndItem>();
        try {
            EndItem endKey = new EndItem();
            endKey.setHuntID(huntID);
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING);
            DynamoDBQueryExpression<EndItem> queryEx = new DynamoDBQueryExpression<EndItem>().withHashKeyValues(endKey);
            List<EndItem> result = mapper.query(EndItem.class, queryEx, config);
            for (Object item : result){
                EndItem end = (EndItem) item;
                ends.add(end);
            }
            return ends;
        } catch (AmazonServiceException e) {
            Log.e(TAG, e.getMessage());
        } catch (AmazonClientException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
