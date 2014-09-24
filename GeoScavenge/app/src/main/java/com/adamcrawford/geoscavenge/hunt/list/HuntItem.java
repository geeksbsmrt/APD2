package com.adamcrawford.geoscavenge.hunt.list;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;

/**
 * Author:  Adam Crawford
 * Project: GeoScavenge
 * Package: com.adamcrawford.geoscavenge.hunt
 * File:    HuntItem
 */
@DynamoDBTable(tableName = "public_hunts")
public class HuntItem implements Serializable {

    private Integer huntID;
    private String endDesc;
    private Integer guesses;
    private String huntCity;
    private String huntDesc;
    private double huntLat;
    private double huntLon;
    private String huntName;
    private String huntState;
    private String imgPath;

    @DynamoDBHashKey(attributeName = "huntID")
    public Integer getHuntID() {
        return huntID;
    }
    public void setHuntID(Integer huntID) {
        this.huntID = huntID;
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

    @DynamoDBAttribute(attributeName = "huntCity")
    public String getHuntCity() {
        return huntCity;
    }
    public void setHuntCity(String huntCity) {
        this.huntCity = huntCity;
    }

    @DynamoDBAttribute(attributeName = "huntDesc")
    public String getHuntDesc() {
        return huntDesc;
    }
    public void setHuntDesc(String huntDesc) {
        this.huntDesc = huntDesc;
    }

    @DynamoDBAttribute(attributeName = "huntLat")
    public double getHuntLat() {
        return huntLat;
    }
    public void setHuntLat(double huntLat) {
        this.huntLat = huntLat;
    }

    @DynamoDBAttribute(attributeName = "huntLon")
    public double getHuntLon() {
        return huntLon;
    }
    public void setHuntLon(double huntLon) {
        this.huntLon = huntLon;
    }

    @DynamoDBAttribute(attributeName = "huntName")
    public String getHuntName() {
        return huntName;
    }
    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    @DynamoDBAttribute(attributeName = "huntState")
    public String getHuntState() {
        return huntState;
    }
    public void setHuntState(String huntState) {
        this.huntState = huntState;
    }

    @DynamoDBAttribute(attributeName = "imgPath")
    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}