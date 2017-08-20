package com.theboringman.behonest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PlayersInfo";

    //Table Names
    public static final String TABLE_PLAYERS = "Players_Info";
    public static final String TABLE_CHECKS = "Checks";

    //Common Column Names
    public static final String KEY_ID = "ID";

    //Players_Info Column Names
    public static final String KEY_Players = "Players";
    public static final String KEY_Scores = "Scores";

    //Checks Column Names
    public static final String KEY_kidsCheck = "KidsCheck";
    public static final String KEY_teensCheck = "TeensCheck";
    public static final String KEY_adultsCheck = "AdultsCheck";
    public static final String KEY_customCheck = "CustomCheck";
    public static final String KEY_scoreLimit = "ScoreLimit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_PLAYERS + " (ID INTEGER PRIMARY KEY,PLAYERS TEXT,SCORES INTEGER)");
        db.execSQL("create table " + TABLE_CHECKS + " (ID INTEGER PRIMARY KEY,KidsCheck INTEGER,TeensCheck INTEGER,AdultsCheck INTEGER,CustomCheck INTEGER,ScoreLimit INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKS);
        onCreate(db);
    }


    //PLAYERS TABLE METHODS
    public boolean insertDataPlayers(int ID,String players,int scores){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID,ID);
        contentValues.put(KEY_Players,players);
        contentValues.put(KEY_Scores,scores);
        long result = db.insert(TABLE_PLAYERS,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllDataPlayers(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_PLAYERS, null);
    }
    public boolean updateScorePlayers(String ID, String scores){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID,ID);
        contentValues.put(KEY_Scores,scores);
        db.update(TABLE_PLAYERS,contentValues,"ID = ?",new String[]{ ID });
        return true;
    }
    public int deleteDataPlayers(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PLAYERS,"ID = ?",new String[]{ ID });
    }


    //CHECKS TABLE METHODS
    public boolean insertDataChecks(int ID,int kidsCheck, int teensCheck, int adultsCheck, int customCheck, int scoreLimit){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID,ID);
        contentValues.put(KEY_kidsCheck,kidsCheck);
        contentValues.put(KEY_teensCheck,teensCheck);
        contentValues.put(KEY_adultsCheck,adultsCheck);
        contentValues.put(KEY_customCheck,customCheck);
        contentValues.put(KEY_scoreLimit,scoreLimit);
        long result = db.insert(TABLE_CHECKS,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getAllDataChecks(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_CHECKS, null);
    }
    public int deleteDataChecks(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CHECKS,"ID = ?",new String[]{ ID });
    }
}
