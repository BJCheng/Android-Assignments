package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import edu.stevens.cs522.bookstore.contracts.AuthorContract;

import static android.R.attr.author;

public class Author implements Parcelable {

    // TODO Modify this to implement the Parcelable interface.

    // NOTE: middleInitial may be NULL!

    public long id;

    public String firstName;

    public String middleInitial;

    public String lastName;

    public Author(String authorText) {
        String[] name = authorText.split(" ");
        switch (name.length) {
            case 0:
                firstName = lastName = "";
                break;
            case 1:
                firstName = "";
                lastName = name[0];
                break;
            case 2:
                firstName = name[0];
                lastName = name[1];
                break;
            default:
                firstName = name[0];
                middleInitial = name[1];
                lastName = name[2];
        }
    }

    public Author(Parcel in){
        this.id = in.readLong();
        this.firstName = in.readString();
        this.middleInitial = in.readString();
        this.lastName = in.readString();
    }

    public void writeToParcel(Parcel out, int flag){
        out.writeLong(this.id);
        out.writeString(this.firstName);
        out.writeString(this.middleInitial);
        out.writeString(this.lastName);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (firstName != null && !"".equals(firstName)) {
            sb.append(firstName);
            sb.append(' ');
        }
        if (middleInitial != null && !"".equals(middleInitial)) {
            sb.append(middleInitial);
            sb.append(' ');
        }
        if (lastName != null && !"".equals(lastName)) {
            sb.append(lastName);
        }
        return sb.toString();
    }

    public void writeToProvider(ContentValues out, long bookId){
        AuthorContract.putFirstName(out, this.firstName);
        AuthorContract.putMiddleInitial(out, this.middleInitial);
        AuthorContract.putLastName(out, this.lastName);
        AuthorContract.putBookFk(out, bookId);
    }

    public void Author(Cursor cursor){
        // TODO init from cursor
    }

    public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>(){
        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public int describeContents(){
        return 0;
    }

}
