package com.adamcrawford.geoscavenge.hunt.endpoint;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt.endpoint
 * File:    EndItem
 */
@DynamoDBTable(tableName = "pub_hunt_ends")
public class EndItem implements Serializable {

    private String huntID;
    private String endOrder;
    private String endDesc;
    private Integer guesses;
    private String endCity;
    private double endLat;
    private double endLon;
//    private String endImgStr;
    private String endState;
//    private S3Link endImg;

    @DynamoDBHashKey(attributeName = "huntID")
    public String getHuntID() {
        return huntID;
    }
    public void setHuntID(String huntID) {
        this.huntID = huntID;
    }

    @DynamoDBAttribute(attributeName = "endOrder")
    @DynamoDBRangeKey
    public String getEndOrder() {
        return endOrder;
    }
    public void setEndOrder(String endOrder) {
        this.endOrder = endOrder;
    }

    @DynamoDBAttribute(attributeName = "endDesc")
    public String getEndDesc() {
        return endDesc;
    }
    public void setEndDesc(String endDesc) {
        this.endDesc = endDesc;
    }

    @DynamoDBAttribute(attributeName = "guesses")
    public Integer getGuesses() {
        return guesses;
    }
    public void setGuesses(Integer guesses) {
        this.guesses = guesses;
    }

    @DynamoDBAttribute(attributeName = "endCity")
    public String getEndCity() {
        return endCity;
    }
    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    @DynamoDBAttribute(attributeName = "endLat")
    public double getEndLat() {
        return endLat;
    }
    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    @DynamoDBAttribute(attributeName = "endLon")
    public double getEndLon() {
        return endLon;
    }
    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    @DynamoDBAttribute(attributeName = "endState")
    public String getEndState() {
        return endState;
    }
    public void setEndState(String endState) {
        this.endState = endState;
    }

//    @DynamoDBIgnore
//    public S3Link getEndImg() {
//        return endImg;
//    }
//    public void setEndImg(S3Link endImg) {
//        this.endImg = endImg;
//    }

//    @DynamoDBIgnore
//    public String getEndImgStr() {
//        return endImgStr;
//    }
//    public void setEndImgStr(String endImgStr) {
//        this.endImgStr = endImgStr;
//    }
}
