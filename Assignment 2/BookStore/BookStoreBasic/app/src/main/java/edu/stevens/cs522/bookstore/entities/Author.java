package edu.stevens.cs522.bookstore.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Author implements Parcelable {

    public Author(){}

    public Author(String firstName){
        this.firstName = firstName;
    }
	
    @Override
    public int describeContents(){
        return 0;
    }

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(this.firstName);
        out.writeString(this.middleInitial);
        out.writeString(this.lastName);
    }

    private Author(Parcel in){
        this.firstName = in.readString();
        this.middleInitial = in.readString();
        this.lastName = in.readString();
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

	// NOTE: middleInitial may be NULL!
	
	public String firstName;
	
	public String middleInitial;
	
	public String lastName;

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

}
