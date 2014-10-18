package com.adamcrawford.soccerscheduler;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:  Adam Crawford
 * Project: Soccer Scheduler
 * Package: com.adamcrawford.soccerscheduler
 * File:    GameItem
 * Purpose: TODO Minimum 2 sentence description
 */
@ParseClassName("Game")
public class GameItem extends ParseObject implements Serializable {

    public static String HOMETEAM = "homeTeam";
    public static String AWAYTEAM = "awayTeam";
    public static String GAMEDATE = "gameDate";
    public static String GAMEFIELD = "gameField";
    public static String GAMEVENUE = "gameVenue";
    public static String AGEGROUP = "ageGroup";

    public String getHomeTeam(){
        return getString(HOMETEAM);
    }
    public void setHomeTeam(String home){
        put(HOMETEAM, home);
    }

    public String getAwayTeam(){
        return getString(AWAYTEAM);
    }
    public void setAwayTeam(String away){
        put(AWAYTEAM, away);
    }

    public Date getGameDate(){
        return getDate(GAMEDATE);
    }
    public void setVenueAddress(Date gDate){
        put(GAMEDATE, gDate);
    }

    public Number getGameField(){
        return getNumber(GAMEFIELD);
    }
    public void setGameField(Number field){
        put(GAMEFIELD, field);
    }

    public String getGameVenue(){
        return getString(GAMEVENUE);
    }
    public void setGameVenue(String venue){
        put(GAMEVENUE, venue);
    }

    public String getAgeGroup(){
        return getString(AGEGROUP);
    }
    public void setAgeGroup(String group){
        put(AGEGROUP, group);
    }

    public static ParseQuery<GameItem> getQuery(){
        return ParseQuery.getQuery(GameItem.class);
    }
}
