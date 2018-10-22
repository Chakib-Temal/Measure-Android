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

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_SOLAR + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT NOT NULL, "
            + COL_VALUEX + " REAL, " + COL_VALUEY + " REAL, " + COL_VALUEZ + " REAL, " + COL_TIME +" REAL NOT NULL);";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_SOLAR + ";");
        onCreate(sqLiteDatabase);
    }
}
