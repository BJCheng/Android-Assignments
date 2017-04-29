package edu.stevens.cs522.bookstoredatabase.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.renderscript.Sampler;

import java.util.regex.Pattern;

/**
 * Created by dduggan.
 */

public class BookContract implements BaseColumns {

    public static final String TITLE = "title";
    public static final String AUTHORS = "authors";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";

    /*
     * TITLE column
     */

    private static int titleColumn = -1;

    public static String getTitle(Cursor cursor) {
        if (titleColumn < 0) {
            titleColumn =  cursor.getColumnIndexOrThrow(TITLE);
        }
        return cursor.getString(titleColumn);
    }

    public static void putTitle(ContentValues values, String title) {
        values.put(TITLE, title);
    }

    /*
     * Synthetic authors column
     */
    public static final char SEPARATOR_CHAR = '|';

    private static final Pattern SEPARATOR = Pattern.compile(Character.toString(SEPARATOR_CHAR), Pattern.LITERAL);

    public static String[] readStringArray(String in) {
        return SEPARATOR.split(in);
    }

    private static int authorColumn = -1;

    public static String[] getAuthors(Cursor cursor) {
        if (authorColumn < 0) {
            authorColumn =  cursor.getColumnIndexOrThrow(AUTHORS);
        }
        return readStringArray(cursor.getString(authorColumn));
    }

    // TODO complete definitions of other getter and setter operations
    /*
     * ISBN column
     */
    public static int isbnColumnIndex = -1;
    public static final String getIsbn(Cursor cursor){
        if(isbnColumnIndex<0)
            isbnColumnIndex = cursor.getColumnIndexOrThrow(ISBN);
        return cursor.getString(isbnColumnIndex);
    }
    public static final void putIsbn(ContentValues contentValues, String isbn){
        contentValues.put(ISBN, isbn);
    }

    /*
     * PRICE column
     */
    public static int priceColumnIndex = -1;
    public static final String getPrice(Cursor cursor){
        if(priceColumnIndex<0)
            priceColumnIndex = cursor.getColumnIndexOrThrow(PRICE);
        return cursor.getString(priceColumnIndex);
    }
    public static final void putPrice(ContentValues contentValue, String price){
        contentValue.put(PRICE, price);
    }

}
