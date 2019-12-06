package com.example.neul7.monkeymp3;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "singDB.db";
    private static final int VERSION = 1;

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "CREATE TABLE singTBL (" +
                "singImage CHAR(300), " +
                "singTitle CHAR(100) PRIMARY KEY, " +
                "singer CHAR(30), " +
                "singTime CHAR(20));";
        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String str = "DROP TABLE IF EXISTS singTBL";
        db.execSQL(str);
        onCreate(db);
    }
}
