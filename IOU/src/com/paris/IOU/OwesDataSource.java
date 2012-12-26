package com.paris.IOU;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: MichaelParis
 * Date: 12/8/12
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OwesDataSource {

    //Database fields
    private SQLiteDatabase database;
    private MySQLiteOweHelper dbHelper;
    private String[] allColumns = { MySQLiteOweHelper.COLUMN_ID,
        MySQLiteOweHelper.COLUMN_NAME, MySQLiteOweHelper.COLUMN_OWEVAL,
        MySQLiteOweHelper.COLUMN_DESC, MySQLiteOweHelper.COLUMN_DATECREATED};

    public OwesDataSource(Context context) {
        dbHelper = new MySQLiteOweHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Owe createOwe(String name, double oweAmount, String desc) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteOweHelper.COLUMN_NAME, name);
        values.put(MySQLiteOweHelper.COLUMN_OWEVAL, oweAmount);
        values.put(MySQLiteOweHelper.COLUMN_DESC, desc);

        //Get current date to add to table
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String strDate = sdf.format(new Date());
        values.put(MySQLiteOweHelper.COLUMN_DATECREATED, strDate);

        long insertId = database.insert(MySQLiteOweHelper.TABLE_OWES, null,
                values);
        Cursor cursor = database.query(MySQLiteOweHelper.TABLE_OWES,
                allColumns, MySQLiteOweHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Owe newOwe = cursorToOwe(cursor);
        cursor.close();
        return newOwe;
    }

    public void updateOwe(Owe owe, double amount) {
        String strFilter = "_id=" + owe.getId();
        ContentValues values = new ContentValues();
        values.put(MySQLiteOweHelper.COLUMN_NAME, owe.getName());
        values.put(MySQLiteOweHelper.COLUMN_OWEVAL, amount);
        values.put(MySQLiteOweHelper.COLUMN_DESC, owe.getDescription());
        values.put(MySQLiteOweHelper.COLUMN_DATECREATED, owe.getDateTime());
        database.update(MySQLiteOweHelper.TABLE_OWES, values, strFilter, null);
    }

    public void deleteOwe(Owe owe) {
        long id = owe.getId();
        System.out.println("Owe deleted with id: " + id);
        database.delete(MySQLiteOweHelper.TABLE_OWES, MySQLiteOweHelper.COLUMN_ID +
        " = " + id, null);
    }


    public List<Owe> getAllOwes() {
        List<Owe> owes = new ArrayList<Owe>();

        Cursor cursor = database.query(MySQLiteOweHelper.TABLE_OWES,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while( !cursor.isAfterLast()) {
            Owe owe = cursorToOwe(cursor);
            owes.add(owe);
            cursor.moveToNext();
        }

        //Make sure to close cursor
        cursor.close();
        return owes;
    }

    private Owe cursorToOwe(Cursor cursor) {
        Owe owe = new Owe();
        owe.setId(cursor.getLong(0));
        owe.setName(cursor.getString(1));
        owe.setOweAmount(cursor.getDouble(2));
        owe.setDescription(cursor.getString(3));
        owe.setDateTime(cursor.getString(4));
        return owe;
    }

}
