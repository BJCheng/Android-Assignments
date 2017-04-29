package edu.stevens.cs522.bookstore.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import edu.stevens.cs522.bookstore.contracts.AuthorContract;
import edu.stevens.cs522.bookstore.contracts.BookContract;

import static edu.stevens.cs522.bookstore.contracts.BookContract.CONTENT_PATH;
import static edu.stevens.cs522.bookstore.contracts.BookContract.CONTENT_PATH_ITEM;

public class BookProvider extends ContentProvider {
    public BookProvider() {
    }

    private static final String AUTHORITY = BookContract.AUTHORITY;
    private static final String CONTENT_PATH = BookContract.CONTENT_PATH;
    private static final String CONTENT_PATH_ITEM = BookContract.CONTENT_PATH_ITEM;
    private static final String AUTHOR_CONTENT_PATH = BookContract.CONTENT_PATH(AuthorContract.CONTENT_URI);


    private static final String DATABASE_NAME = "books.db";
    private static final int DATABASE_VERSION = 1;
    private static final String BOOKS_TABLE = "books";
    private static final String AUTHORS_TABLE = "authors";

    // Create the constants used to differentiate between the different URI  requests.
    private static final int ALL_ROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final int AUTHOR_CODE = 3;

    public static class DbHelper extends SQLiteOpenHelper {

        private String booksCreateSql = "CREATE TABLE books(" +
                "_id INTEGER PRIMARY KEY, " +
                "title TEXT, " +
                "isbn TEXT, " +
                "price TEXT);";
        private String authorsCreateSql = "CREATE TABLE authors(" +
                "_id INTEGER PRIMARY KEY, " +
                "first_name TEXT, " +
                "middle_initial TEXT, " +
                "last_name TEXT, " +
                "book_fk INTEGER NOT NULL, " +
                "FOREIGN KEY(book_fk)  REFERENCES books(_id) ON DELETE CASCADE);";

        public DbHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO initialize database tables
            db.execSQL(booksCreateSql);
            db.execSQL(authorsCreateSql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO upgrade database if necessary
        }
    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        // Initialize your content provider on startup.
        dbHelper = new DbHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }

    // Used to dispatch operation based on URI
    private static final UriMatcher uriMatcher;

    // uriMatcher.addURI(AUTHORITY, CONTENT_PATH, OPCODE)
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH_ITEM, SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, AUTHOR_CONTENT_PATH, AUTHOR_CODE);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/vnd." + BookContract.AUTHORITY + "." + BOOKS_TABLE;
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd." + BookContract.AUTHORITY + "." + BOOKS_TABLE;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                // TODO: Implement this to handle requests to insert a new row.
                // Make sure to notify any observers
                id = db.insert(BOOKS_TABLE, null, values);
                Uri newUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
            case SINGLE_ROW:
                throw new IllegalArgumentException("insert expects a whole-table URI");
            case AUTHOR_CODE:
                id = db.insert(AUTHORS_TABLE, null, values);
                Uri newAuthorUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(newAuthorUri, null);
                return newAuthorUri;
            default:
                throw new IllegalStateException("insert: bad case");
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS:
                // TODO: Implement this to handle query of all books.
                String sql = "SELECT books._id, title, price, isbn, " +
                        "GROUP_CONCAT(ifnull(authors.first_name, '')||' '||ifnull(authors.middle_initial, '')||' '||ifnull(authors.last_name, ''), '|') as authors " +
                        "FROM books LEFT OUTER JOIN AUTHORS ON books._id = AUTHORS.book_fk " +
                        "GROUP by books._id";
                Cursor cursor =  db.rawQuery(sql, null);
//                Cursor cursor = db.query(BOOKS_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case SINGLE_ROW:
                // TODO: Implement this to handle query of a specific book.
                throw new UnsupportedOperationException("Not yet implemented");
            default:
                throw new IllegalStateException("query: bad case");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new IllegalStateException("Update of books not supported");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int numberDeleted = db.delete(BOOKS_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return numberDeleted;
    }

}
