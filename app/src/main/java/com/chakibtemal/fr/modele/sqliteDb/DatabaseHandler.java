package com.chakibtemal.fr.modele.sqliteDb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TABLE_SOLAR = "Solar";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_VALUEX = "valueX";
    private static final String COL_VALUEY = "valueY";
    private static final String COL_VALUEZ = "valueZ";
    private static final String COL_TIME = "time";

    private static final String CREATE_TABLE_SOLAR = "CREATE TABLE " + TABLE_SOLAR + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, "
            + COL_VALUEX + " REAL, " + COL_VALUEY + " REAL, " + COL_VALUEZ + " REAL, " + COL_TIME +" REAL NOT NULL);";


    private static final String TABLE_RUN_MODE = "RunMode";
    private static final String COL_MR_ID = "id";
    private static final String COL_MR_NECESSARY_INDEX = "necessaryIndex";
    private static final String COL_MR_NAME_MODE = "nameMode";

    private static final String CREATE_TABLE_RUN_MODE = "CREATE TABLE " + TABLE_RUN_MODE + " ("
            + COL_MR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_MR_NECESSARY_INDEX + " REAL NOT NULL, "
            + COL_MR_NAME_MODE + " TEXT NOT NULL );";


    private static final String TABLE_SENSORS = "Sensors";
    private static final String COL_S_ID = "id";
    private static final String COL_S_ID_RUN_MODE = "id_run_mode";
    private static final String COL_S_FREQUENCY = "frequency";
    private static final String COL_S_NAME = "name";
    private static final String COL_S_TYPE = "type";

    private static final String CREATE_TABLE_SENSORS = "CREATE TABLE " + TABLE_SENSORS + " ("
            + COL_S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_S_ID_RUN_MODE + " INTEGER NOT NULL, "
            + COL_S_FREQUENCY + " REAL NOT NULL, " + COL_S_NAME + " TEXT NOT NULL, " + COL_S_TYPE + " REAL);";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SOLAR);
        sqLiteDatabase.execSQL(CREATE_TABLE_RUN_MODE);
        sqLiteDatabase.execSQL(CREATE_TABLE_SENSORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_SOLAR + ";");
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_RUN_MODE + ";");
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_SENSORS + ";");
        onCreate(sqLiteDatabase);
    }
}
