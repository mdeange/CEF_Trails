package com.matthew.ceftrails;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class InternalDB {
    private static InternalDB db = null;

    private static SQLiteDatabase myDatabase;
    private static Context context;

    /**
     * Private constructor for singleton class.
     */
    private InternalDB() {}

    /**
     * Used to implement singleton class.
     */
    public static InternalDB getInstance(Context new_context) {
        if (db == null) db = new InternalDB();

        context = new_context;

        myDatabase = context.openOrCreateDatabase("Routes", context.MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Routes (r_id INT(4), name VARCHAR, dateTime INT(15))");
        return db;
    }

    /**
     * Adds a route to the database
     */
    public void addRoute (int r_id, String name) {
        try {
            Cursor c;

            long dateTime = System.currentTimeMillis();

            String execStr = "INSERT INTO Routes (r_id, name, dateTime) VALUES (" + r_id + ", '" + name + "', " + dateTime + ")";
            myDatabase.execSQL(execStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an array of the r_ids for every route
     */
    public String[] getRoutes() {
        ArrayList<String> routes = new ArrayList<String>();

        myDatabase = context.openOrCreateDatabase("Routes", context.MODE_PRIVATE, null);
        Cursor c = myDatabase.rawQuery("SELECT name FROM Routes", null);

        c.moveToFirst();
        int numRoutes = c.getCount();
        for (int i=0; i<numRoutes; i++) {
            routes.add(c.getString(c.getColumnIndex("name")));
            c.moveToNext();
        }

        String[] routesArr = new String[routes.size()];
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
