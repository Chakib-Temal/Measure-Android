package com.chakibtemal.fr.modele.sqliteDb.ModelsRuning;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chakibtemal.fr.modele.sharedResources.RunMode;
import com.chakibtemal.fr.modele.sqliteDb.DatabaseHandler;

public class RunModeBdd {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "androidProject.db";

    private static final String TABLE_RUN_MODE = "RunMode";

    private static final String COL_MR_ID = "id";
    private static final int NUM_COL_MR_ID = 0;

    private static final String COL_MR_NECESSARY_INDEX = "necessaryIndex";
    private static final int NUM_COL_MR_NECESSARY_INDEX = 1;

    private static final String COL_MR_NAME_MODE = "nameMode";
    private static final int NUM_COL_MR_NAME_MODE = 2;

    private SQLiteDatabase bdd;
    private DatabaseHandler databaseHandler;

    public RunModeBdd(Context context){
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
    public long insertSingleModeRun(RunMode runMode ){
        ContentValues values = new ContentValues();
        values.put(COL_MR_NECESSARY_INDEX, runMode.getNecessaryIndex());
        values.put(COL_MR_NAME_MODE, runMode.getNameMode());
        return this.bdd.insert(TABLE_RUN_MODE, null, values);
    }

    public void deleteModeRun(int id){
        bdd.delete(DatabaseHandler.TABLE_RUN_MODE, DatabaseHandler.COL_MR_ID
                + " = " + id, null);
    }
}
