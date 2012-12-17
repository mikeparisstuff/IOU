package com.paris.IOU;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/8/12
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLiteOweHelper extends SQLiteOpenHelper {

    public static final String TABLE_OWES = "owes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_OWE = "owe";

    private static final String DATABASE_NAME = "owes.db";
    private static final int DATABASE_VERSION = 3;

    //Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_OWES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_OWE
            + " text not null);";

    public MySQLiteOweHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteOweHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + " which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWES);
        onCreate(db);
    }

}
