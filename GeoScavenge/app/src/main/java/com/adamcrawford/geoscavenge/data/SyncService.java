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
import com.adamcrawford.geoscavenge.hunt.list.HuntItem;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import org.json.JSONArray;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.data
 * File:    SyncService
 */
public class SyncService extends IntentService {
    private String TAG = "SyncService";
    private String mode;
    public static SyncType type;
    public enum SyncType {
        LISTDATA,
        SEARCH,
        PUTITEM,
        PUTIMG,
        GETIMG
    }

    public static Context sContext;
    JSONArray huntArray;

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
        type = (SyncType) extras.get("type");
        if (extras.containsKey("mode")){
            mode = extras.getString("mode");
        }
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
                    msg.arg1 = MainActivity.RESULT_OK;
                    msg.arg2 = 0;
                    huntArray = DynamoData.getData(credentialsProvider);
                    msg.obj = huntArray;
                    break;
                }
                case SEARCH: {
                    String query = extras.getString("query");
                    msg.arg1 = MainActivity.RESULT_OK;
                    msg.arg2 = 1;
                    msg.obj = DynamoSearch.searchDynamo(credentialsProvider, query, mode);
                    break;
                }
                case PUTITEM: {
                    DynamoPut.putItem(credentialsProvider, (HuntItem) extras.get("hunt"), mode);
                    msg.arg1 = MainActivity.RESULT_OK;
                    msg.arg2 = 2;
                    break;
                }
                case PUTIMG: {
                    //TODO S3 Img upload
                    break;
                }
                case GETIMG: {
                    //TODO S3 Img get
                    break;
                }
                default: {
                    break;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}