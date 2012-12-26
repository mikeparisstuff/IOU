package com.paris.IOU;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/16/12
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLiteOwedHelper extends SQLiteOpenHelper{

    public static final String TABLE_OWEDS = "oweds";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_OWEDVAL = "owedval";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_DATECREATED = "datecreated";

    public static final String DATABASE_NAME = "oweds.db";
    //ADDING DATE AND DESCRIPTION---> VERSION 2
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_OWEDS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_OWEDVAL
            + " text not null, " + COLUMN_DESC
            + " text not null, " + COLUMN_DATECREATED
            + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    public MySQLiteOwedHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteOwedHelper.class.getName(),
                "Upgrading database from version " + oldVersion + "to "
                + newVersion + "which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWEDS);
        onCreate(db);
    }
}
