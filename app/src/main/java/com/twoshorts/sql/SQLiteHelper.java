package com.twoshorts.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "touch";

    private static final String TABLE_NAME = "scores";
    private static final String KEY_ID = "_id";
    private static final String KEY_SCORE = "score";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_SCORE + " INTEGER" + ")";
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addScore(Score score){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score.getScore());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Integer> getAllScores(){
        List<Integer> scores = new ArrayList<Integer>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                scores.add(Integer.parseInt(cursor.getString(1)));
            } while(cursor.moveToNext());
        }

        return scores;
    }

    public int findHighScore(){
        List<Integer> scores = getAllScores();

        int highscore = 0;
        for(int score : scores){
            if(score > highscore){
                highscore = score;
            }
        }
        return highscore;
    }
}
