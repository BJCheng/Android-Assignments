package edu.stevens.cs522.bookstore.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dduggan.
 */

public class AuthorContract implements BaseColumns {

    public static final String ID = _ID;;
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String MIDDLE_INITIAL = "MIDDLE_INITIAL";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String BOOK_FK = "BOOK_FK";

    private static Uri uriBuilder(String authority, String path){
        return new Uri.Builder().scheme("content").authority(authority).path(path).build();
    }

    public static final Uri CONTENT_URI = uriBuilder(BookContract.AUTHORITY, "authors");

    /*
     * NAME column
     */

    private static int nameColumn = -1;

    public static String getFirstName(Cursor cursor) {
        if (nameColumn < 0) {
            nameColumn =  cursor.getColumnIndexOrThrow(FIRST_NAME);;
        }
        return cursor.getString(nameColumn);
    }

    public static void putFirstName(ContentValues values, String firstName) {
        values.put(FIRST_NAME, firstName);
    }

    // TODO complete the definitions of the operations for Parcelable, cursors and contentvalues
    /*
     * MIDDLE_INITIAL column
     */
    public static String getMiddleInitial(Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(MIDDLE_INITIAL));
    }
    public static void putMiddleInitial(ContentValues values, String middleInitial){
        values.put(MIDDLE_INITIAL, middleInitial);
    }

    /*
     * LAST_NAME column
     */
    public static String getLastName(Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(LAST_NAME));
    }
    public static void putLastName(ContentValues values, String lastName){
        values.put(LAST_NAME, lastName);
    }

    /*
     * BOOK_FK column
     */
    public static long getBookFk(Cursor cursor){
        return cursor.getLong(cursor.getColumnIndex(BOOK_FK));
    }
    public static void putBookFk(ContentValues values, long bookFk){
        values.put(BOOK_FK, bookFk);
    }
}
