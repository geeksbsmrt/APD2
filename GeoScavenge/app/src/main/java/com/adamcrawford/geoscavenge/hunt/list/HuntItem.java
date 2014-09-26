package com.adamcrawford.geoscavenge.hunt.list;

import com.adamcrawford.geoscavenge.hunt.endpoint.EndItem;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIgnore;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt
 * File:    HuntItem
 */
@DynamoDBTable(tableName = "public_hunts")
public class HuntItem implements Serializable {

    private String huntID;
    private String huntDesc;
    private String huntName;
    private String huntType;
    private Integer numEnds;
    private ArrayList<EndItem> huntEnds;

    @DynamoDBHashKey(attributeName = "huntID")
    public String getHuntID() {
        return huntID;
    }
    public void setHuntID(String huntID) {
        this.huntID = huntID;
    }

    @DynamoDBAttribute(attributeName = "huntDesc")
    public String getHuntDesc() {
        return huntDesc;
    }
    public void setHuntDesc(String huntDesc) {
        this.huntDesc = huntDesc;
    }

    @DynamoDBAttribute(attributeName = "huntName")
    public String getHuntName() {
        return huntName;
    }
    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    @DynamoDBAttribute(attributeName = "numEnds")
    public Integer getNumEnds() {
        return numEnds;
    }
    public void setNumEnds(Integer numEnds) {
        this.numEnds = numEnds;
    }

    @DynamoDBIgnore
    public ArrayList<EndItem> getHuntEnds() {
        return huntEnds;
    }
    public void setHuntEnds(ArrayList<EndItem> huntEnds) {
        this.huntEnds = huntEnds;
    }

    @DynamoDBIgnore
    public String getHuntType() {
        return huntType;
    }
    public void setHuntType(String huntType) {
        this.huntType = huntType;
    }
}