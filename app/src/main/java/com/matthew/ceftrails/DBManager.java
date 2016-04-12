package com.matthew.ceftrails;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    private static DBManager db = null;

    private static SQLiteDatabase myDatabase;
    private static Context context;

    /**
     * Private constructor for singleton class.
     */
    private DBManager() {}

    /**
     * Used to implement singleton class.
     */
    public static DBManager getInstance(Context new_context) {
        if (db == null) db = new DBManager();

        context = new_context;

        myDatabase = context.openOrCreateDatabase("Routes", context.MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Routes (r_id INT(4), dateTime INT(15))");
        return db;
    }

    /**
     * Adds a route to the database
     */
    public void addRoute (int r_id) {
        try {
            Cursor c;

            long dateTime = System.currentTimeMillis();

            String execStr = "INSERT INTO Routes (r_id, dateTime) VALUES (" + r_id + ", '" + dateTime + "')";
            myDatabase.execSQL(execStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array of the r_ids for every route
     */
    public int[] getRoutes() {
        ArrayList<Integer> routes = new ArrayList<Integer>();

        myDatabase = context.openOrCreateDatabase("Routes", context.MODE_PRIVATE, null);
        Cursor c = myDatabase.rawQuery("SELECT r_id FROM Routes", null);

        c.moveToFirst();
        int numRoutes = c.getCount();
        for (int i=0; i<numRoutes; i++) {
            routes.add(c.getInt(c.getColumnIndex("r_id")));
            c.moveToNext();
        }

        int[] routesArr = new int[routes.size()];
        for (int i=0; i<routes.size(); i++) routesArr[i] = routes.get(i);
        return routesArr;
    }

    /**
     * Gets the r_id for the route at position pos in the Routes table. Assumes that pos < size of
     * the table
     */
    public int getRouteNum(int pos) {
        myDatabase = context.openOrCreateDatabase("Routes", context.MODE_PRIVATE, null);
        Cursor c = myDatabase.rawQuery("SELECT r_id FROM Routes", null);

        c.moveToFirst();
        int numRoutes = c.getCount();
        for (int i=0; i<pos; i++) c.moveToNext();

        return c.getInt(c.getColumnIndex("r_id"));
    }
}
