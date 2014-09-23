package com.adamcrawford.geoscavenge.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.hunt.HuntItem;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import org.json.JSONArray;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    SyncService
 * Purpose: TODO Minimum 2 sentence description
 */
public class SyncService extends IntentService {
    private String TAG = "SyncService";
    public static SearchType type;
    public enum SearchType {
        LISTDATA,
        SEARCH
    }

    public static Context sContext;
    JSONArray huntArray;
    HuntItem hunt;

    public SyncService() {
        super("SyncService");
    }

    public Context getContext() {
        sContext = getApplicationContext();
        return getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "handling Intent");
        Bundle extras = intent.getExtras();
        Messenger msgr = (Messenger) extras.get("msgr");
        type = (SearchType) extras.get("type");
        Message msg = Message.obtain();

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
            getContext(), // get the context for the current activity
            "220324785837", // your AWS Account id
            "us-east-1:761249c4-51ce-4387-8c9e-faf0063c4a25", // your identity pool id
            "arn:aws:iam::220324785837:role/Cognito_publicPoolUnauth_DefaultRole", // an unauthenticated role ARN
            "arn:aws:iam::220324785837:role/Cognito_publicPoolAuth_DefaultRole",// an authenticated role ARN
            Regions.US_EAST_1 //Region
        );

        try {
            Log.i(TAG, "Getting Dynamo Data");

            switch (type) {
                case LISTDATA: {
                    huntArray = DynamoData.getData(credentialsProvider);
                    msg.arg1 = MainActivity.RESULT_OK;
                    msg.arg2 = 0;
                    msg.obj = huntArray;
                }
                case SEARCH: {
                    Integer query = extras.getInt("query");
                    hunt = DynamoSearch.searchDynamo(credentialsProvider);
                    msg.arg1 = MainActivity.RESULT_OK;
                    msg.arg2 = 1;
                    msg.obj = hunt;
                }
                default: {
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Catching error");
            e.printStackTrace();
        }

        try {
            msgr.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}