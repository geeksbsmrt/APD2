package com.adamcrawford.geoscavenge.data;

import android.util.Log;

import com.adamcrawford.geoscavenge.hunt.endpoint.EndItem;
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
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

    public static void putHunt(CognitoCachingCredentialsProvider credentialsProvider, HuntItem hunt, String mode, AWSCredentials creds){
        DynamoData data = new DynamoData();
        data.init(credentialsProvider, creds);
        data.sendHunt(hunt, mode);
    }

//    public static String getEndImg(CognitoCachingCredentialsProvider credentialsProvider, EndItem end, String currentEnd){
//        DynamoData data = new DynamoData();
//        data.init(credentialsProvider);
//        return data.getImg(end);
//    }

    private void init(CognitoCachingCredentialsProvider credentialsProvider) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient();
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client, credentialsProvider);
    }

    private void init(CognitoCachingCredentialsProvider credentialsProvider, AWSCredentials creds) {
        Log.i(TAG, "INIT");
        client = new AmazonDynamoDBClient();
        Region usEast = Region.getRegion(Regions.US_EAST_1);
        client.setRegion(usEast);
        mapper = new DynamoDBMapper(client, credentialsProvider);
    }

    private void sendHunt(HuntItem hunt, String mode){
        if (mode.equals("private")) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("private_hunts"));
            mapper.save(hunt, config);
        } else if (mode.equals("public")){
            Log.e(TAG, hunt.getHuntDesc());
            mapper.save(hunt);
        } else {
            Log.wtf(TAG, "This should never happen");
        }
        int index = 0;
        for (EndItem item : hunt.getHuntEnds()){
            item.setHuntID(hunt.getHuntID());
            item.setEndOrder(String.valueOf(index));
            sendEnd(item, hunt.getHuntType());
            index++;
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
        ArrayList<EndItem> ends = getEnds(hunt.getHuntID(), hunt.getHuntType());
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

    private ArrayList<EndItem> getEnds(String huntID, String mode) {
        ArrayList<EndItem> ends = new ArrayList<EndItem>();
        try {
            EndItem endKey = new EndItem();
            endKey.setHuntID(huntID);
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(
                    DynamoDBMapperConfig.SaveBehavior.UPDATE,
                    DynamoDBMapperConfig.ConsistentReads.CONSISTENT,
                    DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("priv_hunt_ends"),
                    DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING);
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

    private void sendEnd(EndItem end, String mode){
//        if (end.getEndImgStr() == null) {
//        } else {
//            end.setEndImg(mapper.createS3Link("huntimages", end.getHuntID() + "_" + end.getEndOrder()));
//        }
        if (mode.equals("private")) {
            DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement("priv_hunt_ends"));
            mapper.save(end, config);
        } else if (mode.equals("public")){
            mapper.save(end);
        } else {
            Log.wtf(TAG, "This should never happen");
        }
//        if (!end.getEndImgStr().isEmpty()) {
//            end.getEndImg().uploadFrom(new File(end.getEndImgStr()));
//        }
    }

//    private String getImg(EndItem end){
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        String imageFileName = "Geo_" + end.getHuntID() + "_" + end.getEndOrder();
//        File img = new File(storageDir, imageFileName + ".jpg");
//        end.getEndImg().downloadTo(img);
//        return img.getPath();
//    }
}
