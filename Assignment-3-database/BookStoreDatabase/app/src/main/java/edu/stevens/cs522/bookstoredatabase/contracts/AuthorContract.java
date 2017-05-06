package edu.stevens.cs522.bookstoredatabase.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import edu.stevens.cs522.bookstoredatabase.entities.Author;

import static android.R.attr.author;

/**
 * Created by dduggan.
 */

public class AuthorContract implements BaseColumns {

    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String MIDDLE_INITIAL = "MIDDLE_INITIAL";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String BOOK_FK = "BOOK_FK";

    /*
     * FIRST_NAME column
     */
    private static int firstNameColumn = -1;

    public static String getFirstName(Cursor cursor) {
        if (firstNameColumn < 0) {
            firstNameColumn =  cursor.getColumnIndexOrThrow(FIRST_NAME);
        }
        return cursor.getString(firstNameColumn);
    }

    public static void putFirstName(ContentValues values, String firstName) {
        values.put(FIRST_NAME, firstName);
    }

    // TODO complete the definitions of the other operations
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
