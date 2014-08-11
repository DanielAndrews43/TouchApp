package com.twoshorts.sql;

/**
 * Created by danielandrews on 8/7/14.
 */
public class Score {

    private int SCORE;
    private int ID;

    public Score(){

    }

    public Score(int score){
        this.SCORE = score;
    }

    public int getScore(){
        return this.SCORE;
    }

    public void setScore(int score){
        this.SCORE = score;
    }

    public int getID(){
        return this.ID;
    }

    public void setID(int id){
        this.ID = id;
    }

}
