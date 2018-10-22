package com.chakibtemal.fr.modele.sqliteDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SolarBdd {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "androidProject.db";

    private static final String TABLE_SOLAR = "Solar";

    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;

    private static final String COL_NAME = "name";
    private static final int NUM_COL_NAME = 1;

    private static final String COL_VALUEX = "valueX";
    private static final int NUM_COL_VALUEX = 2;

    private static final String COL_VALUEY = "valueY";
    private static final int NUM_COL_VALUEY = 3;

    private static final String COL_VALUEZ = "valueZ";
    private static final int NUM_COL_VALUEZ = 4;

    private static final String COL_TIME = "time";
    private static final int NUM_COL_TIME = 5;

    private SQLiteDatabase bdd;
    private DatabaseHandler databaseHandler;

    public SolarBdd(Context context){
        databaseHandler = new DatabaseHandler(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = databaseHandler.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBdd(){
        return bdd;
    }
    public void insertLivre(Solar sensor){
        ContentValues values = new ContentValues();
        values.put(COL_NAME, sensor.getName());
        values.put(COL_VALUEX, sensor.getValueX());
        values.put(COL_VALUEY, sensor.getValueY());
        values.put(COL_VALUEZ, sensor.getValueZ());
        values.put(COL_TIME, sensor.getTime());

        this.bdd.insert(TABLE_SOLAR, null, values);
    }


}
