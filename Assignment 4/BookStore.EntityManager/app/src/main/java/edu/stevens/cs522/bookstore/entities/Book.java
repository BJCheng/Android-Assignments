package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import edu.stevens.cs522.bookstore.contracts.BookContract;

public class Book implements Parcelable {

    // TODO Modify this to implement the Parcelable interface.

    public long id;
    public String title;
    public Author[] authors;
    public String isbn;
    public String price;

    public Book() {
    }

    public String getFirstAuthor() {
        if (authors != null && authors.length > 0) {
            return authors[0].toString();
        } else {
            return "";
        }
    }

    public String getAuthors() {
        if (authors != null && authors.length > 0) {
            String authors = "";
            for(Author author: this.authors){
                authors += author.toString() + "|";
            }
            return authors.substring(0, authors.length()-1);
        } else {
            return "";
        }
    }

    public Book(Parcel in) {
        // TODO init from parcel
        this.id = in.readLong();
        this.title = in.readString();
        this.authors = in.createTypedArray(Author.CREATOR);
        this.isbn = in.readString();
        this.price = in.readString();
    }

    public void writeToParcel(Parcel out, int flags) {
        // TODO save state to parcel
        out.writeLong(this.id);
        out.writeString(this.title);
        out.writeTypedArray(this.authors, 0);
        out.writeString(this.isbn);
        out.writeString(this.price);
    }

    public Book(Cursor cursor) {
        // TODO init from cursor
        this.title = BookContract.getTitle(cursor);
        List<Author> authorsList = new ArrayList<>();
        for(String authorName: BookContract.getAuthors(cursor)){
            authorsList.add(new Author(authorName));
        }
        this.authors = authorsList.toArray(new Author[0]);
        this.isbn = BookContract.getIsbn(cursor);
        this.price = BookContract.getPrice(cursor);
    }

    public void writeToProvider(ContentValues out) {
        // TODO write to ContentValues
        BookContract.putTitle(out, this.title);
        BookContract.putIsbn(out, this.isbn);
        BookContract.putPrice(out, this.price);
        BookContract.putAuthors(out, this.authors);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>(){
        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int describeContents(){
        return 0;
    }

}