package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
	
    public int describeContents(){
        return 0;
    }

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeInt(this.id);
        out.writeString(this.title);
        out.writeTypedArray(this.authors, 0); //the object write into the parcel should also implements parcel
        out.writeString(this.isbn);
        out.writeString(this.price);
    }

    private Book(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.authors = in.createTypedArray(Author.CREATOR);
        this.isbn = in.readString();
        this.price = in.readString();
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

	public int id;
	
	public String title;
	
	public Author[] authors;
	
	public String isbn;
	
	public String price;

	public Book(int id, String title, Author[] author, String isbn, String price) {
		this.id = id;
		this.title = title;
		this.authors = author;
		this.isbn = isbn;
		this.price = price;
	}

	public String getFirstAuthor() {
		if (authors != null && authors.length > 0) {
			return authors[0].toString();
		} else {
			return "";
		}
	}

}