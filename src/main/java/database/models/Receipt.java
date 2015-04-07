package database.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.sellcom.apps.tracker_material.Utils.Utilities;

import database.DataBaseAdapter;

/**
 * Created by juanc.jimenez on 30/06/14.
 */
public class Receipt extends Table {

    public static final String TABLE = "receipt";

    public static final String ID = "id";
    public static final String IMAGE = "image";

    public static long insert(Context context, int id, String image){

        ContentValues cv = new ContentValues();
        cv.put(ID, id);
        cv.put(IMAGE, image);

        return DataBaseAdapter.getDB(context).insert(TABLE, null, cv);
    }

    public static Bitmap getImage(Context context, int id) {

        Bitmap image = null;
        Cursor cursor = DataBaseAdapter.getDB(context).query(TABLE, null, ID + "=" + id, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String encodedImage = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE));
            image = Utilities.stringToBitmap(encodedImage);

            cursor.close();
        }
        return image;
    }
}
