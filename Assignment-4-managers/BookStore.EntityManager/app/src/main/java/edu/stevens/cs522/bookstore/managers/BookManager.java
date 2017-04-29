package edu.stevens.cs522.bookstore.managers;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Set;

import edu.stevens.cs522.bookstore.activities.MainActivity;
import edu.stevens.cs522.bookstore.async.AsyncContentResolver;
import edu.stevens.cs522.bookstore.async.IContinue;
import edu.stevens.cs522.bookstore.async.IEntityCreator;
import edu.stevens.cs522.bookstore.async.QueryBuilder;
import edu.stevens.cs522.bookstore.async.QueryBuilder.IQueryListener;
import edu.stevens.cs522.bookstore.async.SimpleQueryBuilder;
import edu.stevens.cs522.bookstore.contracts.AuthorContract;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

/**
 * Created by dduggan.
 */

public class BookManager extends Manager<Book> {

    private static final int LOADER_ID = 1;
    private Activity context;

    private static final IEntityCreator<Book> creator = new IEntityCreator<Book>() {
        @Override
        public Book create(Cursor cursor) {
            return new Book(cursor);
        }
    };

    private AsyncContentResolver asyncResolver;
    final static Uri CONTENT_URI = BookContract.CONTENT_URI;

    public BookManager(Context context) {
        super(context, creator, LOADER_ID);
        asyncResolver = new AsyncContentResolver(context.getContentResolver());
    }

    public void getAllBooksAsync(IQueryListener<Book> listener) {
        // TODO use QueryBuilder to complete this
        executeQuery(CONTENT_URI, listener);
    }

    public void getBookAsync(String title, SimpleQueryBuilder.ISimpleQueryListener<Book> listener) {
        // TODO
    }

    public void persistAsync(final Book book) {
        // TODO
        ContentValues values = new ContentValues();
        book.writeToProvider(values);
        asyncResolver.insertAsync(CONTENT_URI, values, new IContinue<Uri>() {
            public void kontinue(Uri uri) {
                book.id = ContentUris.parseId(uri);
            }
        });
    }

    public void deleteBookAsync(Set<Long> toBeDeleted) {
        Long[] ids = new Long[toBeDeleted.size()];
        toBeDeleted.toArray(ids);
        String[] args = new String[ids.length];

        StringBuilder sb = new StringBuilder();
        if (ids.length > 0) {
            sb.append(AuthorContract.ID);
            sb.append("=?");
            args[0] = ids[0].toString();
            for (int ix=1; ix<ids.length; ix++) {
                sb.append(" or ");
                sb.append(AuthorContract.ID);
                sb.append("=?");
                args[ix] = ids[ix].toString();
            }
        }
        String select = sb.toString();

        asyncResolver.deleteAsync(BookContract.CONTENT_URI, select, args);
    }

    public void deleteAllBooksAsync() {
        asyncResolver.deleteAsync(BookContract.CONTENT_URI, null, null);
    }
}
