package com.chakibtemal.fr.modele.sqliteDb.ModelsRuning;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chakibtemal.fr.modele.sharedResources.AllDataForRunActivity;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sqliteDb.DatabaseHandler;

public class DataForNextActivityBdd {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "androidProject.db";

    private static final String TABLE_SOLAR = "Sensors";

    private static final String COL_S_ID = "id";
    private static final int NUM_COL_S_ID = 0;

    private static final String COL_S_ID_RUN_MODE = "id_run_mode";
    private static final int NUM_COL_S_ID_RUN_MODE = 1;

    private static final String COL_S_FREQUENCY = "frequency";
    private static final int NUM_COL_S_FREQUENCY = 2;

    private static final String COL_S_NAME = "name";
    private static final int NUM_COL_S_NAME  = 3;

    private static final String COL_S_TYPE = "type";
    private static final int NUM_COL_S_TYPE = 4;


    private SQLiteDatabase bdd;
    private DatabaseHandler databaseHandler;

    public DataForNextActivityBdd(Context context){
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
    public void insertActualSensor(AllDataForRunActivity allDataForRunActivity,int COL_S_ID_RUN_MODE ){
        ContentValues values = new ContentValues();
        for (DataForNextActivity dataForNextActivity : allDataForRunActivity.getDataForNextActivities()){
            values.put(this.COL_S_ID_RUN_MODE , COL_S_ID_RUN_MODE);
            values.put(COL_S_FREQUENCY ,dataForNextActivity.getFrequency());
            values.put(COL_S_NAME, dataForNextActivity.getName());
            values.put(COL_S_TYPE, dataForNextActivity.getType());
            this.bdd.insert(TABLE_SOLAR, null, values);
        }
    }
}
