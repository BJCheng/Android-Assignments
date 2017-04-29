package edu.stevens.cs522.bookstoredatabase.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import edu.stevens.cs522.bookstoredatabase.entities.Author;
import edu.stevens.cs522.bookstoredatabase.entities.Book;

/**
 * Created by dduggan.
 */

public class CartDbAdapter {

    private static final String DATABASE_NAME = "books.db";
    private static final String BOOK_TABLE = "books";
    private static final String AUTHOR_TABLE = "authors";

    private static final int DATABASE_VERSION = 2;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public List<Book> selectedBook = new LinkedList<>();

    public static class DatabaseHelper extends SQLiteOpenHelper {
        // TODO
        private static final String DATABASE_CREATE_BOOKS =
                "CREATE TABLE BOOKS(" +
                        "_id INTEGER PRIMARY KEY, " +
                        "price TEXT, " +
                        "title TEXT, " +
                        "isbn TEXT" +
                        "); ";
        private static final String DATABASE_CREATE_AUTHORS =
                "CREATE TABLE AUTHORS(" +
                        "_id INTEGER PRIMARY KEY," +
                        "first_name TEXT," +
                        "middle_initial TEXT," +
                        "last_name TEXT," +
                        "book_fk INTEGER NOT NULL," +
                        "FOREIGN KEY(BOOK_FK) REFERENCES BOOKS(_id) ON DELETE CASCADE" +
                        "); ";
        private static final String DATABASE_CREATE_INDEX =
                "CREATE INDEX AuthorsBookIndex ON Authors(book_fk);";

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO
            db.execSQL(DATABASE_CREATE_BOOKS);
            db.execSQL(DATABASE_CREATE_AUTHORS);
            db.execSQL(DATABASE_CREATE_INDEX);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE);
            db.execSQL(DATABASE_CREATE_BOOKS);
            db.execSQL(DATABASE_CREATE_AUTHORS);
        }
    }


    public CartDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        // TODO
        db = dbHelper.getReadableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    public Cursor fetchAllBooks() {
        // TODO
        String sql = "SELECT books._id, title, price, isbn, " +
                "GROUP_CONCAT(authors.first_name||' '||ifnull(authors.middle_initial, '')||' '||authors.last_name, '|') as authors " +
                "FROM books LEFT OUTER JOIN AUTHORS ON books._id = AUTHORS.book_fk " +
                "GROUP by books._id";
        return db.rawQuery(sql, null);
    }

    public Book fetchBook(long rowId) {
        // TODO
        Cursor cursor = db.rawQuery("SELECT books._id, title, price, isbn, " +
                "GROUP_CONCAT(authors.first_name||' '||ifnull(authors.middle_initial, '')||' '||authors.last_name, '|') as authors " +
                "FROM books LEFT OUTER JOIN AUTHORS ON books._id = AUTHORS.book_fk " +
                //"WHERE books._id = " + rowId +
                " GROUP by books._id", null);
        Book book = new Book(cursor);

        return book;
    }

    public void persist(Book book) throws SQLException {
        // TODO
        ContentValues bookContentValue = new ContentValues();
        book.writeToProvider(bookContentValue);
        book.id = db.insert(BOOK_TABLE, null, bookContentValue);

        ContentValues authorContentValue = new ContentValues();
        for (Author author : book.authors) {
            authorContentValue.clear();
            author.writeToProvider(authorContentValue, book);
            long authorId = db.insert(AUTHOR_TABLE, null, authorContentValue);
            System.out.print(authorId);
        }
    }

    public boolean delete(Book book) {
        // TODO
        int num = db.delete(BOOK_TABLE, "_id=?", new String[]{String.valueOf(book.id)});
        return false;
    }

    public int deleteAll() {
        // TODO
        db.delete(AUTHOR_TABLE, null, null);
        return db.delete(BOOK_TABLE, null, null);
    }

    public void close() {
        // TODO
        db.close();
    }

    public void addSelectedBook(Book book) {
        this.selectedBook.add(book);
    }

    void removeSelectedBook(Book book) {
        this.selectedBook.remove(book);
    }

}
