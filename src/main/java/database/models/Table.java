package database.models;


import android.content.Context;

import database.DataBaseAdapter;


public class Table {

    String name;

    public static void clear(Context context,String table)
    {
        DataBaseAdapter.getDB(context).delete(table,null,null);
    }

    public static void getAll(Context context,String table)
    {
        DataBaseAdapter.getDB(context).execSQL("select * from "+table);
    }

    public static void getById(Context context,String table, String id)
    {
        DataBaseAdapter.getDB(context).execSQL("select * from "+table+" where id = "+id);
    }


}
