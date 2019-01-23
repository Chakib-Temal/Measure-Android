package com.chakibtemal.fr.modele.sqliteDb.ModelsRuning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chakibtemal.fr.modele.sharedResources.AllDataForRunActivity;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sharedResources.DataModels;
import com.chakibtemal.fr.modele.sqliteDb.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

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


    public List<DataModels> getAllDatas(RunModeBdd runModeBdd){
        List<DataModels> data = new ArrayList<DataModels>();

        Cursor cursor = bdd.rawQuery("SELECT name, type, frequency, id_run_mode , nameMode, necessaryIndex FROM Sensors INNER JOIN RunMode ON RunMode.id = id_run_mode " , null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            //DataModels data1 = (DataModels) cursorToSensors(cursor, runModeBdd);
            //data.add(data1);
            cursor.moveToNext();
        }
        return data;

    }

    private DataModels cursorToSensors(Cursor cursor, RunModeBdd runModeBdd) {
        DataModels actualData = new DataModels();

        actualData.setName(cursor.getString(0));
        actualData.setType(cursor.getInt(1));
        actualData.setFrequency(cursor.getDouble(2));
        actualData.setId_runMode(cursor.getInt(3));

        Cursor c = bdd.rawQuery("SELECT nameMode, necessaryIndex FROM RunMode WHERE id = ? " , new String[]{Integer.toString(actualData.getId_runMode())});
        c.moveToFirst();
        while (!c.isAfterLast()) {
            actualData.setNameMode(c.getString(0));
            actualData.setNecessaryIndex(c.getInt(1));
            c.moveToNext();
        }
        System.out.println();
        return actualData;
    }


    public int deleteSensorsOfModel(AllDataForRunActivity dataForRunActivity){
        int id_Run_mode = 0;
        for (DataForNextActivity sensor : dataForRunActivity.getDataForNextActivities()){
            bdd.delete(DatabaseHandler.TABLE_SENSORS, DatabaseHandler.COL_S_ID_RUN_MODE
                    + " = " + sensor.getId_runMode(), null);
            id_Run_mode = sensor.getId_runMode();
        }
        return id_Run_mode;
    }

    public List<DataModels> _getAllDatas(RunModeBdd runModeBdd){
        List<DataModels> data = new ArrayList<DataModels>();
        runModeBdd.open();

        Cursor cursor = bdd.rawQuery("SELECT name, type, frequency, id_run_mode , nameMode, necessaryIndex FROM Sensors " +
                "INNER JOIN RunMode ON RunMode.id = id_run_mode " , null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            DataModels data1 = new DataModels();
            data1.setName(cursor.getString(0));
            data1.setType(cursor.getInt(1));
            data1.setFrequency(cursor.getDouble(2));
            data1.setId_runMode(cursor.getInt(3));
            data1.setNameMode(cursor.getString(4));
            data1.setNecessaryIndex(cursor.getInt(5));
            data.add(data1);
            cursor.moveToNext();
        }
        runModeBdd.close();
        return data;
    }
}
